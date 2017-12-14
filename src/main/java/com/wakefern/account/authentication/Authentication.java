package com.wakefern.account.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.*;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

@Path(MWGApplicationConstants.Requests.Authentication.authenticate)
public class Authentication extends BaseService {

	private final static Logger logger = Logger.getLogger("Authentication");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Authentication() {
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
    	 * Triggers the Auth Request for the Session Token, but does not format the response.<br>
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
     * Make the Auth Request to MWG for a Session Token.
     * 
     * @return
     * @throws Exception
     * @throws IOException
     */
    private String makeRequest() throws Exception, IOException {
        this.requestHeader = new MWGHeader(ApplicationConstants.jsonAcceptType, ApplicationConstants.jsonResponseType, MWGApplicationConstants.appToken);

        // The purpose of this request is to simply retrieve a valid Session Token from MWG.
        // As such, just send "{}" as POST data.  
    		// Any actual data sent, would just be ignored anyway.
        
    		ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, "{}", null); 
        
        String reqURL = mapping.getPath();
        String reqData = mapping.getGenericBody();
        Map<String, String> reqHead = mapping.getgenericHeader();
        
        return HTTPRequest.executePost("", reqURL, "", reqData, reqHead, 0);
    }
}
