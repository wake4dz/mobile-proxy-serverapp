package com.wakefern.api.proxy.wakefern.coupon.banner;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
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
 * Add a coupon to a PPC card
 */
@Path(ApplicationConstants.Requests.Proxy + CouponUtils.Requests.BannerRoutes.AddCouponToPPC)
public class AddCouponToPPC extends BaseService {
	private final static Logger logger = LogManager.getLogger(AddCouponToPPC.class);

	@POST
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@PathParam(CouponUtils.Requests.Params.banner) String banner,
			String jsonString)
			throws Exception
	{
		final String url = CouponUtils.constructCouponBannerUrl(WakefernApplicationConstants.CouponsV3.PathInfo.AddCouponToPPC, banner);

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
		headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

		try {
			// Execute POST
			String response = HTTPRequest.executePostJSON(url, jsonString, headerMap, EnvManager.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			
			parseAndLogException(logger, e, 
					CouponUtils.Requests.Params.banner, banner,
					ApplicationConstants.Requests.Headers.Authorization, authToken,
					ApplicationConstants.Requests.Headers.contentType, contentType);
			
			return this.createErrorResponse(e);
		}
	}
}
