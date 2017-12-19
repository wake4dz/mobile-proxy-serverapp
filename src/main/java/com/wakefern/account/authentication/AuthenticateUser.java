package com.wakefern.account.authentication;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(MWGApplicationConstants.Requests.Account.acctPath)
public class AuthenticateUser extends BaseService {
	
	private final static Logger logger = Logger.getLogger("AuthorizationAuthenticate");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
    public AuthenticateUser() {
        this.requestPath = MWGApplicationConstants.Requests.Account.acctPath + MWGApplicationConstants.Requests.Account.login;
    } 

    @PUT
    @Consumes(MWGApplicationConstants.Headers.Account.login)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Account.login)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.chainID) String chainId, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, 
    		String jsonBody) {    
    		
    		String emailKey  = "Email";
    		String pwKey     = "Password";
    		String appVerKey = "AppVersion";
    		String appVerErr = "501, Please update the app to the latest version.";

    		// Here in Wakefern API Land, we don't care if the Session Token supplied by the UI is valid or not.
    		// Simply send it to MWG and let them figure it out.
    		// MWG's response will be passed along to the UI, which will deal with it accordingly.
    		this.requestToken = sessionToken;
        
        JSONObject jsonData = new JSONObject(jsonBody);
        
        String userEmail = (jsonData.has(emailKey)) ? jsonData.getString(emailKey) : ""; 
        String password  = (jsonData.has(pwKey))    ? jsonData.getString(pwKey)    : "";
        
        int appVer = (jsonData.has(appVerKey)) ? Integer.parseInt(jsonData.getString(appVerKey).split("\\.")[0]) : 0;
        
        // MWG does not care about App Version, so remove it before generating the request being sent to MWG.
        if (jsonData.has(appVerKey)) {
        		jsonData.remove(appVerKey);
        }
        
        // Reject all versions that are less than 2.0.0. Session cop fix.
        if (appVer < 2) {
    			return this.createErrorResponse(new Exception(appVerErr));
        
        } else {
            this.requestHeader = new MWGHeader(ApplicationConstants.jsonAcceptType, MWGApplicationConstants.Headers.Account.login, sessionToken);
    			this.requestParams = new HashMap<String, String>();    			
    			
    			this.requestParams.put(MWGApplicationConstants.chainID, chainId);
    	        
    			jsonData.put(pwKey, StringEscapeUtils.escapeHtml4(password));
	        
	        try {
	        		String responseJSON = this.mwgRequest(BaseService.ReqType.PUT, jsonData.toString());
	        		return this.createValidResponse(responseJSON);
	        		
	        } catch (Exception e) {
	        		logger.log(Level.SEVERE, "[getInfo]::Exception authenticate user: {0}, msg: {1}", new Object[]{userEmail, e.toString()});
	            return this.createErrorResponse(e);
	        }
        }
    }   
}
