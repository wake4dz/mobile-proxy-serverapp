package com.wakefern.authentication;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;
import com.wakefern.request.*;

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

    public void buildMWGRequest(String jsonBody, Header header) throws Exception{
    	Body body = new Body();
        String modifiedJsonBody = body.buildBody(jsonBody);
    	
    	if(header != null){

            if(true){//check for application/json
                String newHeader = new Header().getInfo();
            } else {

            }
        }
    	
    }
}
