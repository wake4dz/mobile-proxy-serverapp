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
        myJSONObj.put("message", jsonBody);
        String path = "https://api.shoprite.com/api" + ApplicationConfig.Requests.Authentication.Authenticate;
        myJSONObj.put("Request", Request.executePost(path, jsonBody));
        
        return myJSONObj.toString();
    }

//    public String buildMWGRequest(String jsonBody, String header) throws Exception{
//    	Body body = new Body();
//        String modifiedJsonBody = body.buildBody(jsonBody);
//    	String modifiedHeader = null;
//		if (header != null) {
//			if (true) {//TODO check for application/json
//				modifiedHeader = new Header().getInfo();
//			} else {
//				// Second header option
//				modifiedHeader = header.getInfo();
//			}
//		}
//		//return response
//    	System.console().printf(jsonBody);
//		return Request.executePost(ApplicationConfig.Requests.Authentication.Authenticate, jsonBody);	
//    }
}
