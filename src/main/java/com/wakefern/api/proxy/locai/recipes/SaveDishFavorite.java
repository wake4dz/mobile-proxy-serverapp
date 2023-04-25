package com.wakefern.api.proxy.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
* A proxy API to access Locai's 'Save Dish Favorite' API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class SaveDishFavorite extends BaseService {

    private final static Logger logger = LogManager.getLogger(SaveDishFavorite.class);

    @PUT
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.saveDishFavorite)
    public Response getResponse(
    		@PathParam(WakefernApplicationConstants.RecipeLocai.RequestsParamsPath.userId) String userId, 
    		@PathParam(WakefernApplicationConstants.RecipeLocai.RequestsParamsPath.dishId) String dishId,
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.auth) String jwtToken,
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  EnvManager.getTargetRecipeLocaiServiceEndpoint()
        			+ "/users/" + userId
        			+ "/dishes/" + dishId
        			+ "/favorite/set?clientId=" + EnvManager.getRecipeClientId()
        			+ "&apiKey=" + EnvManager.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);
            headers.put("Authorization", jwtToken);
            
            return this.createValidResponse(HTTPRequest.executePut(path, jsonBody, headers, EnvManager.getApiMediumTimeout()));

        } catch (Exception e) {

			String errorData = parseAndLogException(logger, e, 
					WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType, contentType,
					"clientId", EnvManager.getRecipeClientId(),
					"apiKey", EnvManager.getTargetRecipeLocaiApiKey(),
					WakefernApplicationConstants.RecipeLocai.HeadersParams.auth, jwtToken,
					"userId", userId,
					"dishId", dishId,
					"httpBody", jsonBody);
			
            return this.createErrorResponse(errorData, e);
        }
    }
}
