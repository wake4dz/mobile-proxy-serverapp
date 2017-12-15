package com.wakefern.account.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

@Path(MWGApplicationConstants.Requests.Authentication.authenticate)
public class AuthenticateUI extends BaseService {

	private final static Logger logger = Logger.getLogger("Authentication");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public AuthenticateUI() {
        this.requestPath = MWGApplicationConstants.Requests.Authentication.authenticate;
    }
	
    @POST
    @Consumes(ApplicationConstants.jsonAcceptType)
    @Produces("application/*")
    public Response getInfoResponse(String jsonBody) throws Exception, IOException {
    	
    		// The UI can send an optional boolean config setting called "useStaging".
    		// If "useStaging" is present and TRUE, set the Base URL to the Staging URL.
    		// Otherwise, use the Production URL.
    		JSONObject postDataJSON = new JSONObject(jsonBody);
    		boolean    useStaging   = (postDataJSON.has("useStaging")) ? postDataJSON.getBoolean("useStaging") : false;
    		
    		// TODO: change to 'useStaging' before releasing!
    		MWGApplicationConstants.baseURL = (true) ? MWGApplicationConstants.fgStageBaseURL : MWGApplicationConstants.fgProdBaseURL;
    	
        try {
        		String jsonResp = makeRequest();
        		System.out.println("com.wakefern.authentication.Authentication::getInfoResponse() - " + jsonResp);
            return this.createValidResponse(jsonResp);
        
        } catch (Exception e) {
        		logger.log(Level.SEVERE, "[getInfoResponse]::Exception getInfoResponse ", e);
            return this.createErrorResponse(e);
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
    public String getInfo() throws Exception, IOException {
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
    private String makeRequest() throws Exception, IOException {
        this.requestHeader = new MWGHeader(ApplicationConstants.jsonAcceptType, ApplicationConstants.jsonResponseType, MWGApplicationConstants.appToken);

        // The purpose of this request is to simply retrieve a valid Session Token & Guest User ID, from MWG.
        // Any actual data sent with the request, will just be ignored.
        // Once this request succeeds, The UI will have access to a valid Session Token and Guest User ID.
        // The Session Token is required for all subsequent requests to MWG.  If it's omitted, the request will be rejected as Unauthorized.
        // The Guest User ID is valid for all operations available to non-registered users.
        // To get a valid Registered User ID, users must authenticate via the "AuthenticateUser" endpoint.
        return this.makePostRequest("");
    }
}
