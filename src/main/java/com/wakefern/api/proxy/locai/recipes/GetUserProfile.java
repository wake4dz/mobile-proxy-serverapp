package com.wakefern.api.proxy.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**  
* A proxy API to access Locai's 'Get User Profile' API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class GetUserProfile extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetUserProfile.class);

    @POST
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.getUserProfile)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.auth) String jwtToken,
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  EnvManager.getTargetRecipeLocaiServiceEndpoint()
        			+ "/users/profile/get?clientId=" + EnvManager.getRecipeClientId()
        			+ "&apiKey=" + EnvManager.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);
            headers.put("Authorization", jwtToken);
            
            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, EnvManager.getApiMediumTimeout()));

        } catch (Exception e) {

			String errorData = parseAndLogException(logger, e, 
					WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType, contentType,
					"Authorization", jwtToken,
					"clientId", EnvManager.getRecipeClientId(),
					"apiKey", EnvManager.getTargetRecipeLocaiApiKey());
			
            return this.createErrorResponse(errorData, e);
        }
    }
}
