package com.wakefern.checkout.orders;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.account.authentication.UserJWT;
import com.wakefern.apim.smsenrollment.SmsEnrollment;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.services.MI9TimeoutService;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class CreateOrder extends BaseService {

	private final static Logger logger = Logger.getLogger(CreateOrder.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public CreateOrder() {
		this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix
				+ MWGApplicationConstants.Requests.Checkout.orders;
	}

	/**
	 * Create and place an order with the ability to subscribe to SMS
	 * 
	 * Subscribe to SMS notifications for the created order if a phone number is
	 * submitted.
	 */
	@POST
	@Consumes("application/vnd.wakefern.placeorder.v1+json")
	@Produces(MWGApplicationConstants.Headers.json)
	@Path(MWGApplicationConstants.Requests.Checkout.orders)
	public Response createOrderWithSmsSubscription(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, String jsonString) {

		String storeNumber, phoneNumber, wakefernPpcToken, frequentShopperNumber;

		try {
			JSONObject requestBody = new JSONObject(jsonString);
			storeNumber = requestBody.getString("storeNumber");
			phoneNumber = requestBody.getString("phoneNumber");
			wakefernPpcToken = requestBody.getString("wakefernPpcToken");
		} catch (Exception e) {
			return this.createErrorResponse(new Exception("400,Bad Request"));
		}

		Response orderResponse = this.placeOrder(mwgStoreID, userID, accept, contentType, sessionToken);

		if (orderResponse.getStatus() != 200) {
			// error response
			return orderResponse;
		}

		JSONObject responseBody = new JSONObject(orderResponse.getEntity().toString());
		String orderNumber = Integer.toString(responseBody.getInt("OrderId"));

		try {
			frequentShopperNumber = UserJWT.getPpcFromToken(wakefernPpcToken);
		} catch (Exception e) {
			// accept that a user may not have an fsn on their profile or their ppc
			// token is invalid or missing a ppc
			frequentShopperNumber = "";
		}

		Boolean enrollmentSuccessful = SmsEnrollment.createOrderSmsSubscription(storeNumber, orderNumber, phoneNumber,
				frequentShopperNumber);

		// add additional property to order response to indicate sms subscription status
		responseBody.put("WakefernSmsSubscriptionCreated", enrollmentSuccessful);

		return this.createValidResponse(responseBody.toString());
	}

	/**
	 * Create and place an order. Replaced by create order with SMS subscription.
	 * 
	 * @deprecated
	 * @see #createOrderWithSmsSubscription()
	 */
	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	@Path(MWGApplicationConstants.Requests.Checkout.orders)
	public Response createOrder(@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, String jsonString) {
		return this.placeOrder(mwgStoreID, userID, accept, contentType, sessionToken);
	}

	/**
	 * Make upstream request to create and place an order
	 */
	private Response placeOrder(String mwgStoreID, String userID, String accept, String contentType,
			String sessionToken) {

		long startTime, endTime, actualTime;
		startTime = System.currentTimeMillis();

		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json,
					MWGApplicationConstants.Headers.Checkout.orders, sessionToken);
			this.requestParams = new HashMap<String, String>();

			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);

			String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, null,
					MI9TimeoutService.CHECKOUT_CREATE_ORDER,
					MI9TimeoutService.getTimeout(MI9TimeoutService.CHECKOUT_CREATE_ORDER));

			endTime = System.currentTimeMillis();

			String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, "sessionToken",
					sessionToken, "accept", accept, "contentType", contentType);

			actualTime = endTime - startTime;

			if (actualTime > MwgApiWarnTime.ORDERS_CREATE_ORDER.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is "
						+ MwgApiWarnTime.ORDERS_CREATE_ORDER.getWarnTime() + " ms. The track data: " + trackData);
			}

			// On 9/24/2018, per Loi's request, we log the log below for every order in
			// addition to the userTrackOn feature
			logger.info("[CreateOrders::getResponse]::processing time-" + (endTime - startTime) + " - " + mwgStoreID
					+ " - " + jsonResponse);

			if (LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}

			return this.createValidResponse(jsonResponse);
		} catch (Exception e) {
			endTime = System.currentTimeMillis();
			LogUtil.addErrorMaps(e, MwgErrorType.ORDERS_CREATE_ORDER);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"mwgStoreID", mwgStoreID, "processTime", (endTime - startTime), "userID", userID, "sessionToken",
					sessionToken);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}
}
