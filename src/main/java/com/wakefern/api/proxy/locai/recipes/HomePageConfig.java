package com.wakefern.api.proxy.locai.recipes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * A proxy API to access Locai's 'Homepage Configuration API, which Locai uses a
 * third-party Algolia service
 * 
 * @author Danny Zheng
 *
 */

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class HomePageConfig extends BaseService {

	private final static Logger logger = LogManager.getLogger(HomePageConfig.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path(WakefernApplicationConstants.RecipeLocai.Proxy.homepageConfig)
	public Response getResponse(
			@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
			@QueryParam(WakefernApplicationConstants.RecipeLocai.RequestParamsQuery.banner) String banner) {

		Map<String, String> headers = new HashMap<>();

		try {            
			String path = EnvManager.getTargetRecipeLocaiServiceEndpoint() + "/content/wakefern?" + "&apiKey="
					+ EnvManager.getTargetRecipeLocaiApiKey();

			headers.put("Content-Type", contentType);

			String response = HTTPRequest.executeGet(path, headers, EnvManager.getApiHighTimeout());
			logger.debug("Locai's response data: " + response);
			return createValidResponse(filterLayoutsFromResponse(response, banner));

		} catch (Exception e) {

			String errorData = parseAndLogException(logger, e, 
					WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType, contentType,
					WakefernApplicationConstants.RecipeLocai.RequestParamsQuery.banner, banner,
					"apiKey", EnvManager.getTargetRecipeLocaiApiKey());
			
			return this.createErrorResponse(errorData, e);
		}
	}

	/**
	 * Fetch the current homepageConfig.
	 * @return HomepageConfig
	 * @throws Exception
	 */
	public static JSONObject fetch(String banner) throws Exception {
		Map<String, String> headers = new HashMap<>();
		String path = EnvManager.getTargetRecipeLocaiServiceEndpoint() + "/content/wakefern?" + "&apiKey="
				+ EnvManager.getTargetRecipeLocaiApiKey();

		headers.put("Content-Type", "application/json");

		String response = HTTPRequest.executeGet(path, headers, EnvManager.getApiHighTimeout());
		logger.debug("Locai's response data: " + response);
		return new JSONObject(filterLayoutsFromResponse(response, banner));
	}

	/**
	 * Filter non-ShopRite layouts and expired layouts from the response body.
	 * 
	 * @param response String
	 * @return String
	 */
	private static String filterLayoutsFromResponse(String response, String banner) {

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

			// filter out some invalid layouts/shelves
			if ((isWakefernBrand(layout, banner)) && (isValidDateRange(layout))) {
				filteredLayouts.put(layout);
			}
		}

		// Update the "layout" prop
		hit.remove("layout");
		hit.put("layout", filteredLayouts);

		logger.debug("After filtering the original response data: " + responseObj);

		return responseObj.toString();
	}

	/*
	 * only if the client_brand is a Wakefern banner
	 */
	private static boolean isWakefernBrand(JSONObject layout, String banner) {
		JSONArray clientBrandDisplay = layout.getJSONArray("client_brand_display");

		//this temp check is for the backward compatibility before proxy's multi-banner feature
		//we will check this banner <> null, an banner is not empty after these 3 lines of line in the future.
		if (banner == null ) {
			banner = WakefernApplicationConstants.WakefernBanners.SHOPRITE;
		}
		
		/*
		 * banner is one of values below
		 * shoprite
	       thefreshgrocer
	       fairwaymarket
	       dearbornmarket
	       gourmetgarage
	       pricerite
		 */
		
		logger.debug("banner: " + banner);
		
		final int len = clientBrandDisplay.length();
		for (int i = 0; i < len; i++) {
			if (clientBrandDisplay.getString(i).equalsIgnoreCase(banner.trim())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * only if the current date/time is btw a recipe's start datetime and stop
	 * datetime If the start/end datetime value is null/empty, it means this recipe
	 * is displayed all the time
	 */
	private static boolean isValidDateRange(JSONObject layout) {
		boolean isValid = true;
		
		String shelfTitle = null;
		String shelfStart = null;
		String shelfStop = null;
		
		Date currentServerDateTime = null;

		Date startServerDateTime = null;
		Date stopServerDateTime = null;
		
		try {
			shelfTitle = layout.getString("title").trim();
			shelfStart = layout.getString("start_time").trim();
			shelfStop = layout.getString("stop_time").trim();
	
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			
			sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
			
			currentServerDateTime = new Date();
			//String currentDateTimeString = sdf.format(currentDateTime);
						
			if ((shelfStart.length() == 0) && (shelfStop.length() == 0)) {
				// good, no datetime restraint
			} else if ((shelfStart.length() > 0) && (shelfStop.length() > 0)) {
				startServerDateTime = sdf.parse(shelfStart);
				stopServerDateTime = sdf.parse(shelfStop);
				
				if ((currentServerDateTime.after(startServerDateTime)) && (currentServerDateTime.before(stopServerDateTime))) {
					// good, within the date range
				} else {
					isValid = false;
				}	
			} else if ((shelfStart.length() > 0) && (shelfStop.length() == 0)) {
				startServerDateTime = sdf.parse(shelfStart);
				if (currentServerDateTime.after(startServerDateTime)) {
					// good, shelf in effect without a stop datetime
				} else {
					isValid = false;
				}
			} else if ((shelfStart.length() ==0) && (shelfStop.length() > 0)) {
				stopServerDateTime = sdf.parse(shelfStop);
				if (currentServerDateTime.before(stopServerDateTime)) {
					// good, shelf in effect without a start datetime
				} else {
					isValid = false;
				}
			} else {
				// 4 different computations are already performed above, this "else" is more for warning message.
				logger.warn("Unknown case for shelf title: " + shelfTitle + " [start datetime: " + shelfStart+ ", start datetime in server: " + startServerDateTime + ", "
						+ "stop datetime: " + shelfStop + ", stop datetime in server: " + stopServerDateTime + ", current server datetime: " + currentServerDateTime + "]");
			}
			
			// special note: current server time is 5-hr earlier than EST, but start/stop datetime are also 5-hrs earlier in server
			// so, we are still comparing apple to apple in term of the timezone.
			logger.debug("IsValid?: " + isValid + ", shelf title: " + shelfTitle + " [start datetime: " + shelfStart+ ", start datetime in server: " + startServerDateTime + ", "
					+ "stop datetime: " + shelfStop + ", stop datetime in server: " + stopServerDateTime + ", current server datetime: " + currentServerDateTime + "]");

		} catch (Exception e) {
			logger.error("Shelf title: " + shelfTitle + " [start datetime: " + shelfStart+ ", start datetime in server: " + startServerDateTime + ", "
					+ "stop datetime: " + shelfStop + ", stop datetime in server: " + stopServerDateTime + ", current server datetime: " + currentServerDateTime + "]");
			logger.error(LogUtil.getRelevantStackTrace(e));
			
			// As documented at https://wakefern.atlassian.net/browse/DMAU-1473, we still want this shelf to be in UI even if there is an exception thrown
			return true;
		}
		
		return isValid;
	}
	
}
