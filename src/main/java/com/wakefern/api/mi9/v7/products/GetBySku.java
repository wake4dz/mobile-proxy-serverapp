package com.wakefern.api.mi9.v7.products;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetBySku extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetBySku.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetBySku() {
		this.requestPath = MWGApplicationConstants.Requests.Products.prefix
				+ MWGApplicationConstants.Requests.Products.prodBySKU;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Products.prodBySKU)
	public Response getResponse(@PathParam(MWGApplicationConstants.Requests.Params.Path.productSKU) String productSKU,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,

			@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String wfStoreID,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.reservedTimeslot) String reservedTimeslot) {
		long startTime, endTime, actualTime;

		try {
			String jsonResponse = makeRequest(mwgStoreID, productSKU, isMember, sessionToken, reservedTimeslot, accept);

			// Item Location data provided by MWG is never up-to-date.
			// Used Wakefern-supplied Item Location data instead.
			startTime = System.currentTimeMillis();
			jsonResponse = this.getItemLocations(jsonResponse, wfStoreID);

			// for warning any API call time exceeding its own upper limited time defined in
			// mwgApiWarnTime.java
			endTime = System.currentTimeMillis();
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.PRODUCTS_GET_BY_SKU.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is "
						+ MwgApiWarnTime.PRODUCTS_GET_BY_SKU.getWarnTime() + " ms.");
			}

			return this.createValidResponse(jsonResponse);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_GET_BY_SKU);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"mwgStoreID", mwgStoreID, "isMember", isMember, "wfStoreID", wfStoreID, "productSKU", productSKU,
					"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "reservedTimeslot",
					reservedTimeslot);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

	// -------------------------------------------------------------------------
	// Private Methods
	// -------------------------------------------------------------------------
	/**
	 * Trigger the request to the MWG API.
	 *
	 * @param mwgStoreID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	private String makeRequest(String mwgStoreID, String productSKU, String isMember, String sessionToken,
			String reservedTimeslot, String accept) throws Exception {
		final String mimeType = accept == null ?
				MWGApplicationConstants.Headers.Products.product : accept;

		this.requestHeader = new MWGHeader(mimeType,
				MWGApplicationConstants.Headers.json, sessionToken, reservedTimeslot);
		this.requestParams = new HashMap<>();
		this.queryParams = new HashMap<>();

		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.productSKU, productSKU);

		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);

		return this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.GetBySku");
	}
}
