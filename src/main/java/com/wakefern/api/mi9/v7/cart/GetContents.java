package com.wakefern.api.mi9.v7.cart;

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

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MI9TimeoutRegistry;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(MWGApplicationConstants.Requests.Cart.prefix)
public class GetContents extends BaseService {

	private final static Logger logger = Logger.getLogger(GetContents.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetContents() {
		this.requestPath = MWGApplicationConstants.Requests.Cart.prefix + MWGApplicationConstants.Requests.Cart.cart;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Cart.cart)
	public Response getResponse(@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,

			@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String wfStoreID,
			@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.itemLocator) String itemLocator,
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.reservedTimeslot) String reservedTimeslot) {
		long startTime, endTime, actualTime;

		try {
			itemLocator = itemLocator.trim();
			this.requestHeader = new MWGHeader(accept, contentType, sessionToken, reservedTimeslot);
			this.requestParams = new HashMap<String, String>();

			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);

			String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "wfStoreID", wfStoreID, "userID",
					userID, "itemLocator", itemLocator, "sessionToken", sessionToken, "accept", accept, "contentType",
					contentType, "reservedTimeslot", reservedTimeslot);

			startTime = System.currentTimeMillis();
			String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutRegistry.CART_GET_CONTENTS, MI9TimeoutRegistry.getTimeout(MI9TimeoutRegistry.CART_GET_CONTENTS));

			logger.trace("mi9 jsonResponse: " + jsonResponse );
			
			// for warning any API call time exceeding its own upper limited time defined in
			// mwgApiWarnTime.java
			endTime = System.currentTimeMillis();
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.CART_GET_CONTENTS.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is "
						+ MwgApiWarnTime.CART_GET_CONTENTS.getWarnTime() + " ms. The track data: " + trackData);
			}
			// invoking item locator service if 'true' value returned from req parameter &
			// vcap env variable
			if (!itemLocator.isEmpty() && !itemLocator.equalsIgnoreCase("log")) {
				logger.trace("Calling Item Locator..");
				jsonResponse = this.getItemLocations(jsonResponse, wfStoreID);
				logger.trace("item locator jsonResponse: " + jsonResponse );
			}

			if (LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}

			return this.createValidResponse(jsonResponse);

		} catch (Exception e) {

			LogUtil.addErrorMaps(e, MwgErrorType.CART_GET_CONTENTS);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"mwgStoreID", mwgStoreID, "userId", userID, "wfStoreID", wfStoreID, "itemLocator", itemLocator,
					"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "reservedTimeslot", reservedTimeslot);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
