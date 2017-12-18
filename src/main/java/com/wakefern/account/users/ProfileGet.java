package com.wakefern.account.users;

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

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

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
        
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

    private String makeRequest(String sessionToken, String acceptType, String chainID, String userID) throws Exception, IOException {
    		this.requestParams = new HashMap<String, String>();
        this.requestHeader = new MWGHeader(acceptType, ApplicationConstants.jsonResponseType, sessionToken);
    	        
        this.requestParams.put(MWGApplicationConstants.chainID, chainID);
        this.requestParams.put(MWGApplicationConstants.userID, userID);
        
        return this.mwgRequest(BaseService.ReqType.GET, null);
    }
}
