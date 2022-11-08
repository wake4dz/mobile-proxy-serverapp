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
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * This tracking call is used for optimizing the cook-it application 
 * and further personalize the recipe and ingredient recommendations.
 * 
 * @author  Danny Zheng
 * 
 */

@Path(ApplicationConstants.Requests.Proxy +  WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class RecipeTracking extends BaseService {

    private final static Logger logger = LogManager.getLogger(ProductLookup.class);

    @POST
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.recipeTracking)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()  
        			+ "/recipes/add-to-cart?clientId=" + VcapProcessor.getRecipeClientId()
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);

            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout()));

        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType, "HttpBody", jsonBody);
            
            if (LogUtil.isLoggable(e)) {
            	logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            }

            return this.createErrorResponse(errorData, e);
        }
    }
}
