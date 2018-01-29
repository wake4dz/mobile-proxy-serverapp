package com.wakefern.authentication;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.commons.text.StringEscapeUtils;
import org.json.JSONObject;

import com.wakefern.Shop.ShoppingApiEntry;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.FormattedAuthentication;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 10/5/16.
 */
@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class AuthorizationAuthenticate extends BaseService {
	
	private final static Logger logger = Logger.getLogger("AuthorizationAuthenticate");
	private static final String EMAIL = "Email";
	private static final String PASSWORD = "Password";
	private static final String APP_VERSION = "AppVersion";
	private static final String APP_VER_ERR = "501,Please update ShopRite to the latest version.";
	private static final String EMAIL_PASS_ERR = "401,Please check login credentials must not be blank.";
	private static final String ABERDEEN_PSEUDO_STORE = "DA87780"; //use in shop entry api to setup lists in sister store.
	
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/authenticate")
    public Response getInfo(@HeaderParam("Authorization") String authToken, String jsonBody){
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.authenticationToken;
        }else{
        	this.token = authToken;
        }
        //this.path = ApplicationConstants.Requests.Authentication.Authenticate + ApplicationConstants.StringConstants.authenticate;
        this.path = "https://api.shoprite.com/api/authorization/v5/authorization/authenticate";
        
        try {
            JSONObject messageJson = new JSONObject(jsonBody);
            //Test to see if there is user data, if not get thrown to guest auth
            StringEscapeUtils seu = new StringEscapeUtils();
            String userEmail =  String.valueOf(messageJson.get(EMAIL));
            String password = String.valueOf(messageJson.get(PASSWORD));
            
            if(userEmail.isEmpty() || password.isEmpty()){
            	messageJson.put(PASSWORD, password.isEmpty() ? "" : "----");//scrub the password out, don't let it get logged to console
            	logger.log(Level.SEVERE, "[getInfo]::Empty credential: "+messageJson.toString());
            	return this.createErrorResponse(new Exception(EMAIL_PASS_ERR));
            }
            try{
            	// reject all versions that are less than 2.0.0, session cop fix.
                int appVer = Integer.parseInt(String.valueOf(messageJson.get(APP_VERSION)).split("\\.")[0]);
	            if(appVer < 2){
	            	return this.createErrorResponse(new Exception(APP_VER_ERR));
	            }
            } catch(Exception e){
            	return this.createErrorResponse(new Exception(APP_VER_ERR));
            	
            }
            String escapeCharPass = seu.escapeHtml4(password);
            messageJson.put(PASSWORD, escapeCharPass);
            ServiceMappings mapping = new ServiceMappings();
            jsonBody = messageJson.toString();
            mapping.setPutMapping(this, jsonBody);
            String json;
            
            try {
                json = (HTTPRequest.executePostJSON(this.path, jsonBody, mapping.getgenericHeader(), 0));
            } catch (Exception e) {
            	logger.log(Level.SEVERE, "[getInfo]::Exception authenticate user: "+userEmail+", msg: "+e.getMessage());
                return this.createErrorResponse(e);
            }

            //run regular v5 authentication
//            String v5;
//            try {
//                Authentication authentication = new Authentication();
//                v5 = authentication.getInfo(jsonBody);
//            } catch (Exception e) {
//            	logger.log(Level.SEVERE, "[getInfo]::Exception authenticate in v5 ", e.toString());
//                return this.createErrorResponse(e);
//            }

            FormattedAuthentication formattedAuthentication = new FormattedAuthentication();

            try {
            	String formatAuthObjStr = formattedAuthentication.formatAuth(json, messageJson.getString(ApplicationConstants.FormattedAuthentication.Email),
                        ApplicationConstants.FormattedAuthentication.ChainId, ApplicationConstants.FormattedAuthentication.AuthPlanning).toString();
            	
            	if(!formattedAuthentication.getFromHome()){ //run shop Entry api using Aberdeen pseudo store Id, for MWG doesn't take sister store Id.
                	String userId = formattedAuthentication.getUserId();
                	String userToken = formattedAuthentication.getUserToken();
                	String storeName = formattedAuthentication.getStoreName();
            		this.callShoppingEntryAPI(userId, userToken, storeName);
            	}
                return Response.status(200).entity(formatAuthObjStr).build();
            } catch (Exception e) {
            	logger.log(Level.SEVERE, "[getInfo]::Exception getting auth response " +e.getMessage());
                return this.createErrorResponse(e);
            }
        } catch (Exception ex){
            //Invalid JSON return guest credentials
            try {
                Authentication authentication = new Authentication();
                return this.createValidResponse(authentication.getInfo("{}"));//{} is the jsonbody that triggers a guest authtoken
            } catch (Exception e){
            	logger.log(Level.SEVERE, "[getInfo]::Exception on authentication " +e.getMessage());
                return this.createErrorResponse(e);
            }
        }
    }
    
    /**
     * Fix production issue of new user with sister store, so entry api call will fail, 
     * 	it needs an actual SRFH store to successfully call entry API, for MWG to setup the acc & allow list API read/write initialization.
     * @param userId
     * @param userToken
     */
    private void callShoppingEntryAPI(String userId, String userToken, String storeName){
    	ShoppingApiEntry sae = new ShoppingApiEntry();
    	try {
			sae.getInfo(userId, ABERDEEN_PSEUDO_STORE, "", userToken);
			logger.log(Level.INFO, "[callShoppingEntryAPI]::shopping Entry store: "+storeName);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "[callShoppingEntryAPI]::Exception calling shopping Entry "+storeName+" "+ e.getMessage());
		}
    }

    public AuthorizationAuthenticate() {
        this.serviceType = new MWGHeader();
    }
}
