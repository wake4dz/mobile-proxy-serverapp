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
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
/**  
* A proxy API to access Locai's SearchBySkus API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class SearchBySkus extends BaseService {

    private final static Logger logger = LogManager.getLogger(SearchBySkus.class);

    @POST
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.searchBySkus)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {
        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  EnvManager.getTargetRecipeLocaiServiceEndpoint()
        			+ "/products/internal-variant-id-lookup-by-sku/batch?clientId=" + EnvManager.getRecipeClientId()
        			+ "&apiKey=" + EnvManager.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);

            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, EnvManager.getApiMediumTimeout()));

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

