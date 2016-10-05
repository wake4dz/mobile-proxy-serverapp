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
import java.io.IOException;

/**
 * Created by zacpuste on 10/5/16.
 */
@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class AuthorizationAuthenticate extends BaseService {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/authenticate")
    public String getInfo(@HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Authentication.Authenticate + ApplicationConstants.StringConstants.authenticate;

        JSONObject messageJson = new JSONObject(jsonBody);
        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody);

        String json;
        try {
            json = (HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), mapping.getgenericHeader()));
        } catch (Exception e){
            ExceptionHandler exceptionHandler = new ExceptionHandler();
            return exceptionHandler.ExceptionMessage(e);
        }

        //run regular v5 authentication
        String v5;
        try {
            Authentication authentication = new Authentication();
            v5 = authentication.getInfo(jsonBody);
        } catch (Exception e){
            ExceptionHandler exceptionHandler = new ExceptionHandler();
            return exceptionHandler.ExceptionMessage(e);
        }


        FormattedAuthentication formattedAuthentication = new FormattedAuthentication();
        return formattedAuthentication.formatAuth(json, messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
                ApplicationConstants.FormattedAuthentication.ChainId, ApplicationConstants.FormattedAuthentication.AuthPlanning,
                v5).toString();
    }

    public AuthorizationAuthenticate(){
        this.serviceType = new MWGHeader();
    }
}
