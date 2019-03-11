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
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Created by loicao on 10/16/18.
 * Edited by philmayer on 2/27/19.
 */
@Path(ApplicationConstants.Requests.CouponsV2.RemoveCouponFromPPC)
public class RemoveCouponFromPPC extends BaseService {
	private final static Logger logger = Logger.getLogger(RemoveCouponFromPPC.class);

	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn,
			@QueryParam(ApplicationConstants.Requests.CouponsV2.coupon_id) String couponId,
			String jsonString) throws Exception, IOException {

		// If the application is in Fresh Grocer mode, we need to attribute a different clip source for analysis.
		String appMode = MWGApplicationConstants.getApplicationMode();
		String targetCouponEndpoint = appMode.equals(WakefernApplicationConstants.Chains.FreshGrocer)
				? ApplicationConstants.Requests.CouponsV2.RemoveCouponFromPPC_FG
				: ApplicationConstants.Requests.CouponsV2.RemoveCouponFromPPC;
		String clipSource = appMode.equals(WakefernApplicationConstants.Chains.FreshGrocer)
				? WakefernApplicationConstants.CouponsV2.ParamValues.ClipAppSource_FG
				: WakefernApplicationConstants.CouponsV2.ParamValues.ClipAppSource_SR;

		//Execute POST
		String url = ApplicationConstants.Requests.CouponsV2.BaseCouponURL + targetCouponEndpoint;

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
		headerMap.put(WakefernApplicationConstants.CouponsV2.Headers.clip_source, clipSource);
		headerMap.put(WakefernApplicationConstants.CouponsV2.Headers.coupon_id, couponId);
		headerMap.put(WakefernApplicationConstants.CouponsV2.Headers.fsn, fsn);

		try {
			String response = HTTPRequest.executePostJSON(url, jsonString, headerMap, 0);
			return this.createValidResponse(response);
		} catch (Exception e){
			String errorData = LogUtil.getRequestData("RemoveCouponFromPPC::Exception", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
