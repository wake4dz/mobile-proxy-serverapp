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
    
    protected static enum ReqType { GET, POST, PUT, DELETE };
    
//    protected String mwgRequest(ReqType reqType, String reqData) {
//    		String reqTypeStr;
//    		
//    		switch (reqType) {
//    			case GET: reqTypeStr = "GET"; break;
//    			case POST: reqTypeStr = "POST"; break;
//    			case PUT: reqTypeStr = "PUT"; break;
//    			case DELETE: reqTypeStr = "DELETE"; break;
//    		}
//    		
//    		
//    }
    
    /**
     * Trigger a GET request to MyWebGrocer.
     * 
     * @return
     * @throws Exception
     * @throws IOException
     */
    protected String makeGetRequest() throws Exception, IOException {
        
    		if ((requestPath == null) || (requestHeader == null)) {
    			throw new Exception("Unable to execute GET request.  Missing required data.");
    		
    		} else {
    			ServiceMappings sm = getServiceMapping(ReqType.GET, null);
    			
    			String reqURL = sm.getPath();
    			Map<String, String> genericHeader = sm.getgenericHeader();
        
    			return HTTPRequest.executeGetJSON(reqURL, genericHeader, 0);
    		}
    }
    
    /**
     * Trigger a POST request to MyWebGrocer.
     * 
     * @param data
     * @return
     * @throws Exception
     * @throws IOException
     */
    protected String makePostRequest(String data) throws Exception, IOException {
        
    		if ((requestPath == null) || (requestHeader == null)) {
    			throw new Exception("Unable to execute POST request.  Missing required data.");
    		
    		} else {
    			ServiceMappings sm = getServiceMapping(ReqType.POST, data);
            
            String reqURL = sm.getPath();
            String reqData = sm.getGenericBody();
            Map<String, String> reqHead = sm.getgenericHeader();
            
            return HTTPRequest.executePost(reqURL, reqData, reqHead);
    		}
    }
    
    /**
     * Trigger a PUT request to MyWebGrocer.
     * 
     * @param data
     * @return
     * @throws Exception
     * @throws IOException
     */
    protected String makePutRequest(String data) throws Exception, IOException {
    	
		if ((requestPath == null) || (requestHeader == null)) {
			throw new Exception("Unable to execute PUT request.  Missing required data.");
		
		} else {
	    		ServiceMappings sm = getServiceMapping(ReqType.PUT, data);
	        
	        String reqURL = sm.getPath();
	        String reqData = sm.getGenericBody();
	        Map<String, String> reqHead = sm.getgenericHeader();
	        
	        return HTTPRequest.executePut("", reqURL, "", reqData, reqHead, 0);
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
    
    /**
     * Returns the ServiceMappings Object required to construct a request.
     * 
     * @param data
     * @return
     */
    private ServiceMappings getServiceMapping(ReqType reqType, String data) {
    		ServiceMappings sm = new ServiceMappings();
    		
    		data = (data == null) || (data.length() == 0) ? "{}" : data;
    		
    		switch (reqType) {
    			case DELETE:
    			case GET:
    				sm.setGetMapping(this, requestParams);
    				break;
    				
    			case POST:
    			case PUT:
    				sm.setPutMapping(this, data, requestParams);
    				break;
    		}
    		    		
    		return sm;
    }
}
