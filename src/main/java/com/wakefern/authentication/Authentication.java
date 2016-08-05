package com.wakefern.authentication;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;

import javax.ws.rs.*;
import java.io.IOException;

@Path(ApplicationConfig.Requests.Authentication.Authenticate)
public class Authentication {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public String getInfo(String jsonBody) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();
        myJSONObj.put("message", "Hello World!");
        return myJSONObj.toString();
    }
}
