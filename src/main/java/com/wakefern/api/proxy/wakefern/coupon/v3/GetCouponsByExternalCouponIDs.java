package com.wakefern.api.proxy.wakefern.coupon.v3;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
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
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Get a coupon details by its id
 * 
 * not used as of 2023-03-21
 */
@Path(ApplicationConstants.Requests.Proxy + CouponUtils.Requests.Routes.GetCouponByExternalCouponIds)
public class GetCouponsByExternalCouponIDs extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetCouponsByExternalCouponIDs.class);

	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@PathParam(CouponUtils.Requests.Params.externalCouponIds) String externalCouponIds)
			throws Exception
	{
		Map<String, String> params = new HashMap<>();
		params.put("couponIds", externalCouponIds);
		this.requestPath = CouponUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.GetCouponByCouponId, params, null);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
		headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

		try {
			String response = HTTPRequest.executeGet(this.requestPath, headerMap, EnvManager.getApiHighTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			if (LogUtil.isLoggable(e)) {
				String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
						"externalCouponIds", externalCouponIds,
						"authorization", authToken,
						"contentType", contentType);
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}
			
			return this.createErrorResponse(e);
		}
	}
}
