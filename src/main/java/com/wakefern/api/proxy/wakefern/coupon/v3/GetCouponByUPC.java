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
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Get coupons associated with a upc code for a user
 */
@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Requests.CouponsV3.GetCouponByUPC)
public class GetCouponByUPC extends BaseService {
	private final static Logger logger = Logger.getLogger(GetCouponByUPC.class);

	@GET
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@PathParam(ApplicationConstants.Requests.CouponsV3.upc) String upc)
			throws Exception
	{
		Map<String, String> params = new HashMap<>();
		params.put("upcCode", upc);
		final String url = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.GetCouponsByUPC, params);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

		try {
			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiLowTimeout());//No match coupon found for the UPC
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_COUPONS_V3_GET_COUPON_BY_UPC);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"upc", upc,
					"authorization", authToken,
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
