package com.wakefern.api.mi9.v7.products.chains;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Fetch wakefern product by sku.
 */
@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetWakefernProductBySku extends BaseService {

	private final static Logger logger = Logger.getLogger(GetWakefernProductBySku.class);

	private static final String TAG = GetWakefernProductBySku.class.getName();

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetWakefernProductBySku() {
		this.requestPath = MWGApplicationConstants.Requests.Products.prefix
				+ MWGApplicationConstants.Requests.Products.wakefernProductBySku;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Products.wakefernProductBySku)
	public Response getResponse(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
			@PathParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
			@PathParam(MWGApplicationConstants.Requests.Params.Query.sku) String sku,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.wakefernProduct, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<>();
			this.queryParams = new HashMap<>();

			// Path params
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.productSKU, sku);

			String jsonResponse = this.mwgRequest(ReqType.GET, null, TAG);
			return this.createValidResponse(jsonResponse);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.CHAINS_GET_WAKEFERN_PRODUCT_BY_SKU);

			String errorData = LogUtil.getRequestData("exceptionLocation",
					LogUtil.getRelevantStackTrace(e),
					"storeId", storeID,
					"chainID", chainID,
					"sku", sku,
					"sessionToken", sessionToken,
					"accept", accept,
					"contentType", contentType);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}
}
