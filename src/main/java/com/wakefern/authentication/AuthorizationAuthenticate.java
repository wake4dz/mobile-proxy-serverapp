package com.wakefern.authentication;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.FormattedAuthentication;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Created by zacpuste on 10/5/16.
 */
@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class AuthorizationAuthenticate extends BaseService {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/authenticate")
    public Response getInfo(@HeaderParam("Authorization") String authToken, String jsonBody){
        if(this.token.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.authenticationToken;
        }

        this.token = authToken;
        //this.path = ApplicationConstants.Requests.Authentication.Authenticate + ApplicationConstants.StringConstants.authenticate;
        this.path = "https://api.shoprite.com/api/authorization/v5/authorization/authenticate";

        try {
            JSONObject messageJson = new JSONObject(jsonBody);
            //Test to see if there is user data, if not get thrown to guest auth
            messageJson.get("Email");
            ServiceMappings mapping = new ServiceMappings();
            mapping.setPutMapping(this, jsonBody);
            String json;
            try {
                json = (HTTPRequest.executePostJSON(this.path, jsonBody, mapping.getgenericHeader(), 0));
            } catch (Exception e) {
                return this.createErrorResponse(e);
            }
            //run regular v5 authentication
            String v5;
            try {
                Authentication authentication = new Authentication();
                v5 = authentication.getInfo(jsonBody);
            } catch (Exception e) {
                return this.createErrorResponse(e);
            }

            FormattedAuthentication formattedAuthentication = new FormattedAuthentication();
            try {
                return Response.status(200).entity(formattedAuthentication.formatAuth(json, messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
                        ApplicationConstants.FormattedAuthentication.ChainId, ApplicationConstants.FormattedAuthentication.AuthPlanning,
                        v5).toString()).build();
            } catch (Exception e) {
                return this.createErrorResponse(e);
            }
        } catch (Exception ex){
            //Invalid JSON return guest credentials
            try {
                Authentication authentication = new Authentication();
                return this.createValidResponse(authentication.getInfo("{}"));//{} is the jsonbody that triggers a guest authtoken
            } catch (Exception e){
                return this.createErrorResponse(e);
            }
        }
    }

    public AuthorizationAuthenticate() {
        this.serviceType = new MWGHeader();
    }
}
