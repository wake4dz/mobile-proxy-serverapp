package com.wakefern.api.mi9.v7.account.authentication;

import com.wakefern.global.*;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Authentication.prefix)
public class DeleteSessionToken extends BaseService {

	private final static Logger logger = LogManager.getLogger(DeleteSessionToken.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public DeleteSessionToken() {
        this.requestPath = MWGApplicationConstants.Requests.Authentication.prefix + MWGApplicationConstants.Requests.Authentication.delete;
    }
	
    @DELETE
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Authentication.delete)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.token) String secret,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonData
	) {
    	    	
    		// MWG expects the 'secret' to be sent as the Authentication header, instead of the usual session token.
    		// The session token, is instead sent as a path parameter.
    	
        try {
            this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, secret);
			this.requestParams = new HashMap<String, String>();
			
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.token, sessionToken);
			
			String jsonResp = this.mwgRequest(BaseService.ReqType.DELETE, jsonData, "com.wakefern.account.authentication.DeleteSessionToken");
        
			return this.createValidResponse(jsonResp);
        
        } catch (Exception e) {

        	LogUtil.addErrorMaps(e, MwgErrorType.AUTHENTICATION_DELETE_SESSION_TOKEN);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}
