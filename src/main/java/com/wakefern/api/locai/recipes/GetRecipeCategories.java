package com.wakefern.api.locai.recipes;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import com.wakefern.global.BaseService;

import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * A custom API to retrieve a list category/sub-category/dish-tag to help
 * building a Recipe Browse UI.
 * 
 * @author Danny Zheng
 *
 */

@Path(WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class GetRecipeCategories extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetRecipeCategories.class);

	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path(WakefernApplicationConstants.RecipeLocai.Proxy.getRecipeCategories)
	public Response getResponse() {

		try {
			JSONObject jsonObject = new JSONObject(CategoryJsonFileCache.getJsonCache());
			
			return this.createValidResponse(jsonObject.toString());

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_LOCAI_GET_RECIPE_CATEGORIES);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		} 
	}

}
