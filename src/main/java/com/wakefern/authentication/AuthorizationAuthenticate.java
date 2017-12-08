package com.wakefern.authentication;

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
	private static final String appVersionErr = "501,Please update ShopRite to the latest version.";
	
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Account.authPath)
    public Response getInfo(@PathParam("chainId") String chainId, @HeaderParam("Authorization") String authToken, String jsonBody) 
    {    
    		if (authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)) {
            this.token = ApplicationConstants.Requests.Tokens.authenticationToken;
        } else {
        		this.token = authToken;
        }
        
        this.path = MWGApplicationConstants.baseURL + MWGApplicationConstants.Requests.Account.acctPath + MWGApplicationConstants.Requests.Account.authPath;
        
        try {
        		JSONObject messageJson = new JSONObject(jsonBody);
            
            // Test to see if there is user data, if not get thrown to guest auth
            String userEmail =  String.valueOf(messageJson.get(Email));
            String password = String.valueOf(messageJson.get(Password));
            
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

            messageJson.put(Password, escapeCharPass);
            jsonBody = messageJson.toString();
            mapping.setPutMapping(this, jsonBody);
            
            try {
                json = (HTTPRequest.executePostJSON(this.path, jsonBody, mapping.getgenericHeader(), 0));
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
            // Invalid JSON return guest credentials
            try {
                Authentication authentication = new Authentication();
                return this.createValidResponse(authentication.getInfo("{}")); // {} is the jsonbody that triggers a guest authtoken
            
            } catch (Exception e) {
            		logger.log(Level.SEVERE, "[getInfo]::Exception occurred on authentication ", e);
                return this.createErrorResponse(e);
            }
        }
    }

    public AuthorizationAuthenticate() 
    {
        this.serviceType = new MWGHeader();
    }
}
