package com.wakefern.api.wakefern.apim.smsenrollment;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy endpoints for SRFH SMS enrollments APIs deployed in APIM.
 */
@Path(WakefernApplicationConstants.SmsEnrollment.Proxy.Path)
public class SmsEnrollment extends BaseService {
	private final static Logger logger = Logger.getLogger(SmsEnrollment.class);

	// TODO: refactor response building to not be parsing comma separated strings.
	private static final String badRequestMessage = "400,Premature rejection,Bad Request: Malformed json.";

	@PathParam("userId")
	private String userId;

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path("/")
	@ValidatePPC
	public Response getEnrollmentInfo(@PathParam("ppc") String fsn, @QueryParam("phoneNumber") String phoneNumber) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		headerMap.put(ApplicationConstants.Requests.Header.contentAccept,
				WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
		headerMap.put(WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersionHeaderKey,
				WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersion);
		headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.APIM_SMS_ENROLLMENTS_KEY));
		try {
			URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
			builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.enrollmentPath)
					.setParameter("frequentShopperNumber", fsn).setParameter("phoneNumber", phoneNumber);
			final String requestURI = builder.build().toString();
			final String response = HTTPRequest.executeGet(requestURI, headerMap, VcapProcessor.getApiLowTimeout());
			logger.info("Response from apim: " + response);
			JSONObject responseJson = new JSONObject(response);
			JSONObject responseData = responseJson.getJSONObject("data");

			// Return the phone number payload
			return this.createValidResponse(responseData.toString());
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.APIM_SMS_ENROLLMENTS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}

	/**
	 * Global opt-in for SMS alerts for order updates and product substitutions.
	 * Replaced by order-based SMS subscription.
	 * 
	 * @deprecated
	 * @see com.wakefern.api.mi9.v7.checkout.orders.CreateOrder#createWithSmsSubscription
	 */
	@POST
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path("/")
	@ValidatePPC
	public Response upsertEnrollment(@PathParam("ppc") String fsn, String jsonBody) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		headerMap.put(WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersionHeaderKey,
				WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersion);
		headerMap.put(ApplicationConstants.Requests.Header.contentAccept,
				WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
		headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.APIM_SMS_ENROLLMENTS_KEY));
		try {
			JSONObject payload = new JSONObject();
			String phoneNumber;
			boolean isEnrolled;
			try {
				JSONObject body = new JSONObject(jsonBody);
				phoneNumber = body.getString("phoneNumber");
				isEnrolled = body.getBoolean("isEnrolled");
			} catch (JSONException ex) {
				logger.error(
						"SmsEnrollment$updateEnrollment::Exception -> JSONException creating json object from request payload: "
								+ ex.getMessage());
				throw new Exception(badRequestMessage);
			}
			payload.put("frequentShopperNumber", fsn);
			payload.put("phoneNumber", phoneNumber);
			payload.put("isEnrolled", isEnrolled);

			URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
			builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.enrollmentPath);
			final String requestURI = builder.build().toString();
			String response = HTTPRequest.executePostJSON(requestURI, payload.toString(), headerMap,
					VcapProcessor.getApiLowTimeout());
			logger.info("SmsEnrollment$updateEnrollment response:" + response);

			// return Http 202 Accepted
			return this.createResponse(202);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.APIM_SMS_ENROLLMENTS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}

	/**
	 * Create an SMS subscription for an order. Returns a boolean indicating if the
	 * subscription was successful.
	 */
	public static Boolean createOrderSmsSubscription(String storeNumber, String orderNumber, String phoneNumber,
			String frequentShopperNumber) {
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		headerMap.put(ApplicationConstants.Requests.Header.contentAccept,
				WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
		headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.APIM_SMS_ENROLLMENTS_KEY));

		JSONObject payload = new JSONObject();

		payload.put("storeNumber", storeNumber);
		payload.put("orderNumber", orderNumber);
		payload.put("phoneNumber", phoneNumber);
		payload.put("frequentShopperNumber", frequentShopperNumber);

		try {
			URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
			builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.orderSubscriptionPath);
			final String requestURI = builder.build().toString();
			HTTPRequest.executePostJSON(requestURI, payload.toString(), headerMap, VcapProcessor.getApiLowTimeout());
			return true;
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.APIM_SMS_ENROLLMENTS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return false;
		}
	}
}
