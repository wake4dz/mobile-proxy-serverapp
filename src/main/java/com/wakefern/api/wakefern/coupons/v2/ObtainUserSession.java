package com.wakefern.api.wakefern.coupons.v2;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.json.JSONException;

/**
 * Created by loicao on 10/11/18.
 * Edited by matt-miller7 on 2/20/20.
 */
@Path(ApplicationConstants.Requests.CouponsV2.UserLogin)
public class ObtainUserSession extends BaseService {
	private final static Logger logger = LogManager.getLogger(ObtainUserSession.class);

	// TODO: refactor response building to not be parsing comma separated strings.
	private static final String badRequestMessage = "400,Premature rejection,Bad Request: Missing frequent shopper number.";

	private static final String fsnKey = "fsn";

	@PUT
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion,
			String body) {

		// Coupon server response if no "fsn" field in json data.
		StringBuilder sb = new StringBuilder();
		sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURLAuth);
		sb.append(ApplicationConstants.Requests.CouponsV2.UserLogin);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization,
		MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.COUPON_V2_KEY));
		JSONObject jsonObject;
		boolean validated;
		try {
			try {
				jsonObject = new JSONObject(body);
				// Check if the request body is valid (has PPC number)
				validated = isValidRequestBody(jsonObject);
			} catch (JSONException ex) {
				logger.error("ObtainUserSession::Exception -> JSONException creating json object from request payload: "
						+ ex.getMessage());
				throw new Exception(badRequestMessage);
			}

			// Execute PUT
			String response = HTTPRequest.executePut(sb.toString(), body, headerMap);

			if (!validated) {
				// Log payload for debugging purposes.
				logger.info("ObtainUserSession::invalid request: appVersion: " + appVersion
						+ " payload: " + body
						+ " fsn: " + jsonObject.optString("fsn")
						+ " email: " + jsonObject.optString("email"));
				throw new Exception(badRequestMessage);
			}

			return this.createValidResponse(response);
		} catch (Exception e){
			LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_V2_OBTAIN_USER_SESSION);
			
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}

	/**
	 * Validate the request body
	 * @param  jsonObject JSONObject
	 * @return boolean True if the request body is valid, false otherwise.
	 */
	private static boolean isValidRequestBody(JSONObject jsonBody){
		return jsonBody != null && !jsonBody.optString(fsnKey).trim().isEmpty();
	}
	
	public String getCouponV2Token(String jsonData){
		StringBuilder sb = new StringBuilder();
		sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURLAuth);
		sb.append(ApplicationConstants.Requests.CouponsV2.UserLogin);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization,
				MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.COUPON_V2_KEY));
		String response = "";
		try{
			response = HTTPRequest.executePut(sb.toString(), jsonData, headerMap);
		} catch(Exception e){
			response = "{\"error\":\"fail to get token\"}";
		}
		return response;
	}
}
