package com.wakefern.api.mi9.v7.account.authentication;

import com.wakefern.global.*;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;


@Path(MWGApplicationConstants.Requests.Authentication.prefix)
public class AuthenticateUI extends BaseService {

	private final static Logger logger = Logger.getLogger(AuthenticateUI.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public AuthenticateUI() {
        this.requestPath = MWGApplicationConstants.Requests.Authentication.prefix + MWGApplicationConstants.Requests.Authentication.authorize;
    }
	
    @POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Authentication.authorize)
    public Response getResponse(
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonBody) {
    	    	
        try {
        	String jsonResp = makeRequest();
            return this.createValidResponse(jsonResp);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.AUTHENTICATION_AUTHENTICATE_UI);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }

    	/**
    	 * Triggers the Auth Request for the Session Token.<br>
    	 * For use internally.<br>  
    	 * This is not a REST endpoint.<br>
    	 * 
    	 * @return
    	 * @throws Exception
    	 * @throws IOException
    	 */
    public String getInfo() throws Exception {
    		return makeRequest();
    }
    
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

    /**
     * Make the Auth Request to MWG for a Session Token.<br>
     * The Session Token must be sent with all subsequent requests to MWG.
     * 
     * @return
     * @throws Exception
     * @throws IOException
     */
    private String makeRequest() throws Exception {
        this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, MWGApplicationConstants.getAppToken());

        // The purpose of this request is to simply retrieve a valid Session Token & Guest User ID, from MWG.
        // Any actual data sent with the request, will just be ignored.
        // Once this request succeeds, The UI will have access to a valid Session Token and Guest User ID.
        // The Session Token is required for all subsequent requests to MWG.  If it's omitted, the request will be rejected as Unauthorized.
        // The Guest User ID is valid for all operations available to non-registered users.
        // To get a valid Registered User ID, users must authenticate via the "AuthenticateUser" endpoint.
        return this.mwgRequest(BaseService.ReqType.POST, "{}", "com.wakefern.account.authentication.AuthenticateUI");
    }
}
