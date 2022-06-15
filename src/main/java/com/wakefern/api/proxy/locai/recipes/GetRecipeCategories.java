package com.wakefern.api.proxy.locai.recipes;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;

import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * A custom API to retrieve a list category/sub-category/dish-tag to help
 * building a Recipe Browse UI.
 * 
 * @author Danny Zheng
 *
 */

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.ProxyV8.path)
public class GetRecipeCategories extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetRecipeCategories.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path(WakefernApplicationConstants.RecipeLocai.ProxyV8.getRecipeCategories)
	public Response getResponse() {

		try {
			JSONObject jsonObject = new JSONObject(CategoryJsonFileCache.getJsonCache());
			
			return this.createValidResponse(jsonObject.toString());

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_RECIPES_LOCAI_GET_RECIPE_CATEGORIES);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		} 
	}

}
