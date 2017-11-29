package com.wakefern.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.FormattedAuthentication;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 10/5/16.
 */
@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class AuthorizationAuthenticate extends BaseService {
	
	private final static Logger logger = Logger.getLogger("AuthorizationAuthenticate");
	private static final String Email = "Email";
	private static final String Password = "Password";
	private static final String AppVersion = "AppVersion";
	private static final String appVersionErr = "501,Please update ShopRite to the latest version.";
	
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/authenticate")
    public Response getInfo(@HeaderParam("Authorization") String authToken, String jsonBody){
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.authenticationToken;
        }else{
        	this.token = authToken;
        }
        //this.path = ApplicationConstants.Requests.Authentication.Authenticate + ApplicationConstants.StringConstants.authenticate;
        this.path = "https://api.shoprite.com/api/authorization/v5/authorization/authenticate";
        
        try {
            JSONObject messageJson = new JSONObject(jsonBody);
            //Test to see if there is user data, if not get thrown to guest auth
            StringEscapeUtils seu = new StringEscapeUtils();
            String userEmail =  String.valueOf(messageJson.get(Email));
            String password = String.valueOf(messageJson.get(Password));
            try{
            	// reject all versions that are less than 2.0.0, session cop fix.
                int appVer = Integer.parseInt(String.valueOf(messageJson.get(AppVersion)).split("\\.")[0]);
	            if(appVer < 2){
	            	return this.createErrorResponse(new Exception(appVersionErr));
	            }
            } catch(Exception e){
            	return this.createErrorResponse(new Exception(appVersionErr));
            	
            }
            String escapeCharPass = seu.escapeHtml4(password);
            messageJson.put(Password, escapeCharPass);
            ServiceMappings mapping = new ServiceMappings();
            jsonBody = messageJson.toString();
            mapping.setPutMapping(this, jsonBody);
            String json;
            
            try {
                json = (HTTPRequest.executePostJSON(this.path, jsonBody, mapping.getgenericHeader(), 0));
            } catch (Exception e) {
            	logger.log(Level.SEVERE, "[getInfo]::Exception authenticate user: {0}, msg: {1}", new Object[]{userEmail, e.toString()});
                return this.createErrorResponse(e);
            }

            //run regular v5 authentication
//            String v5;
//            try {
//                Authentication authentication = new Authentication();
//                v5 = authentication.getInfo(jsonBody);
//            } catch (Exception e) {
//            	logger.log(Level.SEVERE, "[getInfo]::Exception authenticate in v5 ", e.toString());
//                return this.createErrorResponse(e);
//            }

            FormattedAuthentication formattedAuthentication = new FormattedAuthentication();

            try {
                return Response.status(200).entity(formattedAuthentication.formatAuth(json, messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
                        ApplicationConstants.FormattedAuthentication.ChainId, ApplicationConstants.FormattedAuthentication.AuthPlanning).toString()).build();//,
//                        v5).toString()).build();
            } catch (Exception e) {
            	logger.log(Level.SEVERE, "[getInfo]::Exception occurred getting auth response ", e);
                return this.createErrorResponse(e);
            }
        } catch (Exception ex){
            //Invalid JSON return guest credentials
            try {
                Authentication authentication = new Authentication();
                return this.createValidResponse(authentication.getInfo("{}"));//{} is the jsonbody that triggers a guest authtoken
            } catch (Exception e){
            	logger.log(Level.SEVERE, "[getInfo]::Exception occurred on authentication ", e);
                return this.createErrorResponse(e);
            }
        }
    }

    public AuthorizationAuthenticate() {
        this.serviceType = new MWGHeader();
    }
}
