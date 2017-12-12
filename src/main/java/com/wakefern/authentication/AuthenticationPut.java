package com.wakefern.authentication;

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
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by Loi Cao on 10/04/17.
 */
@Path(MWGApplicationConstants.Requests.Authentication.authenticate)
public class AuthenticationPut extends BaseService {
	
	private final static Logger logger = Logger.getLogger("AuthenticationPut");
	
    @PUT
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{sessionToken}/authenticate")
    public Response getInfo(@PathParam("sessionToken") String sessionToken, @HeaderParam("Authorization") String authToken, String jsonBody){
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.authenticationToken;
        }else{
        	this.token = authToken;
        }
        //this.path = ApplicationConstants.Requests.Authentication.Authenticate + ApplicationConstants.StringConstants.authenticate;
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.shoprite.com/api/authorization/v5/authorization/");
        sb.append(sessionToken);	sb.append("/authenticate");
        this.path = sb.toString();//"https://api.shoprite.com/api/authorization/v5/authorization/authenticate";
        System.out.println("this.path: "+this.path);
        
        try {
            JSONObject messageJson = new JSONObject(jsonBody);
            //Test to see if there is user data, if not get thrown to guest auth
            String userEmail =  String.valueOf(messageJson.get("Email"));
            String password = String.valueOf(messageJson.get("Password"));
            String escapeCharPass = StringEscapeUtils.escapeHtml4(password);
            messageJson.put("Password", escapeCharPass);
            ServiceMappings mapping = new ServiceMappings();
            jsonBody = messageJson.toString();
            mapping.setPutMapping(this, jsonBody, null);
            String json;
            
            try {
//                json = (HTTPRequest.executePostJSON(this.path, jsonBody, mapping.getgenericHeader(), 0));
                json = (HTTPRequest.executePut("", this.path, "", mapping.getGenericBody(), mapping.getgenericHeader(), 0));
                System.out.println("json: "+json);
                return this.createValidResponse(json);
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

//            FormattedAuthentication formattedAuthentication = new FormattedAuthentication();
//
//            try {
//                return Response.status(200).entity(formattedAuthentication.formatAuth(json, messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
//                        ApplicationConstants.FormattedAuthentication.ChainId, ApplicationConstants.FormattedAuthentication.AuthPlanning).toString()).build();//,
////                        v5).toString()).build();
//            } catch (Exception e) {
//            	logger.log(Level.SEVERE, "[getInfo]::Exception occurred getting auth response ", e);
//                return this.createErrorResponse(e);
//            }
        } catch (Exception ex){
            //Invalid JSON return guest credentials
            try {
                Authentication authentication = new Authentication();
                return this.createValidResponse(authentication.getInfo());//{} is the jsonbody that triggers a guest authtoken
            } catch (Exception e){
            	logger.log(Level.SEVERE, "[getInfo]::Exception occurred on authentication ", e);
                return this.createErrorResponse(e);
            }
        }
    }

    public AuthenticationPut() {
        this.serviceType = new MWGHeader();
    }
}
