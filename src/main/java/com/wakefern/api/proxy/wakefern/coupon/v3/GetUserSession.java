package com.wakefern.api.proxy.wakefern.coupon.v3;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.UserJWTV2;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Requests.CouponsV3.UserLogin)
public class GetUserSession extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetUserSession.class);

	private static final String unauthorizedMessage = "401,Unauthorized";

	private static final String fsnKey = "ppc";

	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String bearerToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType)
	{
		final String url = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.UserLogin);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization,
				MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.COUPON_V3_KEY));

		JSONObject jsonObject;
		try {
			if (bearerToken == null) {
				/**
				 * In the event of not having the JWT token attached, authenticate as a guest with the Coupons V3 API.
				 * The user will be able to view coupons, but not clip them.
				 */
				return createValidResponse(HTTPRequest.executePost(url, (new JSONObject()).toString(), headerMap));
			}

			final String ppc = validateJWTAndParsePPC(bearerToken);
			logger.debug("PPC parsed from JWT: " + ppc);
			jsonObject = new JSONObject();
			jsonObject.put(fsnKey, ppc);

			return createValidResponse(HTTPRequest.executePost(url, jsonObject.toString(), headerMap));
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_COUPONS_V3_GET_USER_SESSION);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"authorization", bearerToken,
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}

	/**
	 * Validate JWT and return the PPC embedded in the JWT.
	 * @param jwtToken
	 * @return String
	 * @throws Exception
	 */
	private static String validateJWTAndParsePPC(final String jwtToken) throws Exception {
		if (!UserJWTV2.isValid(jwtToken)) {
			throw new Exception(unauthorizedMessage);
		}

		return UserJWTV2.getPpcFromToken(jwtToken);
	}
}
