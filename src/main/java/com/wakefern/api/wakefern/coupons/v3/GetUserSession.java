package com.wakefern.api.wakefern.coupons.v3;

import com.wakefern.api.mi9.v7.account.authentication.UserJWT;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
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
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path(ApplicationConstants.Requests.CouponsV3.UserLogin)
public class GetUserSession extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetUserSession.class);

	// TODO: refactor response building to not be parsing comma separated strings.
	private static final String badRequestMessage = "400,Premature rejection,Bad Request: Missing frequent shopper number.";
	private static final String unauthorizedMessage = "401,Unauthorized";

	private static final String fsnKey = "ppc";

	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			String body)
	{
		final String url = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.UserLogin);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization,
				MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.COUPON_V3_KEY));

		JSONObject jsonObject;
		boolean containsPPC;
		try {
			try {
				jsonObject = new JSONObject(body);
				containsPPC = bodyContainsPPC(jsonObject);
			} catch (JSONException ex) {
				logger.error("ObtainUserSession::Exception -> JSONException creating json object from request payload: "
						+ ex.getMessage());
				throw new Exception(badRequestMessage);
			}

			if (containsPPC) {
				final String ppc = jsonObject.optString("ppc");
				validatePPCWithJWT(ppc, authToken);
			}

			String response = HTTPRequest.executePost(url, body, headerMap);
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_V3_GET_USER_SESSION);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"body", body,
					"authorization", authToken,
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}

	private static boolean bodyContainsPPC(JSONObject jsonBody) {
		return jsonBody != null && !jsonBody.optString(fsnKey).trim().isEmpty();
	}

	private static boolean validatePPCWithJWT(final String ppc, final String jwtToken) throws Exception {
		if (!UserJWT.isValid(jwtToken, ppc)) {
			throw new Exception(unauthorizedMessage);
		} else {
			logger.debug("Validate PPC passed for ppc: " + ppc);
		}
		return true;
	}
}
