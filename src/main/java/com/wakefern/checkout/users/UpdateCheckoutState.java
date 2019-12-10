package com.wakefern.checkout.users;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.services.MI9TimeoutService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class UpdateCheckoutState extends BaseService {
	
	private final static Logger logger = Logger.getLogger(UpdateCheckoutState.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public UpdateCheckoutState() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.userCheckoutState;
    }
    
	@PUT
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
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.checkoutResults, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);

            String jsonResponse = this.mwgRequest(BaseService.ReqType.PUT, null, MI9TimeoutService.CHECKOUT_UPDATE_STATE, MI9TimeoutService.getTimeout(MI9TimeoutService.CHECKOUT_UPDATE_STATE));
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USERS_UPDATE_CHECKOUT_STATE);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"mwgStoreID", mwgStoreID, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}


