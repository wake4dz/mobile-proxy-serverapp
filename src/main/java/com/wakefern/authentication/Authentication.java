package com.wakefern.authentication;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGBody;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.*;
import com.wakefern.request.models.Header;

import javax.ws.rs.*;

import java.io.IOException;
import java.util.Map;

@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class Authentication {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public String getInfo(String jsonBody) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();
        myJSONObj.put("message", jsonBody);
        
        //Authorization
        String path = (ApplicationConstants.Requests.baseURLV5 + ApplicationConstants.Requests.Authentication.Authenticate);
        Map<String, String> headerMap = new MWGHeader().authHeader();
        String body = new MWGBody().authBody(jsonBody);
        
        myJSONObj.put("HTTPRequest", HTTPRequest.executePost("", path, "", body, headerMap));
        
        return myJSONObj.toString();
    }
}
