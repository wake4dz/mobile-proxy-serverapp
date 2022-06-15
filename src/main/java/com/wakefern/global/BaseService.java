package com.wakefern.global;

import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.errorHandling.ExceptionHandler;

public class BaseService {
    protected String requestPath   = null;

    private final static Logger logger = LogManager.getLogger(BaseService.class);
        
    /**
     * Create a standardized Error Response (HTTP 5xx / 4xx) to pass back to the UI.
     * 
     * @param e
     * @return
     */
    protected Response createErrorResponse(Exception e) {
		return createErrorResponse(null, e);
    }
    
    /**
     * Create a standardized Error Response (HTTP 5xx / 4xx) to pass back to the UI.
     * 
     * @param errorData String
	 * @param e	Exception
     * @return
     */
    protected Response createErrorResponse(String errorData, Exception e) {
        try {
        	String jsonErrStart = "{\"ErrorMessage\":\"";
        	String jsonErrEnd   = "\"}";
        		
            String[] array = e.getMessage().split(",");
            String buildError;

            final int statusCode = Integer.parseInt(array[0]);
            
            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                buildError = jsonErrStart + ApplicationConstants.Requests.unauthorizedError + jsonErrEnd;
            
            } else {	
            		StringBuilder sb = new StringBuilder();
            		
            		// We have to allow for the possibility that there's more than one "," in the exception's error message.
            		// For example, the error message may actually be a JSON string.
            		for (int i = 1; i < array.length; i++) {
            			sb.append(array[i]).append(",");
            		}
            		
            		// Strip off the trailing comma & convert to a String
            		sb.deleteCharAt(sb.length() - 1);
            		String respBody = sb.toString();
            		
            		// Test to see if the response is already a valid JSON string.
            		// If so, just assign it to the 'buildError' var.
            		// If it's not, assemble the 'buildError' var as a JSON string.
            		try {
            			new JSONObject(respBody);
            			buildError = respBody;
        			
            		} catch (Exception ex) {
            			try {
            				new JSONArray(respBody);
            				buildError = respBody;
            			
            			} catch (Exception exx) {
            				logger.error(errorData != null ?
									"MWG returned an unexpected, non-JSON compliant error: " + errorData + " - " + respBody
									: "MWG returned an unexpected, non-JSON compliant error: " + respBody);

            				
            				// The error is in an unexpected format.
            				// Respond with a default text message.
            				buildError = jsonErrStart + "MWG returned an unexpected, non-JSON compliant error." + jsonErrEnd;
            			}
        			}
            }

            return Response.status(statusCode).entity(buildError).build();
        
        } catch (Exception stringError) {
            return Response.status(500).entity(ExceptionHandler.fromException(e)).build();
        }
    }
    
    /**
     * Create Valid (HTTP 200) Response.
     * 
     * @param jsonResponse
     * @return
     */
    protected Response createValidResponse(String jsonResponse) {
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
