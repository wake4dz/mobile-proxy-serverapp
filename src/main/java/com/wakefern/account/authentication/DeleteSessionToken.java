package com.wakefern.account.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(MWGApplicationConstants.Requests.Authentication.prefix)
public class DeleteSessionToken extends BaseService {

	private final static Logger logger = Logger.getLogger("DeleteSessionToken");
	
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
    		String jsonData
	) throws Exception, IOException {
    	    	
    		// MWG expects the 'secret' to be sent as the Authentication header, instead of the usual session token.
    		// The session token, is instead sent as a path parameter.
    	
        try {
            this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, secret);
    			this.requestParams = new HashMap<String, String>();
    			
    			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.token, sessionToken);
    			
    			String jsonResp = this.mwgRequest(BaseService.ReqType.DELETE, jsonData, "com.wakefern.account.authentication.DeleteSessionToken");
            
    			return this.createValidResponse(jsonResp);
        
        } catch (Exception e) {
        		logger.log(Level.SEVERE, "[getResponse]::Exception getResponse ", e);
            return this.createErrorResponse(e);
        }
    }
}
