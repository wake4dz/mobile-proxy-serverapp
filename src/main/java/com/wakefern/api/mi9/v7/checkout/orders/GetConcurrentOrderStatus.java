package com.wakefern.api.mi9.v7.checkout.orders;

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
 * Endpoint to fetch the number of concurrent orders/max orders for a particular user.
 */
@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetConcurrentOrderStatus extends BaseService {

	private final static Logger logger = Logger.getLogger(GetConcurrentOrderStatus.class);

	private final static String TAG = GetConcurrentOrderStatus.class.getName();
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetConcurrentOrderStatus() {
		this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.concurrentOrders;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Checkout.concurrentOrders)
	public Response getResponse(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.concurrentOrders, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<>();

			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			String jsonResponse = this.mwgRequest(ReqType.GET, null, TAG);
			return this.createValidResponse(jsonResponse);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.ORDERS_GET_CONCURRENT_ORDER_STATUS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"mwgStoreID", mwgStoreID, "userID", userID, "sessionToken", sessionToken);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}


