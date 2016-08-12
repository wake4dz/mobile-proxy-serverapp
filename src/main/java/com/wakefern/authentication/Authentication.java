package com.wakefern.authentication;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;
import com.wakefern.request.*;

import javax.ws.rs.*;

import java.io.IOException;

@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class Authentication {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public String getInfo(String jsonBody) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();
        myJSONObj.put("message", jsonBody);
        String path = "https://api.shoprite.com/api" + ApplicationConstants.Requests.Authentication.Authenticate;
        myJSONObj.put("HTTPRequest", HTTPRequest.executePost("", path, "", jsonBody,""));
        
        return myJSONObj.toString();
    }
}
