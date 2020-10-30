package com.wakefern.api.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.json.JSONArray;
import org.json.JSONObject;

/**
* A proxy API to access Locai's 'Homepage Configuration API, which Locai uses a third-party Algolia service
*   
* @author  Danny Zheng
*
*/ 

@Path(WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class HomePageConfig extends BaseService {

    private final static Logger logger = Logger.getLogger(HomePageConfig .class);

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.homepageConfig)
    public Response getResponse(
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType ) {

        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()  
        			+ "/content/wakefern?"
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);

            String response = HTTPRequest.executeGet(path, headers, VcapProcessor.getApiHighTimeout());
            return createValidResponse(filterNonShopRiteLayoutsFromResponse(response));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_LOCAI_HOMEPAGE_CONFIG);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		 "contentType", contentType);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

	/**
	 * Filter non-ShopRite layouts from the response body.
	 * @param response String
	 * @return String
	 */
	private static String filterNonShopRiteLayoutsFromResponse(String response) {
		JSONObject responseObj = new JSONObject(response);
		JSONArray hits = responseObj.optJSONArray("hits");
		if (hits == null) {
			return response;
		}
		JSONObject hit = hits.optJSONObject(0);
		if (hit == null) {
			return response;
		}
		JSONArray layouts = hit.optJSONArray("layout");
		if (layouts == null || layouts.length() == 0) {
			return response;
		}
		final int numLayouts = layouts.length();
		JSONArray filteredLayouts = new JSONArray();
		for (int i = 0; i < numLayouts; i++) {
			JSONObject layout = layouts.getJSONObject(i);
			JSONArray clientBrandDisplay = layout.getJSONArray("client_brand_display");
			if (containsShopRite(clientBrandDisplay)) {
				filteredLayouts.put(layout);
			}
		}

		// Update the "layout" prop
		hit.remove("layout");
		hit.put("layout", filteredLayouts);
		return responseObj.toString();
	}

	private static boolean containsShopRite(JSONArray brandDisplays) {
		final int len = brandDisplays.length();
		for (int i = 0; i < len; i++) {
			if (brandDisplays.getString(i).equalsIgnoreCase("shoprite")) {
				return true;
			}
		}
		return false;
	}
}
