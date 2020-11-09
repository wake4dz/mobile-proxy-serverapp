package com.wakefern.api.mi9.v7.account.users;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class UpdatePreferredStore extends BaseService {
	
	private final static Logger logger = Logger.getLogger(CreateRegistration.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public UpdatePreferredStore() {
        this.requestPath = MWGApplicationConstants.Requests.Account.prefix + MWGApplicationConstants.Requests.Account.profile;
    }
    
	@PUT
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Account.profile)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,    		
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonData
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Account.prefStore, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		

            String jsonResponse = this.mwgRequest(BaseService.ReqType.PUT, jsonData, "com.wakefern.account.users.UpdatePreferredStore");
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("chainId", chainID, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonData);
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USERS_UPDATE_PREFERRED_STORE);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainId", chainID, 
        			"userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonData);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
          
        }
    }
}
