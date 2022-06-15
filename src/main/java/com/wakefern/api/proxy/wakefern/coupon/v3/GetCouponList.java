package com.wakefern.api.proxy.wakefern.coupon.v3;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.wynshop.WynshopApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Get a list of coupons for a user that are clipped,
 * redeemed or available to be clipped.
 * If an unauthenticated token is passed,
 * the returned list cannot be clipped/unclipped.
 */
@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Requests.CouponsV3.CouponList)
public class GetCouponList extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetCouponList.class);

	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@QueryParam(ApplicationConstants.Requests.CouponsV3.storeId) String storeId)
	{
		Map<String, String> queryParams = new HashMap<>();

		if (storeId != null) {
			queryParams.put(ApplicationConstants.Requests.CouponsV3.storeId, storeId);
		}

		final String url = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.CouponsList, queryParams);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);

		try {
			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiHighTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_COUPONS_V3_GET_COUPON_LIST);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"authorization", authToken,
					"contentType", contentType,
					"storeId", storeId);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return createErrorResponse(e);
		}
	}
}
