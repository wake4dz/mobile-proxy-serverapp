package com.wakefern.api.proxy.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
* A proxy API to access Locai's SearchBySkus API
*   
* @author  Danny Zheng
*
*/ 

@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class SearchBySkus extends BaseService {

    private final static Logger logger = Logger.getLogger(SearchBySkus.class);

    @POST
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.searchBySkus)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

    	logger.error("dz...");
        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()  
        			+ "/products/internal-variant-id-lookup-by-sku/batch?clientId=" + VcapProcessor.getRecipeClientId()
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);

            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout()));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.PROXY_RECIPES_LOCAI_SEARCH_BY_SKUS);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType, "HttpBody", jsonBody);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
