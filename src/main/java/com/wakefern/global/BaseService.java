package com.wakefern.global;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.ErrorHandling.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

/**
 * Created by zacpuste on 8/12/16.
 */
public class BaseService {
    public Object serviceType = null;
    public String path = null;
    //public HttpServletRequest request = null;
    public String token = null;
    
    public Response createErrorResponse(Exception e){
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        return Response.status(500).entity(exceptionHandler.exceptionMessageJson(e)).build();
    }
    
    public Response createValidResponse(String jsonResponse){
    	return Response.status(200).entity(jsonResponse).build();
    }
    
    public Response createValidDelete(){
    	return Response.status(200).build();
    }
    
    public Response createDefaultResponse(){
    	return Response.status(500).build();
    }
}
