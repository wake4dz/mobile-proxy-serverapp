package com.wakefern.api.mi9.v7.coupons;

import java.io.IOException;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernHeader;

/**
 * Created by loi cao on 10/27/18.
 */
@Path(ApplicationConstants.Requests.Coupons.CouponIDByPromoCode)
public class GetCouponIDByPromoCode extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetCouponIDByPromoCode.class);
    public JSONObject matchedObjects;

    @GET
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@DefaultValue(WakefernApplicationConstants.Coupons.Metadata.PPC_All) 
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PPC) String ppcParam,
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PromoteCode) String promoCode,
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PromoteCodeAdd) String add,
    								@HeaderParam("Authorization") String authToken) {
        try {
	        if (ppcParam.equals("")) {
	            return this.createResponse(400);
	        }
            return this.createValidResponse(getInfo(ppcParam, promoCode, add, authToken));
        } catch (Exception e){
    			LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_COUPON_ID_BY_PROMO_CODE);
        		String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"ppcParam", ppcParam, "promoCode", promoCode, "authToken", authToken, "PromoteCodeAdd", add);
        		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
        		
            return this.createErrorResponse(e);
        }
    }

    @POST
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getPOSTResponse(@DefaultValue(WakefernApplicationConstants.Coupons.Metadata.PPC_All) 
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PPC) String ppcParam,
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PromoteCode) String promoCode,
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PromoteCodeAdd) String add,
								@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType, 
    								@HeaderParam("Authorization") String authToken,
    								String jsonString) {
        try {
	    	    if (ppcParam.equals("") && authToken.isEmpty()) {
	    	        return this.createResponse(400);
	    	    }
            return this.createValidResponse(getInfo(ppcParam, promoCode, add, authToken));
        } catch (Exception e){
        		LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_COUPON_ID_BY_PROMO_CODE);
        	
        		String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"ppcParam", ppcParam, "promoCode", promoCode, "authToken", authToken, "contentType", contentType);
        		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
        		
            return this.createErrorResponse(e);
        }
    }

    private String getInfo(String ppcParam, String promoCode, String add, String authToken) throws Exception, IOException {

		this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;
	
	    matchedObjects = new JSONObject();
	    prepareResponse(ppcParam, promoCode, add);
	
	    //Execute Post
	    ServiceMappings serviceMappings = new ServiceMappings();
	    serviceMappings.setCouponMapping(this);

        return HTTPRequest.executePostJSON(serviceMappings.getPath(), "{}", serviceMappings.getgenericHeader(), VcapProcessor.getApiLowTimeout());
    }

    public GetCouponIDByPromoCode() {     this.requestHeader = new WakefernHeader();    }

    private void prepareResponse(String ppcParam, String promoCode, String promoCodeAdd){
    		StringBuilder sb = new StringBuilder();
    		sb.append(ApplicationConstants.Requests.Coupons.BaseCouponURL);
    		sb.append(ApplicationConstants.Requests.Coupons.CouponIDByPromoCode);
    		sb.append(WakefernApplicationConstants.Coupons.Metadata.PPCQuery); sb.append(ppcParam);
    		sb.append(WakefernApplicationConstants.Coupons.Metadata.PromoteCodeParam); sb.append(promoCode);
    		sb.append(WakefernApplicationConstants.Coupons.Metadata.PromoteCodeAddParam); sb.append(promoCodeAdd);
        this.requestPath =  sb.toString();
    }
}
