package com.wakefern.api.proxy.wakefern.coupon.v3;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Get a coupon associated with a promo code for an authenticated user
 */
@Path(ApplicationConstants.Requests.Proxy + CouponUtils.Requests.Routes.GetCouponByPromoCode)
public class GetCouponByPromoCode extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetCouponByPromoCode.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@PathParam("promoCode") String promoCode)
	{
		try {
			Map<String, String> params = new HashMap<>();
			params.put("promoCode", promoCode);

			final String url = CouponUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.GetCouponByPromoCode, params);
			Map<String, String> headerMap = new HashMap<>();

			headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
			headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			
			if (LogUtil.isLoggable(e)) {
				String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
						"promoCode", promoCode,
						"authorization", authToken,
						"contentType", contentType);
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}
			
			return this.createErrorResponse(e);
		}
	}
}
