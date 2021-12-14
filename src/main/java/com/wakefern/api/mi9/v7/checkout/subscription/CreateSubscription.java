package com.wakefern.api.mi9.v7.checkout.subscription;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Create a subscription with a program id which is from GetSubscriptions() API.
 * Special note: this API can't work alone in Postman,
 *               the session token has to be associated with a timeslot first in the checkout process
 *               because this API needs to be associated with a fulfillment type either Pickup or Delivery
 *               https://mi9retail.atlassian.net/browse/WFD-27351
 */

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class CreateSubscription extends BaseService {

	private final static Logger logger = LogManager.getLogger(CreateSubscription.class);

	private static final String TAG = CreateSubscription.class.getName();

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public CreateSubscription() {
		this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix
				+ MWGApplicationConstants.Requests.Checkout.createSubscription;
	}

	@POST
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Checkout.createSubscription)
	public Response getResponse(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userId,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeId,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.subscriptionID) String subscriptionId,
			                                                                             
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
		
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<>();

			// Path params
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userId);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeId);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.subscriptionID, subscriptionId);

			String jsonResponse = this.mwgRequest(ReqType.POST, null, TAG);
			return this.createValidResponse(jsonResponse);
			
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.SUBSCRIPTION_CREATE_SUBSCRIPTION);

			String errorData = LogUtil.getRequestData("exceptionLocation",
					LogUtil.getRelevantStackTrace(e),
					"storeId", storeId,
					"userId", userId,
					"subscriptionId", subscriptionId,
					"sessionToken", sessionToken);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}
}
