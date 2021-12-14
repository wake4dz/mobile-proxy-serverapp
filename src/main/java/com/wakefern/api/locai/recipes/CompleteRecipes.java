package com.wakefern.api.locai.recipes;

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

import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**  
* A proxy API to access Locai's 'Complete Recipes' API
*   
* @author  Danny Zheng
*
*/ 

@Path(WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class CompleteRecipes extends BaseService {

    private final static Logger logger = LogManager.getLogger(CompleteRecipes.class);

    @POST
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.completeRecipes)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint() 
        			+ "/recipes/completion/batch?clientId=" + VcapProcessor.getRecipeClientId() 
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
        			
            headers.put("Content-Type", contentType);

            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout()));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_LOCAI_COMPLETE_RECIPES);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType, "httpBody", jsonBody);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
