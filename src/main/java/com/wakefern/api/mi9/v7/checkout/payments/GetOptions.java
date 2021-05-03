package com.wakefern.api.mi9.v7.checkout.payments;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetOptions extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetOptions.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetOptions() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.payments;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.payments)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.fulfillType) String fulfillType,
    		
    		@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
        try {	
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.paymentsV3, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.fulfillType, fulfillType);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.checkout.payments.GetOptions");
            
            logger.debug("jsonResponse in GetOptions(): " + jsonResponse);
            
            // 11/17/2020 Danny Zheng
            // In the emergency release of v3.23.1 to filter out the Thanksgiving payment option 
            try {
	            JSONObject responseObj = new JSONObject(jsonResponse);
	            
	            if (ApplicationUtils.isReleaseApplicable(appVersion, 4, 21)) { // for any UI app release < 4.21
		            JSONArray paymentMethodsArray = responseObj.getJSONArray("PaymentMethods");  
					int paymentsSize = paymentMethodsArray.length();
					for (int i = 0; i < paymentsSize; i++) {
						if (paymentMethodsArray.get(i) instanceof JSONObject) {
							JSONObject paymentObj = paymentMethodsArray.getJSONObject(i);
							if (paymentObj.getInt("Id") == 8) {
								logger.debug(paymentObj.getInt("Id"));
								logger.debug(paymentObj.getString("Name"));
								logger.debug(paymentObj.getString("PaymentMethodMessage"));
								
								// found it, remove it, break out the for loop
								paymentMethodsArray.remove(i);
								break;
							}
						}
					}
					logger.debug("jsonResponse after filter out Thanksgiving payment option in GetOptions(): " + responseObj.toString());
	            }
	            
				jsonResponse = responseObj.toString();
				
            } catch (Exception error) {
            	logger.error("Something went wrong in filtering out the Thanksgiving payment option: " + LogUtil.getRelevantStackTrace(error));;
            	// jsonResponse is the original data returned from Mi9, no filtering just in case something is not right
            }
            
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.PAYMENTS_GET_OPTIONS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"mwgStoreID", mwgStoreID, "appVersion", appVersion, "fulfillType", fulfillType, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
	
}


