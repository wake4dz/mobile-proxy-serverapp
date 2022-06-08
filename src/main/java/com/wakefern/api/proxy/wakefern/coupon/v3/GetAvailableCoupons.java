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

@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Requests.CouponsV3.AvailableCoupons)
public class GetAvailableCoupons extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetAvailableCoupons.class);

	@GET
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@QueryParam(ApplicationConstants.Requests.CouponsV3.storeId) String storeId)
	{
		Map<String, String> queryParams = new HashMap<>();

		if (storeId != null) {
			queryParams.put(ApplicationConstants.Requests.CouponsV3.storeId, storeId);
		}

		final String url = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.AvailableCoupons, queryParams);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

		try {
			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiHighTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_COUPONS_V3_GET_AVAILABLE_COUPONS);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"authorization", authToken,
					"contentType", contentType,
					"storeId", storeId);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return createErrorResponse(e);
		}
	}
}
