package com.wakefern.api.wakefern.coupons.v3;

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

@Path(ApplicationConstants.Requests.CouponsV3.AvailableCoupons)
public class GetAvailableCoupons extends BaseService {
	private final static Logger logger = Logger.getLogger(GetAvailableCoupons.class);

	@GET
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType)
	{
		this.requestPath = ApplicationUtils.constructCouponV3Url(WakefernApplicationConstants.CouponsV3.PathInfo.AvailableCoupons);
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

		try {
			String response = HTTPRequest.executeGet(this.requestPath, headerMap, VcapProcessor.getApiHighTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_V3_GET_AVAILABLE_COUPONS);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"authorization", authToken,
					"contentType", contentType);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
