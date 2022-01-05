package com.wakefern.api.proxy.locai.recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * A proxy API to access Locai's 'Lookup Recipes' API
 *
 * @author Danny Zheng
 */

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.ProxyV8.path)
public class LookupRecipes extends BaseService {
	private final static Logger logger = LogManager.getLogger(LookupRecipes.class);


	@POST
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path(WakefernApplicationConstants.RecipeLocai.ProxyV8.lookupRecipes)
	public Response getResponse(
			@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType,
			String jsonBody) {

		Map<String, String> headers = new HashMap<>();

		try {
			String path = VcapProcessor.getTargetRecipeLocaiServiceEndpoint()
					+ "/recipes/lookup/batch?clientId=" + VcapProcessor.getRecipeClientId()
					+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();

			headers.put("Content-Type", contentType);

			return this.createValidResponse(HTTPRequest.executePost(path, jsonBody, headers, VcapProcessor.getApiMediumTimeout()));

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_RECIPES_LOCAI_LOOKUP_RECIPES);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", contentType, "HttpBody", jsonBody);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
