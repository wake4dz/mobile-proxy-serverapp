package com.wakefern.shop;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Shop.prefix)
public class GetEntryResourcesByStore extends BaseService {

	private final static Logger logger = Logger.getLogger(GetEntryResourcesByStore.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetEntryResourcesByStore() {
		this.requestPath = MWGApplicationConstants.Requests.Shop.prefix
				+ MWGApplicationConstants.Requests.Shop.entryByStore;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Shop.entryByStore)
	public Response getResponse(@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
		try {
			String jsonResponse = this.activateMWGShopEntry(userID, storeID, sessionToken);
			
			if (LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					String trackData = LogUtil.getRequestData("storeId", storeID, "userID", userID, "sessionToken",
							sessionToken, "accept", accept, "contentType", contentType);
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}

			return this.createValidResponse(jsonResponse);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.SHOP_GET_ENTRY_RESOURCES_BY_STORE);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId",
					storeID, "userID", userID, "sessionToken", sessionToken, "accept", accept, "contentType",
					contentType);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
	
	public String getInfo(String userID, String storeID, String sessionToken) throws IOException, Exception {
		return this.activateMWGShopEntry(userID, storeID, sessionToken);
	}
	
	private String activateMWGShopEntry(String userID, String storeID, String sessionToken) throws IOException, Exception {
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Shop.entry,
				MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();

		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		
		return this.mwgRequest(BaseService.ReqType.GET, null,
				"com.wakefern.shop.GetEntryResourcesByStore");
	}
}
