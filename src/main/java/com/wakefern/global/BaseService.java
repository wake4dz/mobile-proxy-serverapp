package com.wakefern.global;

import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.request.HTTPRequest;
import com.wakefern.request.models.Header;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

public class BaseService {
    protected HashMap<String, String> requestParams = null;
    protected HashMap<String, String> queryParams   = null;
    
    protected Header requestHeader = null;
    protected String requestPath   = null;
    protected String requestToken  = null;
    
    protected static enum ReqType { GET, POST, PUT, DELETE };
    
    /**
     * Trigger a request to the MyWebGrocer REST API.
     * 
     * @param reqType
     * @param reqData
     * @return
     * @throws Exception
     * @throws IOException
     */
    protected String mwgRequest(ReqType reqType, String reqData) throws Exception, IOException {
    		String reqTypeStr;
    		String reqBody;
    		String response;
    		
    		boolean isValidType = true;
    		
    		switch (reqType) {
    			case GET    : reqTypeStr = "GET";    break;
    			case POST   : reqTypeStr = "POST";   break;
    			case PUT    : reqTypeStr = "PUT";    break;
    			case DELETE : reqTypeStr = "DELETE"; break;
    			default     : 
    				reqTypeStr  = ""; 
    				isValidType = false;
    		}
    		
    		if ((requestPath == null) || (requestHeader == null)) {
    			throw new Exception("Unable to execute " + reqTypeStr + " request.  Missing required data.");
    			
    		} else {
    			if (!isValidType) {
    				throw new Exception("Unable to execute request. Invalid request type.");
    				
    			} else {
    				ServiceMappings sm = getServiceMapping(reqType, reqData);
    				
    				String reqURL = sm.getPath();
        			Map<String, String> reqHead = sm.getgenericHeader();
        			
            		switch (reqType) {
	        			case GET : 
	        				response = HTTPRequest.executeGet(reqURL, reqHead, 0);
	        				break;
	        			case POST :
	        				reqBody  = sm.getGenericBody();
	        				response = HTTPRequest.executePost(reqURL, reqBody, reqHead);
	        				break;
	        			case PUT :
	        				reqBody  = sm.getGenericBody();
	        				response = HTTPRequest.executePut(reqURL, reqBody, reqHead);
	        				break;
	        			case DELETE :
	        				response = HTTPRequest.executeDelete(reqURL, reqHead, 0);
	        				break;
        				default :
        					response = "{}"; // This should never actually happen.  BUT just in case...
            		}
            		
            		return response;
    			}
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
    				sm.setGetMapping(this, requestParams, queryParams);
    				break;
    				
    			case POST:
    			case PUT:
    				sm.setPutMapping(this, data, requestParams, queryParams);
    				break;
    		}
    		    		
    		return sm;
    }
}
