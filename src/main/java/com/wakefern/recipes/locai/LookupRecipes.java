package com.wakefern.recipes.locai;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;


@Path("recipeshop/lookupRecipes")
public class LookupRecipes extends BaseService {

    private final static Logger logger = Logger.getLogger(LookupRecipes.class);

    @POST
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path("/")
    public Response getResponse(String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
            //String path = WakefernApplicationConstants.APIM.apimBaseURL + WakefernApplicationConstants.APIM.nutritionBySkuStoreId + "/" + skuStoreId;
        	String path =  "https://cookit-api-stg.locai.io/v1" + "/recipes/lookup/batch?clientId=" + "b35a28e9-4116-41fa-88ca-0aaaebaaa33b" +
        					"&apiKey=" + "ixvlsNKAvUN30Ah03pN2F7Y3gGWEJLpE";
        			
            headers.put("Content-Type","application/json");

            return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout()));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.NUTRITION_GET_NUTRITION);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
