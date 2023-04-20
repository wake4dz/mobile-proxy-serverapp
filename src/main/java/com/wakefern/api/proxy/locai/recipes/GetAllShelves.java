package com.wakefern.api.proxy.locai.recipes;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Custom endpoint that batches calls to Recipe Search to fetch all recipes for a set of shelves (via HomePageConfig.fetch)
 */
@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class GetAllShelves extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetAllShelves.class);

    private static final String BAD_REQUEST_ERROR_MSG = "Request body must be a json object";

    @POST
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.getShelves)
    public Response getResponse(
            @HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType,
            @QueryParam(WakefernApplicationConstants.RecipeLocai.RequestParamsQuery.banner) String banner,
            String jsonBody)
    {
        try {
            // Check the body of the request. If not valid JSON object, throw Bad Request error
            if (jsonBody == null) {
                return this.createErrorResponse(Response.Status.BAD_REQUEST, BAD_REQUEST_ERROR_MSG);
            }
            try {
                new JSONObject(jsonBody);
            } catch (JSONException e) {
                return this.createErrorResponse(Response.Status.BAD_REQUEST, BAD_REQUEST_ERROR_MSG);
            }

            // Fetch the homepage config.
            JSONObject homePageConfig = HomePageConfig.fetch(banner);

            // Iterate over the shelf layouts and fetch all the recipes.
            JSONArray layouts = homePageConfig.getJSONArray("hits").getJSONObject(0).getJSONArray("layout");
  
            logger.debug("banner: " + banner);
            logger.debug("layouts: " + layouts);
            
            JSONArray hydratedLayouts = RecipeUtils.fetchAllShelfRecipes(layouts, jsonBody);
            return createValidResponse(hydratedLayouts.toString());
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
