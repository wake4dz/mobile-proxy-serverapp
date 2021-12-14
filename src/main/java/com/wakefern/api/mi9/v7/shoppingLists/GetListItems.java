package com.wakefern.api.mi9.v7.shoppingLists;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MI9TimeoutRegistry;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)
public class GetListItems extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetListItems.class);

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetListItems() {
		this.requestPath = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.ShoppingList.items)
	@Produces(MWGApplicationConstants.Headers.ShoppingList.items)
	@Path(MWGApplicationConstants.Requests.ShoppingList.items)
	public Response getResponse(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.listID) String listID,

			@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String fd,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.categoryMap) String catMap, // Sort of like a "cat nap", but not really.  :-)

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.items, MWGApplicationConstants.Headers.generic, sessionToken);
		this.requestParams = new HashMap<>();
		this.queryParams = new HashMap<>();

		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);

		// Build the Map of Query String parameters.
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.categoryMap, catMap);
		if (!fd.isEmpty()) {
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, fd);
		}

		try {
			String jsonResponse = mwgRequest(BaseService.ReqType.GET, null,
					MI9TimeoutRegistry.SHOPPINGLIST_GET_LIST_ITEMS,
					MI9TimeoutRegistry.getTimeout(MI9TimeoutRegistry.SHOPPINGLIST_GET_LIST_ITEMS));
			// Item Location data provided by MWG is never up-to-date.
			// Used Wakefern-supplied Item Location data instead.
			// The Store ID being passed here, is Wakefern's version.
			jsonResponse = this.getItemLocations(jsonResponse, storeID);

			if (LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					String trackData = LogUtil.getRequestData("chainID", chainID, "storeID", storeID, "listID", listID, "userID", userID,
							"skip", skip, "take", take, "filters", fd, "categoryMap", catMap,
							"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}

			return this.createValidResponse(jsonResponse);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.SHOPPING_LISTS_GET_LIST_ITEMS);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainID", chainID,
					"storeID", storeID, "listID", listID, "userID", userID,
					"skip", skip, "take", take, "filters", fd, "categoryMap", catMap,
					"sessionToken", sessionToken, "accept", accept, "contentType", contentType);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}


}

