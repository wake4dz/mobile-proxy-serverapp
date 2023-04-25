package com.wakefern.api.proxy.wakefern.coupon.v3;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;

import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.global.UserJWTV2;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Proxy + CouponUtils.Requests.Routes.UserLogin)
public class GetUserSession extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetUserSession.class);

	private static final String unauthorizedMessage = "401,Unauthorized";

	private static final String fsnKey = "ppc";

	@POST
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String bearerToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType)
	{
		final String url = CouponUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.UserLogin);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Headers.Authorization, EnvManager.getCouponV3Key());
		headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

		JSONObject jsonObject;
		String ppc = null;
		
		try {
			if (bearerToken == null) {
				/**
				 * In the event of not having the JWT token attached, authenticate as a guest with the Coupons V3 API.
				 * The user will be able to view coupons, but not clip them.
				 */
				return createValidResponse(HTTPRequest.executePost(url, (new JSONObject()).toString(), headerMap));
			}

			ppc = validateJWTAndParsePPC(bearerToken);
			logger.debug("PPC parsed from JWT: " + ppc);
			jsonObject = new JSONObject();
			jsonObject.put(fsnKey, ppc);

			return createValidResponse(HTTPRequest.executePost(url, jsonObject.toString(), headerMap));
		} catch (Exception e) {

			parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.Authorization, EnvManager.getCouponV3Key(),
					ApplicationConstants.Requests.Headers.contentType, contentType,
					"ppc", ppc);
			
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
