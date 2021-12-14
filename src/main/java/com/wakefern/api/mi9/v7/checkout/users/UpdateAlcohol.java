package com.wakefern.api.mi9.v7.checkout.users;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class UpdateAlcohol extends BaseService {
	
	private final static Logger logger = LogManager.getLogger(UpdateAlcohol.class);
	
	private static final String TAG = UpdateAlcohol.class.getName();
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public UpdateAlcohol() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.alcohol;
    }
    
	@PUT
	// Mi9 didn't provide an Accept header type since it returns 204 from Mi9
    //@Consumes(MWGApplicationConstants.Headers.generic) 
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.alcohol)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData
	) {
		try {	
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Checkout.alcoholContentType, sessionToken);
			this.requestParams = new HashMap<>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);

            String jsonResponse = this.mwgRequest(BaseService.ReqType.PUT, jsonData, TAG);
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", MWGApplicationConstants.Headers.json, 
		        			"contentType", MWGApplicationConstants.Headers.Checkout.alcoholContentType, "httpBody", jsonData );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USERS_UPDATE_ALCOHOL);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"mwgStoreID", mwgStoreID, "userID", userID, 
        			"sessionToken", sessionToken, "accept", MWGApplicationConstants.Headers.json, 
        			"contentType", MWGApplicationConstants.Headers.Checkout.alcoholContentType, "httpBody", jsonData );

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}


