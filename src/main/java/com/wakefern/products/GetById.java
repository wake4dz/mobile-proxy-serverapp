package com.wakefern.products;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetById extends BaseService {

	private final static Logger logger = Logger.getLogger(GetById.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetById() {
		this.requestPath = MWGApplicationConstants.Requests.Products.prefix
				+ MWGApplicationConstants.Requests.Products.prodByID;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Products.prodByID)
	public Response getResponse(@PathParam(MWGApplicationConstants.Requests.Params.Path.productID) String productID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.reservedTimeslot) String reservedTimeslot) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.product,
					MWGApplicationConstants.Headers.json, sessionToken, reservedTimeslot);
			this.requestParams = new HashMap<String, String>();
			this.queryParams = new HashMap<String, String>();

			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.productID, productID);

			// Build the Map of Query String parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);

			String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.GetById");

			return this.createValidResponse(jsonResponse);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_GET_BY_ID);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId",
					storeID, "productID", productID, "isMember", isMember, "sessionToken", sessionToken, "accept",
					accept, "contentType", contentType, "reservedTimeslot", reservedTimeslot);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
