package com.wakefern.coupons.v2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Created by loicao on 10/15/18.
 * Edited by philmayer on 2/27/19.
 */
@Path(ApplicationConstants.Requests.CouponsV2.CouponIDListByPPC_SEC)
public class GetCouponIDListByPPC_SEC extends BaseService {

	private final static Logger logger = Logger.getLogger(GetCouponIDListByPPC_SEC.class);

	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn,
			String jsonString) throws Exception, IOException {

		// If the application is in Fresh Grocer mode, we need to use a different coupon API endpoint.
		String appMode = MWGApplicationConstants.getApplicationMode();
		String targetCouponEndpoint = appMode.equals(WakefernApplicationConstants.Chains.FreshGrocer)
			? ApplicationConstants.Requests.CouponsV2.CouponIDListByPPC_SEC_FG
			: ApplicationConstants.Requests.CouponsV2.CouponIDListByPPC_SEC;

		//Execute POST
		this.requestPath = ApplicationUtils.constructCouponUrl(targetCouponEndpoint, fsn).toString();
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

		try {
			String response = HTTPRequest.executePostJSON(this.requestPath, jsonString, headerMap, 0);
			return this.createValidResponse(response);
		} catch (Exception e){
			String errorData = LogUtil.getRequestData("GetCouponIDListByPPC_SEC::Exception", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
