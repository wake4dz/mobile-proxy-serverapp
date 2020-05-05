package com.wakefern.recipes.locai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
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

	private final static Logger logger = Logger.getLogger(GetRecipeCategories.class);

	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path(WakefernApplicationConstants.RecipeLocai.Proxy.getRecipeCategories)
	public Response getResponse() {
		
		InputStream is = null;
		BufferedReader br = null;
		
		try {
			String jsonFileName = null;
			
			//select which file to be sent based on recipe_service VCAP setting
			if (VcapProcessor.getRecipeService().trim().equalsIgnoreCase("Staging")) {
				jsonFileName = "recipe_qa.json";
			} else {
				jsonFileName = "recipe_prod.json";
			}

			is = getClass().getClassLoader().getResourceAsStream(jsonFileName);

			br = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			JSONObject jsonObject = new JSONObject(sb.toString());
			
			return this.createValidResponse(jsonObject.toString());

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_LOCAI_GET_RECIPE_CATEGORIES);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
