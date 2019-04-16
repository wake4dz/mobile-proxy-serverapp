package com.wakefern.checkout.users;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetCheckoutState extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetCheckoutState.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetCheckoutState() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.userCheckoutState;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.userCheckoutState)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.checkoutV3,
					MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);

            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.checkout.users.GetCheckoutState");
            
            /**
             * DMAU-533-Checkout Combined Service Fee, MWG is combining all charges for pickup & delivery into one fee vs scattered fee,
             * This implementation is to accommodate MWG's changes. 
             * Previously, MWG's v2 service is returning "Service Fee Total" attribute, the new v3 is returning "Combined Service Fee Total",
             * Wakefern will translate "Combined Service Fee Total" to "Service Fee" for pickup and "Service and Delivery Fee" for delivery
             *  & display it in mobile UI
             *  
             *  on 4/8/2019 with enh/remove-checkoutv3-vcap branch, checkoutv3 is now permanent
             */
            try {

        		String combinedServiceFee = "Combined Service Fee Total";
        		String pickupServiceFee = "Service Fee";
        		String deliveryServiceFee = "Service and Delivery Fee";
        		
        		String[] respArr = jsonResponse.split("\"payment\"");
        		if(respArr.length>1) {
        			jsonResponse = respArr[1].contains("Pickup") ? jsonResponse.replaceFirst(combinedServiceFee, pickupServiceFee) : jsonResponse.replaceFirst(combinedServiceFee, deliveryServiceFee);
        		} else {
            		logger.error("error geting Checkout fee - " + jsonResponse);
        		}
        
            } catch(Exception e) {
            		//Error in looking for "payment" keyword, proceed as this feature is not affecting checkout
            		logger.error("Combined Service Fee error - " + e.getMessage() + jsonResponse);
            }
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USERS_GET_CHECKOUT_STATE);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"mwgStoreID", mwgStoreID, "userID", userID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
	

}


