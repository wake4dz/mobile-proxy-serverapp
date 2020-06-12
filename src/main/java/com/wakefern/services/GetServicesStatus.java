package com.wakefern.services;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.account.authentication.AuthenticateUI;
import com.wakefern.coupons.v2.ObtainUserSession;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.products.reports.GetToken;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;

/**
 * This api will make request to several services currently being used by ShopRite app.
 * 	The purpose of this api is to verify the integrity of the token in VCAP against the services. 
 * 	This new api is created to accommodate the migration of hardcoded token to VCAP environment variable key/value pair.
 * 	The impact services will be
 * 		1) Coupon
 * 		2) Item Locator
 * 		3) Product Recommendation
 * 		4) Business Intelligent (for login to report product not found sku)
 * @author sfl1c
 *
 */

@Path(ApplicationConstants.Requests.VerifyServices)
public class GetServicesStatus extends BaseService{
	private final static Logger logger = Logger.getLogger(GetServicesStatus.class);

    @POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
    									@HeaderParam(ApplicationConstants.Requests.Header.contentAccept) String accept,
    									String jsonBody) throws Exception, IOException {

    		String ppcKey = "ppc";
    		String storeIdKey = "storeId";
    		
    		StringBuilder sb = new StringBuilder();
    		if(contentType.equals("application/wakefern-services")) {

    	        JSONObject jsonData = new JSONObject(jsonBody);
    	        
    	        String ppc = (jsonData.has(ppcKey)) ? jsonData.getString(ppcKey) : null; 
    	        String externalStoreId  = (jsonData.has(storeIdKey))    ? jsonData.getString(storeIdKey) : "";
    	        
		    	sb.append("[");
		    	
		    	//check VCAP names not empty or null
			sb.append(checkVCAPNames());
				
		    	// verify MI9 authorization service
		    	sb.append(this.printServiceName("MI9 AUTHORIZATION", 	getMI9AuthStatus()));
		    	sb.append(",");
		    	
		    	// verify coupon V2 service..
		    	sb.append(this.printServiceName("COUPON V2", getCouponV2Status(ppc)));
		    	sb.append(",");
		    	
		    	// verify Item Locator service..
			sb.append(this.printServiceName("ITEM LOCATOR / DIGITAL RECEIPT", getItemLocatorStatus()));
			sb.append(",");
	    	
		    	//verify product not found service..
		    	sb.append(this.printServiceName("BI LOGS PRODUCT NOT FOUND", getBIProdNotFoundStatus()));
		    	sb.append(",");
			
			// verify product recommendation service..
			sb.append(this.printServiceName("PRODUCT RECOMMENDATION", getProdRecommendationStatus(externalStoreId, ppc)));
			sb.append("]");
		} else {
			sb.append("{\"error\":\"wrong header type\"}");
    		}
		return this.createValidResponse(sb.toString());
    }
    
    private String printServiceName(String serviceName, String status) {
    		String srvName="{\"service name\":\""+serviceName+"\",\"service status\":\""+status+"\"}";
    		return srvName;
    }
    
    /**
     * verify coupon V2 service..
     * @param ppc
     * @return
     */
    private String getCouponV2Status(String ppc) {
	    	// verify coupon V2 service..
	    	ObtainUserSession coupon = new ObtainUserSession();
	    	String couponPayload=  ppc != null ? "\"fsn\":\""+ppc+"\"" : ""; // token will return with empty ppc, for guest user to see coupons.
	    	String couponResp = coupon.getCouponV2Token("{"+couponPayload+"}");
	    return !couponResp.contains("error") ?"active" : "inactive";
    }
    
    /**
     * verify Item Locator service..
     * @return
     */
    private String getItemLocatorStatus() {
    		String serviceStatus;
	    	try {
			WakefernAuth auth = new WakefernAuth();
			String itemLocResp = auth.getInfo(
					MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
			serviceStatus = "active";
	    	} catch(Exception e) {
	    		logger.error("[GetServicesStatus]:: Item Locator exception resp: "+e.getMessage());
	    		serviceStatus = "inactive";
	    	}
	    	return serviceStatus;
    }

	/**
	 * verify product not found service..
	 * @return
	 */
    private String getBIProdNotFoundStatus() {
	    	GetToken gt = new GetToken();
	    	String prodNotFoundResp = gt.getProdNotFoundLogin();
	
	    	return !prodNotFoundResp.contains("error") ? "active" : "inactive";
    }
    
    /**
     * verify product recommendation service..
     * @param externalStoreId
     * @param ppc
     * @return
     */
    private String getProdRecommendationStatus(String externalStoreId, String ppc) {
    		String serviceStatus;
	    	try {
			this.requestHeader = new MWGHeader();
		    this.requestToken  = MWGApplicationConstants.getProductRecmdAuthToken();
		    this.requestPath = ApplicationConstants.Requests.Recommendations.ProductRecommendationsv2 + "/"+externalStoreId+"/email//fsn/"+ppc;
		    
		    ServiceMappings secondMapping = new ServiceMappings();
		    secondMapping.setMappingWithURL(this, ApplicationConstants.Requests.Recommendations.BaseRecommendationsURL);
			String secondMapPath = secondMapping.getPath();
		
			// CALL & GET LIST OF RECOMMENDED SKUs
			String prodRecmdResp = HTTPRequest.executeGet(secondMapPath, secondMapping.getgenericHeader(), 0);
			logger.info("[GetServicesStatus]:: Product Recommendation resp: "+prodRecmdResp);
    			serviceStatus = "active";
	    	} catch (Exception ex) {
			logger.error("[GetServicesStatus]:: Product Recommendation Exception: "+ex.getMessage());
	    		serviceStatus = "inactive";
	    	}
	    	return serviceStatus;
    }
    
    /**
     * verify MI9 Authorization service..
     * @return
     */
    private String getMI9AuthStatus() {
		String serviceStatus;
    		try {
	    		AuthenticateUI aui = new AuthenticateUI();
	    		String authResp = aui.getInfo();
	    		System.out.println(authResp);
	    		serviceStatus = authResp.contains("Token") ? "active" : "inactive";
    		} catch(Exception e) {
	    		serviceStatus = "inactive";
    		}
    		return serviceStatus;
    }
    
    /**
     * Checking VCAP names not empty or null
     * @return
     */
    private String checkVCAPNames() {
    		StringBuilder sb = new StringBuilder();
    		verifyVCAP(WakefernApplicationConstants.VCAPKeys.COUPON_V2_KEY, sb);
    		verifyVCAP(WakefernApplicationConstants.VCAPKeys.SR_MWG_PROD_KEY, sb);
    		verifyVCAP(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY, sb);
    		verifyVCAP(WakefernApplicationConstants.VCAPKeys.PROD_NOT_FOUND_LOGIN, sb);
    		verifyVCAP(WakefernApplicationConstants.VCAPKeys.SR_PRODUCT_RECOMMENDATION_KEY, sb);
		return !sb.toString().isEmpty() 
				? "{\"VCAP names\":\""+sb.toString()+"\",\"description\":\"ATTENTION!!! Please check Bluemix VCAP, the listed VCAP name(s) are empty or null.\"},"
				: "";
    }
    
    private void verifyVCAP(String vcapName, StringBuilder sb) {
    		if(!notNullEmpty(MWGApplicationConstants.getSystemPropertyValue(vcapName))){
			sb.append(vcapName); sb.append(" / ");
    		}
    }
    
    private boolean notNullEmpty(String vcapName) {
    		if(vcapName != null && !vcapName.isEmpty())
    			return true;
    		return false;
    }
}
