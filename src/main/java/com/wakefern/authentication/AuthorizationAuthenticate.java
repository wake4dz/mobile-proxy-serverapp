package com.wakefern.authentication;

import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.FormattedAuthentication;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

@Path(MWGApplicationConstants.Requests.Account.acctPath)
public class AuthorizationAuthenticate extends BaseService {
	
	private final static Logger logger = Logger.getLogger("AuthorizationAuthenticate");
	private static final String appVersionErr = "501, Please update ShopRite to the latest version.";
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
    public AuthorizationAuthenticate() {
        this.serviceType = new MWGHeader();
        this.path = MWGApplicationConstants.Requests.Account.acctPath + MWGApplicationConstants.Requests.Account.authPath;
    } 

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Account.authPath)
    public Response getInfo(@PathParam("chainId") String chainId, @HeaderParam("Authorization") String authToken, String jsonBody) {    
    		
    		String emailKey  = "Email";
    		String pwKey     = "Password";
    		String appVerKey = "AppVersion";
    	
    		// Here in Wakefern API Land, we don't care if the Auth Token supplied by the UI is valid or not.
    		// Simply send it to MWG and let them figure it out.
    		// MWG's response will be passed along to the UI, which will deal with it accordingly.
    		this.token = authToken;
        
        JSONObject postDataJSON = new JSONObject(jsonBody);
        
        String userEmail = (postDataJSON.has(emailKey)) ? String.valueOf(postDataJSON.get(emailKey)) : ""; 
        String password  = (postDataJSON.has(pwKey))    ? String.valueOf(postDataJSON.get(pwKey))    : "";
        
        int appVer = (postDataJSON.has(appVerKey)) ? Integer.parseInt(String.valueOf(postDataJSON.get(appVerKey)).split("\\.")[0]) : 0;
        
        // Reject all versions that are less than 2.0.0. Session cop fix.
        if (appVer < 2) {
    			return this.createErrorResponse(new Exception(appVersionErr));
        
        } else {
	        String responseJSON;
	        ServiceMappings mapping = new ServiceMappings();
	        HashMap<String, String> reqParams = new HashMap<String, String>();
	        
	        reqParams.put(MWGApplicationConstants.chainID, chainId);
	        postDataJSON.put(pwKey, StringEscapeUtils.escapeHtml4(password));
	        mapping.setPutMapping(this, postDataJSON.toString(), reqParams);
	        
	        String reqURL = mapping.getPath();
	        String reqData = mapping.getGenericBody();
	        Map<String, String> reqHead = mapping.getgenericHeader();
	        
	        try {
	        		responseJSON = HTTPRequest.executePostJSON(reqURL, reqData, reqHead, 0);
	        
	        } catch (Exception e) {
	        		logger.log(Level.SEVERE, "[getInfo]::Exception authenticate user: {0}, msg: {1}", new Object[]{userEmail, e.toString()});
	            return this.createErrorResponse(e);
	        }
	
	        FormattedAuthentication formattedAuthentication = new FormattedAuthentication();
	
	        try {
	            return Response.status(200).entity(
	            		formattedAuthentication.formatAuth(
	            				responseJSON, 
	            				postDataJSON.getString(ApplicationConstants.FormattedAuthentication.Email),
	            				ApplicationConstants.FormattedAuthentication.ChainId, 
	            				ApplicationConstants.FormattedAuthentication.AuthPlanning
	    				).toString()
	        		).build();
	        
	        } catch (Exception e) {
	        		logger.log(Level.SEVERE, "[getInfo]::Exception occurred getting auth response ", e);
	            return this.createErrorResponse(e);
	        }
        }
        
//        try {
//        		//JSONObject messageJson = new JSONObject(jsonBody);
//            
//            // Test to see if there is user data. If not, get Thrown to guest authorization
//            //String userEmail = String.valueOf(messageJson.get(Email));
//            //String password = String.valueOf(messageJson.get(Password));
//            
////            if ((userEmail.length() == 0) && (password.length() == 0)) {
////            		// UI sent empty Username & Password fields. Default to Guest User status.
////            		throw new Exception();
////            }
//                        
////            try {
////            		// Reject all versions that are less than 2.0.0, session cop fix.
////                int appVer = Integer.parseInt(String.valueOf(messageJson.get(AppVersion)).split("\\.")[0]);
////	            
////                if (appVer < 2) {
////	            		return this.createErrorResponse(new Exception(appVersionErr));
////	            }
////                
////            } catch(Exception e) {
////            		return this.createErrorResponse(new Exception(appVersionErr));
////            }
//            
////            String json;
////            ServiceMappings mapping = new ServiceMappings();
////            String escapeCharPass = StringEscapeUtils.escapeHtml4(password);
////            HashMap<String, String> reqParams = new HashMap<String, String>();
//
////            reqParams.put(MWGApplicationConstants.chainID, chainId);
////            messageJson.put(Password, escapeCharPass);
////            jsonBody = messageJson.toString();
////            mapping.setPutMapping(this, jsonBody, reqParams);
//            
////            String reqURL = mapping.getPath();
////            String reqData = mapping.getGenericBody();
////            Map<String, String> reqHead = mapping.getgenericHeader();
//            
////            try {
////                json = HTTPRequest.executePostJSON(reqURL, reqData, reqHead, 0);
////            } catch (Exception e) {
////            		logger.log(Level.SEVERE, "[getInfo]::Exception authenticate user: {0}, msg: {1}", new Object[]{userEmail, e.toString()});
////                return this.createErrorResponse(e);
////            }
////
////            FormattedAuthentication formattedAuthentication = new FormattedAuthentication();
////
////            try {
////                return Response.status(200).entity(
////                		formattedAuthentication.formatAuth(
////                				json, 
////                				messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
////                				ApplicationConstants.FormattedAuthentication.ChainId, 
////                				ApplicationConstants.FormattedAuthentication.AuthPlanning
////        				).toString()
////            		).build();
////            
////            } catch (Exception e) {
////            		logger.log(Level.SEVERE, "[getInfo]::Exception occurred getting auth response ", e);
////                return this.createErrorResponse(e);
////            }
//        
//        } catch (Exception ex) {
//            // Return guest credentials
//            try {
//                Authentication authentication = new Authentication();
//                return this.createValidResponse(authentication.getInfo());
//            
//            } catch (Exception e) {
//            		logger.log(Level.SEVERE, "[getInfo]::Exception occurred on authentication ", e);
//                return this.createErrorResponse(e);
//            }
//        }
    }   
}
