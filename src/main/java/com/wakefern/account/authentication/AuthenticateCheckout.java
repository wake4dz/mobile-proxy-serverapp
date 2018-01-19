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
public class AuthenticateCheckout extends BaseService {

	private final static Logger logger = Logger.getLogger("Authentication");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public AuthenticateCheckout() {
        this.requestPath = MWGApplicationConstants.Requests.Authentication.prefix + MWGApplicationConstants.Requests.Authentication.checkout;
    }
	
    @PUT
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Authentication.checkout)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.token) String token,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData
	) throws Exception, IOException {
    	    	
        try {
            this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, sessionToken);
    			this.requestParams = new HashMap<String, String>();
        		
    			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.token, token);
        		String jsonResp = this.mwgRequest(BaseService.ReqType.PUT, jsonData);
            return this.createValidResponse(jsonResp);
        
        } catch (Exception e) {
        		logger.log(Level.SEVERE, "[getResponse]::Exception getResponse ", e);
            return this.createErrorResponse(e);
        }
    }
}
