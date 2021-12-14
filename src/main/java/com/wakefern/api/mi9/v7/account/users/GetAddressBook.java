package com.wakefern.api.mi9.v7.account.users;

import java.io.IOException;
import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class GetAddressBook extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetAddressBook.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
    public GetAddressBook() {
        this.requestPath = MWGApplicationConstants.Requests.Account.prefix + MWGApplicationConstants.Requests.Account.addresses;
    } 

    @GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Account.addresses)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID, 
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonBody) {
			
		try {
			String jsonResponse = makeRequest(sessionToken, MWGApplicationConstants.Headers.Account.address, chainID, userID);
			
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("chainId", chainID, "userID", userID, "sessionToken", sessionToken );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
			return this.createValidResponse(jsonResponse);
		
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USERS_GET_ADDRESSES);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainId", chainID, 
        			"userID", userID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
		} 
    }
        
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

    private String makeRequest(String sessionToken, String acceptType, String chainID, String userID) throws Exception, IOException {
    	this.requestParams = new HashMap<String, String>();
        this.requestHeader = new MWGHeader(acceptType, MWGApplicationConstants.Headers.json, sessionToken);
        this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
        this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
        
        return this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.account.users.GetAddressBook");
    }
}
