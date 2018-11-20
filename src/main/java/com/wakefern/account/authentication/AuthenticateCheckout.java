package com.wakefern.account.authentication;

import com.wakefern.global.*;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;


@Path(MWGApplicationConstants.Requests.Authentication.prefix)
public class AuthenticateCheckout extends BaseService {

	private final static Logger logger = Logger.getLogger(AuthenticateCheckout.class);
	
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
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.token) String secret,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonData
	) {
    	    	
    		// This request relies on a Deprecated MWG endpoint.  
    		// Wakefern has specifically asked that this endpoint continue to be used.  
    		// Be aware that it may be removed from service at some point in the future.
    		//
    		// The structure of this request is a little odd.
    		// MWG expects the 'secret' to be sent as the Authentication header, instead of the usual session token.
    		// The session token, is instead sent as a path parameter.
    	
    	long startTime, endTime, actualTime;
    	
        try {
            this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, secret);
			this.requestParams = new HashMap<String, String>();
			
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.token, sessionToken);
			
			startTime = System.currentTimeMillis();
			String jsonResp = this.mwgRequest(BaseService.ReqType.PUT, jsonData, "com.wakefern.account.authentication.AuthenticateCheckout");
            
			// for warning any API call time exceeding its own upper limited time defined in mwgApiWarnTime.java
			endTime = System.currentTimeMillis();
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.AUTHENTICATION_AUTHENTICATE_CHECKOUT.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is " +
						MwgApiWarnTime.AUTHENTICATION_AUTHENTICATE_CHECKOUT.getWarnTime() + " ms.");
			}
			
			return this.createValidResponse(jsonResp);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.AUTHENTICATION_AUTHENTICATE_CHECKOUT);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}
