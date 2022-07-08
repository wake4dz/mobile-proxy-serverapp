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
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**  
* A proxy API to access Locai's 'Search Recipes' API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.ProxyV8.path)
public class SearchRecipes extends BaseService {

    private final static Logger logger = LogManager.getLogger(SearchRecipes.class);

    @POST
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.ProxyV8.searchRecipes)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
            final String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()
        			+ "/recipes/search?clientId=" + VcapProcessor.getRecipeClientId()
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
        			
            headers.put("Content-Type", contentType);
            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout()));
        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType, "HttpBody", jsonBody);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

	/**
	 * @param jsonBody
	 * @return
	 * @throws Exception
	 */
    public static String searchRecipes(String jsonBody) throws Exception {
        Map<String, String> headers = new HashMap<>();

        final String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()
            + "/recipes/search?clientId=" + VcapProcessor.getRecipeClientId()
            + "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();

        headers.put("Content-Type", "application/json");

        String response = HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout());
        JSONObject resObj = new JSONObject(response);
        return resObj.getJSONArray("hits").toString();
	}
}
