package com.wakefern.api.proxy.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.logging.LogUtil;
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

    private final static Logger logger = LogManager.getLogger(RecipeTags.class);

    @GET
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.recipeTags)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType) {
        
    	  Map<String, String> headers = new HashMap<>();
    	
        try {
            
            String path = EnvManager.getTargetRecipeLocaiServiceEndpoint() +
                "/recipes/tags?clientId=" + EnvManager.getRecipeClientId()
                + "&apiKey=" + EnvManager.getTargetRecipeLocaiApiKey();

            headers.put("Content-Type", contentType);
        	
            return this.createValidResponse(HTTPRequest.executeGet(path, headers, EnvManager.getApiMediumTimeout()));

        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType);
            
            if (LogUtil.isLoggable(e)) {
            	logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            }

            return this.createErrorResponse(errorData, e);
        }
    }
}
