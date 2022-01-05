package com.wakefern.api.proxy.locai.recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

/**
 * Custom endpoint that batches calls to Recipe Search to fetch all recipes for a set of shelves (via HomePageConfig.fetch)
 */
@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.ProxyV8.path)
public class GetAllShelves extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetAllShelves.class);

	@POST
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path(WakefernApplicationConstants.RecipeLocai.ProxyV8.getShelves)
	public Response getResponse(
			@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType,
			String jsonBody)
	{
		try {
			// Fetch the homepage config.
			JSONObject homePageConfig = HomePageConfig.fetch();

			// Iterate over the shelf layouts and fetch all the recipes.
			JSONArray layouts = homePageConfig.getJSONArray("hits").getJSONObject(0).getJSONArray("layout");

			JSONArray hydratedLayouts = RecipeUtils.fetchAllShelfRecipes(layouts, jsonBody);
			return createValidResponse(hydratedLayouts.toString());
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_RECIPES_LOCAI_GET_SHELVES);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
