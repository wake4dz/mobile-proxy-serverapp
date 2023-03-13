package com.wakefern.api.proxy.mi9.v8.lists.planning;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.itemLocator.ItemLocatorUtils;
import com.wakefern.wynshop.WynshopApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Danny Zheng
 * @date 2021-09-13
 * 
 *       https://mobile-gateway.staging.brands.wakefern.com/index.html#/Lists/get_api_lists_planning__retailerstoreid_
 *       Lists: Get /api/lists/planning/{retailerstoreid} - Retrieves planning
 *       for a store curl -X 'GET' \
 *       'https://mobile-gateway.staging.brands.wakefern.com/api/lists/planning/144'
 *       \
 * 
 * 
 */
@Path(ApplicationConstants.Requests.Proxy + WynshopApplicationConstants.Requests.Routes.ShoppingCartItemLocator)
public class GetPlanningListItemLocator extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetPlanningListItemLocator.class);

	@GET
	public Response getPlanningListItemLocator(
			@HeaderParam(Requests.Headers.Accept) String accept,
			@HeaderParam(Requests.Headers.xSiteHost) String xSiteHost,
			@HeaderParam(Requests.Headers.Authorization) String sessionToken,
			@PathParam(WynshopApplicationConstants.Requests.Params.Path.storeID) String storeId) {
		try {

			final String url = WynshopApplicationConstants.BaseURL + "/lists/planning/" + storeId.trim();
			
			Map<String, String> headerMap = new HashMap<>();

			//for the Cloudflare pass-thru
			headerMap.put(Requests.Headers.userAgent,
					ApplicationConstants.StringConstants.wakefernApplication);

			headerMap.put(Requests.Headers.Authorization, sessionToken);
			headerMap.put(Requests.Headers.Accept, accept);
			headerMap.put(Requests.Headers.xSiteHost, xSiteHost);

			String response = HTTPRequest.executeGet(url, headerMap, EnvManager.getApiMediumTimeout());

			// call Wakefern's ItemLocator API to get additional data
			// return empty item locator info for each sku if ItemLocator API fails for some reason
			response = ItemLocatorUtils.decorateCollectionWithItemLocations(storeId,response, ItemLocatorUtils.ResponseType.PLANNING_LIST);

			return createValidResponse(response);

		} catch (Exception e) {
			
			if (LogUtil.isLoggable(e)) {
				String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
						Requests.Headers.Accept, accept, Requests.Headers.xSiteHost, xSiteHost,
						Requests.Headers.Authorization, sessionToken, WynshopApplicationConstants.Requests.Params.Path.storeID, storeId);
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}
			
			return createErrorResponse(e);
		}
	}
}