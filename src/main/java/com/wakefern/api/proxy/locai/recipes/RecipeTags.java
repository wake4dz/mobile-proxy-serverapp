package com.wakefern.api.proxy.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
/**  
* A proxy API to access Locai's 'Recipe Tags' API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class RecipeTags extends BaseService {

    private final static Logger logger = Logger.getLogger(RecipeTags.class);

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.recipeTags)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType) {
        
    	Map<String, String> headers = new HashMap<>();
    	
        try {
            
        	String path = VcapProcessor.getTargetRecipeLocaiServiceEndpoint() + 
        			"/recipes/tags?clientId=" + VcapProcessor.getRecipeClientId()
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
       
        	headers.put("Content-Type", contentType);
        	
            return this.createValidResponse(HTTPRequest.executeGet(path, headers, VcapProcessor.getApiMediumTimeout()));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.PROXY_RECIPES_LOCAI_RECIPE_TAGS);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

}
