package com.wakefern.api.proxy.wakefern.coupon.v3;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Get a coupon associated with a promo code for an authenticated user
 */
@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Requests.CouponsV3.GetCouponByPromoCode)
public class GetCouponByPromoCode extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetCouponByPromoCode.class);

	@GET
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@PathParam("promoCode") String promoCode)
	{
		try {
			Map<String, String> params = new HashMap<>();
			params.put("promoCode", promoCode);

			final String url = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.GetCouponByPromoCode, params);
			Map<String, String> headerMap = new HashMap<String, String>();

			headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
			headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_COUPONS_V3_GET_COUPON_FROM_PROMOCODE);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"promoCode", promoCode,
					"authorization", authToken,
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
