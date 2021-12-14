package com.wakefern.api.wakefern.receiptUserSettings;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Endpoint to update the user's "Digital Receipts" settings.
 */
@Path(WakefernApplicationConstants.ReceiptUserSettings.Proxy.Path)
public class UpdateReceiptSettings extends BaseService {
	private final static Logger logger = LogManager.getLogger(UpdateReceiptSettings.class);

	private final static String REQUEST_BODY_RECEIPT_OPTION_KEY = "digitalReceiptOption";
	private final static String REQUEST_BODY_RECEIPT_EMAIL_OPTION_KEY = "digitalReceiptEmailOption";
	private final static String REQUEST_BODY_YES_VALUE = "Y";
	private final static String REQUEST_BODY_NO_VALUE = "N";

	@PUT
	@ValidatePPC
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	public Response getReceiptSettings(@PathParam("userId") String userId, @PathParam("ppc") String ppc, String body) {
		try {
			final String apiKey = MWGApplicationConstants.getProductRecmdAuthToken();
			HashMap<String, String> headers = new HashMap<>();

			String path = WakefernApplicationConstants.ReceiptUserSettings.Upstream.BaseURL
					+ WakefernApplicationConstants.ReceiptUserSettings.Upstream.UpdatePath + ppc;

			headers.put(ApplicationConstants.Requests.Header.contentType, MediaType.APPLICATION_JSON);
			headers.put(ApplicationConstants.Requests.Header.contentAuthorization, apiKey);

			JSONObject jsonObject;
			boolean validated;
			try {
				jsonObject = new JSONObject(body);
				// Check if the request body is valid (has PPC number)
				validated = isValidRequestBody(jsonObject);
			} catch (JSONException ex) {
				logger.error(UpdateReceiptSettings.class.getName()
						+ "::Exception -> JSONException creating json object from request payload: "
						+ ex.getMessage());
				throw new Exception("Invalid request body");
			}

			if (!validated) {
				logger.error(UpdateReceiptSettings.class.getName()
						+ "::Exception -> Invalid json object from request payload: "
						+ body);
				throw new Exception("Invalid request body");
			}

			String response = HTTPRequest.executePut(path, jsonObject.toString(),
					headers, VcapProcessor.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.RECEIPT_UPDATE_USER_SETTINGS);
			String errorData = LogUtil.getRequestData("exceptionLocation",
					LogUtil.getRelevantStackTrace(e), "ppc", ppc);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}

	/**
	 * Validate the request payload.
	 * @param jsonObject
	 * @return boolean
	 */
	private static boolean isValidRequestBody (JSONObject jsonObject) {
		return jsonObject != null
				&& jsonObject.has(REQUEST_BODY_RECEIPT_EMAIL_OPTION_KEY)
				&& jsonObject.has(REQUEST_BODY_RECEIPT_OPTION_KEY)
				&& isValidPropValue(jsonObject.getString(REQUEST_BODY_RECEIPT_OPTION_KEY))
				&& isValidPropValue(jsonObject.getString(REQUEST_BODY_RECEIPT_EMAIL_OPTION_KEY));
	}

	private static boolean isValidPropValue(final String value) {
		return value != null
				&& (value.contentEquals(REQUEST_BODY_YES_VALUE) || value.contentEquals(REQUEST_BODY_NO_VALUE));
	}
}



