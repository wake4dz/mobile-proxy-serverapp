package com.wakefern.api.proxy.wakefern.coupon.v3;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Get a list of coupons for a user that are clipped,
 * redeemed or available to be clipped.
 * If an unauthenticated token is passed,
 * the returned list cannot be clipped/unclipped.
 * 
 * not used as of 2023-03-21
 */
@Path(ApplicationConstants.Requests.Proxy + CouponUtils.Requests.Routes.CouponList)
public class GetCouponList extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetCouponList.class);

	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@QueryParam(CouponUtils.Requests.Params.storeId) String storeId)
	{
		Map<String, String> queryParams = new HashMap<>();

		if (storeId != null) {
			queryParams.put(CouponUtils.Requests.Params.storeId, storeId);
		}

		final String url = CouponUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.CouponsList, null, queryParams);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
		headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

		try {
			String response = HTTPRequest.executeGet(url, headerMap, EnvManager.getApiHighTimeout());
			return createValidResponse(response);
		} catch (Exception e) {

			parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.Authorization, authToken,
					ApplicationConstants.Requests.Headers.contentType, contentType,
					"storeId", storeId);
			
			return createErrorResponse(e);
		}
	}
}
