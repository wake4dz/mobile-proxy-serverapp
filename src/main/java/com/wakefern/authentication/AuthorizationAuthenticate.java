package com.wakefern.authentication;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.FormattedAuthentication;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 10/5/16.
 */
@Path(MWGApplicationConstants.Requests.Account.acctPath)
public class AuthorizationAuthenticate extends BaseService {
	
	private final static Logger logger = Logger.getLogger("AuthorizationAuthenticate");
	private static final String Email = "Email";
	private static final String Password = "Password";
	private static final String AppVersion = "AppVersion";
	private static final String appVersionErr = "501, Please update ShopRite to the latest version.";
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Account.authPath)
    public Response getInfo(@PathParam("chainId") String chainId, @HeaderParam("Authorization") String authToken, String jsonBody) {    
    		boolean isAppToken = false;
    		
    		if (authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)) {
            this.token = MWGApplicationConstants.appToken;
            isAppToken = true;
        } else {
        		this.token = authToken;
        }
        
        this.path = MWGApplicationConstants.Requests.Account.acctPath + MWGApplicationConstants.Requests.Account.authPath;
        
        try {
        		JSONObject messageJson = new JSONObject(jsonBody);
            
            // Test to see if there is user data. If not, get Thrown to guest authorization
            String userEmail = String.valueOf(messageJson.get(Email));
            String password = String.valueOf(messageJson.get(Password));
            
            if ((userEmail.length() == 0) && (password.length() == 0)) {
            		// UI sent empty Username & Password fields. Default to Guest User status.
            		throw new Exception();
            }
            
            // If we got user credentials, but do not have a MWG Session Token, we need to request one before the user can authenticate.
            if (isAppToken) {
                try {
                    Authentication authentication = new Authentication();
                    
                    String response = authentication.getInfo("{}");
                    JSONObject respJSON = new JSONObject(response);
                    
                    // The value returned by MWG as the "Token" is the Session Token required for all subsequent calls.
                    // With the exception of the initial Auth request to get the Session Token in the first place,
                    // any requests lacking the Session Token will be rejected by MWG.
                    this.token = respJSON.getString("Token");
                    
                } catch (Exception e) {
                		logger.log(Level.SEVERE, "[getInfo]::Exception occurred on authentication ", e);
                    return this.createErrorResponse(e);
                }
            }
            
            try {
            		// Reject all versions that are less than 2.0.0, session cop fix.
                int appVer = Integer.parseInt(String.valueOf(messageJson.get(AppVersion)).split("\\.")[0]);
	            
                if (appVer < 2) {
	            		return this.createErrorResponse(new Exception(appVersionErr));
	            }
                
            } catch(Exception e) {
            		return this.createErrorResponse(new Exception(appVersionErr));
            }
            
            String json;
            ServiceMappings mapping = new ServiceMappings();
            String escapeCharPass = StringEscapeUtils.escapeHtml4(password);
            HashMap<String, String> reqParams = new HashMap<String, String>();

            reqParams.put(MWGApplicationConstants.chainID, chainId);
            messageJson.put(Password, escapeCharPass);
            jsonBody = messageJson.toString();
            mapping.setPutMapping(this, jsonBody, reqParams);
            
            String reqURL = mapping.getPath();
            String reqData = mapping.getGenericBody();
            Map<String, String> reqHead = mapping.getgenericHeader();
            
            try {
                json = HTTPRequest.executePostJSON(reqURL, reqData, reqHead, 0);
            } catch (Exception e) {
            		logger.log(Level.SEVERE, "[getInfo]::Exception authenticate user: {0}, msg: {1}", new Object[]{userEmail, e.toString()});
                return this.createErrorResponse(e);
            }

            FormattedAuthentication formattedAuthentication = new FormattedAuthentication();

            try {
                return Response.status(200).entity(
                		formattedAuthentication.formatAuth(
                				json, 
                				messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
                				ApplicationConstants.FormattedAuthentication.ChainId, 
                				ApplicationConstants.FormattedAuthentication.AuthPlanning
        				).toString()
            		).build();
            
            } catch (Exception e) {
            		logger.log(Level.SEVERE, "[getInfo]::Exception occurred getting auth response ", e);
                return this.createErrorResponse(e);
            }
        
        } catch (Exception ex) {
            // Return guest credentials
            try {
                Authentication authentication = new Authentication();
                return this.createValidResponse(authentication.getInfo("{}")); // {} is the jsonbody that triggers a guest authtoken
            
            } catch (Exception e) {
            		logger.log(Level.SEVERE, "[getInfo]::Exception occurred on authentication ", e);
                return this.createErrorResponse(e);
            }
        }
    }

    public AuthorizationAuthenticate() {
        this.serviceType = new MWGHeader();
    }    
}
