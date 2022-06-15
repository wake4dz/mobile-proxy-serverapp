package com.wakefern.api.proxy.locai.recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.*;

/**  
* A proxy API to access Locai's 'Get User Favorite' API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.ProxyV8.path)
public class GetUserFavorites extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetUserFavorites.class);

    @GET
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.ProxyV8.getUserFavorites)
    public Response getResponse(
    		@PathParam(WakefernApplicationConstants.RecipeLocai.RequestsParamsPath.userId) String userId, 
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.auth) String jwtToken) {
        try {
            return createValidResponse(getFavoriteRecipes(userId, contentType, jwtToken));
        } catch (Exception e) {
            LogUtil.addErrorMaps(e, ErrorType.PROXY_RECIPES_LOCAI_GET_USER_FAVORITES);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"userId", userId, "contentType", contentType);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

	/**
	 * Make the request to fetch the user's favorite recipes.
	 * @param userId String
	 * @param contentType String
	 * @param jwtToken String
	 * @return String
	 * @throws Exception
	 */
    private static String getFavoriteRecipes(final String userId, final String contentType, final String jwtToken) throws Exception {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType);
        headers.put("Authorization", jwtToken);

        final String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()
              + "/users/" + userId
              + "/dishes/favorite?clientId=" + VcapProcessor.getRecipeClientId()
              + "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();

        return HTTPRequest.executeGet(path, headers, VcapProcessor.getApiMediumTimeout());
    }
}

