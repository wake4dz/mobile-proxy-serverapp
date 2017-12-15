package com.wakefern.global;

import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.request.HTTPRequest;
import com.wakefern.request.models.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

/**
 * Created by zacpuste on 8/12/16.
 */
public class BaseService {
    protected HashMap<String, String> requestParams = null;
    
    protected Header requestHeader = null;
    protected String requestPath   = null;
    protected String requestToken  = null;
    
    protected String makeGetRequest() throws Exception, IOException {
    		ServiceMappings mapping = new ServiceMappings();
        
    		if ((requestToken == null) || (requestPath == null) || (requestHeader == null)) {
    			throw new Exception("Unable to execute GET request.  Missing required data.");
    		
    		} else {
    			mapping.setGetMapping(this, requestParams);
        
    			String reqURL = mapping.getPath();
    			Map<String, String> genericHeader = mapping.getgenericHeader();
        
    			return HTTPRequest.executeGetJSON(reqURL, genericHeader, 0);
    		}
    }
    
    /**
     * Create a standardized Error Response (HTTP 5xx / 4xx) to pass back to the UI.
     * 
     * @param e
     * @return
     */
    protected Response createErrorResponse(Exception e) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        
        try {
            String[] array = e.getMessage().split(",");

            String buildError;
            
            if (e.getMessage().contains("400")) {
                return Response.status(400).entity(exceptionHandler.exceptionMessageJson(e)).build();
            }
            
            if (Integer.parseInt(array[0]) == 401 || Integer.parseInt(array[0]) == 403) {
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
    
    /**
     * Create Valid (HTTP 200) Response.
     * 
     * @param jsonResponse
     * @return
     */
    protected Response createValidResponse(String jsonResponse){
    		return Response.status(200).entity(jsonResponse).build();
    }
        
    /**
     * Create a generic Response Object, using whatever status is supplied.
     * 
     * @param status
     * @return
     */
    protected Response createResponse(int status) {
        return Response.status(status).build();
    }
}
