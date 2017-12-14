package com.wakefern.account.users;

import java.util.Map;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

@Path(MWGApplicationConstants.Requests.Account.acctPath)
public class ProfileGet extends BaseService {

	private final static Logger logger = Logger.getLogger("ProfileGet");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
    public ProfileGet() {
        this.requestPath = MWGApplicationConstants.Requests.Account.acctPath + MWGApplicationConstants.Requests.Account.userProfilePath;
    } 

    @GET
    @Consumes(MWGApplicationConstants.Headers.Account.fullProfile)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Account.userProfilePath)
    public Response getFullProfile(
    		@PathParam(MWGApplicationConstants.chainID) String chainID, 
    		@PathParam(MWGApplicationConstants.userID) String userID, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, 
    		String jsonBody) {
			
    		try {
    			String jsonResponse = makeRequest(sessionToken, MWGApplicationConstants.Headers.Account.fullProfile, chainID, userID);
    			return this.createValidResponse(jsonResponse);
    		} catch (Exception e) {
    			logger.log(Level.SEVERE, "[getFullProfile]::Exception - Get user profile.  Message: {1}", new Object[]{e.toString()});
            return this.createErrorResponse(e);
    		} 
    }
    
    @GET
    @Consumes(MWGApplicationConstants.Headers.Account.basicProfile)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Account.userProfilePath)
    public Response getBasicProfile(
    		@PathParam(MWGApplicationConstants.chainID) String chainID, 
    		@PathParam(MWGApplicationConstants.userID) String userID, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, 
    		String jsonBody) {
			
		try {
			String jsonResponse = makeRequest(sessionToken, MWGApplicationConstants.Headers.Account.basicProfile, chainID, userID);
			return this.createValidResponse(jsonResponse);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "[getBasicProfile]::Exception - Get user profile.  Message: {1}", new Object[]{e.toString()});
			return this.createErrorResponse(e);
		}     	    	
    }
    
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

    private String makeRequest(String sessionToken, String contentType, String chainID, String userID) throws Exception, IOException {
    		this.token = sessionToken;
        this.requestHeader = new MWGHeader(ApplicationConstants.jsonAcceptType, contentType, sessionToken);
    	
        ServiceMappings mapping = new ServiceMappings();
        HashMap<String, String> reqParams = new HashMap<String, String>();
        
        reqParams.put(MWGApplicationConstants.chainID, chainID);
        reqParams.put(MWGApplicationConstants.userID, userID);
        
        mapping.setGetMapping(this, reqParams);
        
        String reqURL = mapping.getPath();
        Map<String, String> reqHead = mapping.getgenericHeader();
        
        return HTTPRequest.executeGetJSON(reqURL, reqHead, 0);
    }
}
