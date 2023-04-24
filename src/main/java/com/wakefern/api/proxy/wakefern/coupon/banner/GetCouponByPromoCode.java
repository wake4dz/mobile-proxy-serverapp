package com.wakefern.api.proxy.wakefern.coupon.banner;

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
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Get a coupon associated with a promo code for an authenticated user
 */
@Path(ApplicationConstants.Requests.Proxy + CouponUtils.Requests.BannerRoutes.GetCouponByPromoCode)
public class GetCouponByPromoCode extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetCouponByPromoCode.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@PathParam(CouponUtils.Requests.Params.banner) String banner,
			@PathParam("promoCode") String promoCode)
	{
		try {
			Map<String, String> params = new HashMap<>();
			params.put("promoCode", promoCode);

			final String url = CouponUtils.constructCouponBannerUrl(WakefernApplicationConstants.CouponsV3.PathInfo.GetCouponByPromoCode, banner, params, null);
			Map<String, String> headerMap = new HashMap<>();

			headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
			headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
			headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
			
			String response = HTTPRequest.executeGet(url, headerMap, EnvManager.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			
			parseAndLogException(logger, e, 
					CouponUtils.Requests.Params.banner, banner,
					ApplicationConstants.Requests.Headers.Authorization, authToken,
					ApplicationConstants.Requests.Headers.contentType, contentType,
					"promoCode", promoCode);
			
			return this.createErrorResponse(e);
		}
	}
}
