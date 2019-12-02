package com.wakefern.account.authentication;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class AuthenticateUser extends BaseService {

	private final static Logger logger = Logger.getLogger(AuthenticateUser.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
    public AuthenticateUser() {
        this.requestPath = MWGApplicationConstants.Requests.Account.prefix + MWGApplicationConstants.Requests.Account.login;
    } 

    @PUT
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Account.login)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainId, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonBody) {    
    		
    	long startTime, endTime, actualTime;
    	
		String emailKey  = "Email";
		String pwKey     = "Password";
		String appVerKey = "AppVersion";
		String appVerErr = "501, Please update the app to the latest version.";

		// Here in Wakefern API Land, we don't care if the Session Token supplied by the UI is valid or not.
		// Simply send it to MWG and let them figure it out.
		// MWG's response will be passed along to the UI, which will deal with it accordingly.
		this.requestToken = sessionToken;
        
        JSONObject jsonData = new JSONObject(jsonBody);
        
        String userEmail = jsonData.has(emailKey) ? jsonData.getString(emailKey) : "";
        String password  = jsonData.has(pwKey)    ? jsonData.getString(pwKey)    : "";
        
        int appVer = (jsonData.has(appVerKey)) ? Integer.parseInt(jsonData.getString(appVerKey).split("\\.")[0]) : 0;
        
        // MWG does not care about App Version, so remove it before generating the request being sent to MWG.
        if (jsonData.has(appVerKey)) {
        		jsonData.remove(appVerKey);
        }
        
        // Reject all versions that are less than 2.0.0. Session cop fix.
        if (appVer < 3) {
    			return this.createErrorResponse(new Exception(appVerErr));
        
        } else {
        	this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Account.login, sessionToken);
			this.requestParams = new HashMap<>();
			
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainId);
	        
			jsonData.put(pwKey, password);//StringEscapeUtils.escapeHtml4(password));
	        
	        try {
	        	startTime = System.currentTimeMillis();
        		String responseJSON = this.mwgRequest(BaseService.ReqType.PUT, jsonData.toString(), "com.wakefern.account.authentication.AuthenticateUser");
                // for warning any API call time exceeding its own upper limited time defined in mwgApiWarnTime.java
    			endTime = System.currentTimeMillis();
    			actualTime = endTime - startTime;
    			if (actualTime > MwgApiWarnTime.AUTHENTICATION_AUTHENTICATE_USER.getWarnTime()) {
    				logger.warn("com.wakefern.authentication.AuthenticateUser::getResponse() - The API call took "
							+ actualTime + " ms to process the request, the warn time is " +
    						MwgApiWarnTime.AUTHENTICATION_AUTHENTICATE_USER.getWarnTime() + " ms.");
    			}
        		logger.info("com.wakefern.authentication.AuthenticateUser::getResponse() - " + responseJSON);
        		return this.createValidResponse(responseJSON);
	        		
	        } catch (Exception e) {
  
	        	LogUtil.addErrorMaps(e, MwgErrorType.AUTHENTICATION_AUTHENTICATE_USER);
	        	
	        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "userEmail", userEmail, 
	        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
	        	
	    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
	    		
	            return this.createErrorResponse(errorData, e);
	        }
        }
    }   
}
