package com.wakefern.shoppingLists;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.services.MI9TimeoutService;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)
public class GetSharedListItems extends BaseService {

	private final static Logger logger = Logger.getLogger(GetSharedListItems.class);

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetSharedListItems() {
		this.requestPath = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.ShoppingList.wakefernItems)
	@Produces(MWGApplicationConstants.Headers.ShoppingList.wakefernItems)
	@Path(MWGApplicationConstants.Requests.ShoppingList.items)
	public Response getResponse(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.listID) String listID,

			@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
			@QueryParam("externalStoreId") String externalStoreID,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String fd,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.categoryMap) String catMap, // Sort of like a "cat nap", but not really.  :-)

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {

		this.requestParams = new HashMap<>();
		this.queryParams = new HashMap<>();
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.wakefernItems,
				MWGApplicationConstants.Headers.generic, sessionToken);

		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.categoryMap, catMap);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeID, storeID);

		if (!fd.isEmpty()) {
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, fd);
		}

		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);

		try {
			String jsonResponse = mwgRequest(ReqType.GET, null,
					MI9TimeoutService.SHOPPINGLIST_GET_SHARED_LIST_ITEMS,
					MI9TimeoutService.getTimeout(MI9TimeoutService.SHOPPINGLIST_GET_SHARED_LIST_ITEMS));

			// Item Location data provided by MWG is never up-to-date.
			// Used Wakefern-supplied Item Location data instead.
			// The Store ID being passed here, is Wakefern's version.
			jsonResponse = this.getItemLocations(jsonResponse, externalStoreID);

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
			LogUtil.addErrorMaps(e, MwgErrorType.SHOPPING_LISTS_GET_SHARED_LIST_ITEMS);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainID", chainID,
					"storeID", storeID, "listID", listID, "userID", userID,
					"skip", skip, "take", take, "filters", fd, "categoryMap", catMap,
					"sessionToken", sessionToken, "accept", accept, "contentType", contentType);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}

	public String getListItems(HashMap<String, String> queryParam, String chainID, String userID, String listID, String accept, String sessionToken) throws Exception{
		this.requestHeader = new MWGHeader(accept, MWGApplicationConstants.Headers.generic, sessionToken);
		this.requestParams = new HashMap<>();

		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);

		if(this.queryParams == null){
			this.queryParams = queryParam;
		}
		return this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutService.SHOPPINGLIST_GET_LIST_ITEMS, MI9TimeoutService.getTimeout(MI9TimeoutService.SHOPPINGLIST_GET_LIST_ITEMS));
	}


}

