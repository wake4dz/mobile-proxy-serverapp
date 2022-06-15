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
 * Get a coupon details by its id
 */
@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Requests.CouponsV3.GetCouponsByStoreId)
public class GetCouponsByStoreId extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetCouponsByStoreId.class);

	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			@PathParam("storeId") String storeId)
			throws Exception
	{
		Map<String, String> params = new HashMap<>();
		params.put("storeId", storeId);
		this.requestPath = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.GetCouponListByStoreId, params);
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);

		try {
			String response = HTTPRequest.executeGet(this.requestPath, headerMap, VcapProcessor.getApiHighTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_COUPONS_V3_GET_COUPONS_BY_STORE_ID);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"storeId", storeId,
					"authorization", authToken,
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
