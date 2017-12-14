package com.wakefern.global;

import com.wakefern.global.ErrorHandling.ExceptionHandler;

import javax.ws.rs.core.Response;

/**
 * Created by zacpuste on 8/12/16.
 */
public class BaseService {
    public Object requestHeader = null;
    public String requestPath = null;
    public String token = null;
    
    public Response createErrorResponse(Exception e){
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        try {
            String[] array = e.getMessage().split(",");

            String buildError;
            if(e.getMessage().contains("400")){
                return Response.status(400).entity(exceptionHandler.exceptionMessageJson(e)).build();
            }
            if(Integer.parseInt(array[0]) == 401 || Integer.parseInt(array[0]) == 403){
                buildError = ApplicationConstants.Requests.buildErrorJsonOpen + ApplicationConstants.Requests.forbiddenError
                        + ApplicationConstants.Requests.buildErrorJsonClose;
            } else {
                 buildError = ApplicationConstants.Requests.buildErrorJsonOpen + array[1]
                        + ApplicationConstants.Requests.buildErrorJsonClose;
            }

            return Response.status(Integer.parseInt(array[0])).entity(buildError).build();
        } catch (Exception stringError) {
            return Response.status(500).entity(exceptionHandler.exceptionMessageJson(e)).build();
        }
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

    public Response createResponse(int status){
        return Response.status(status).build();
    }
}
