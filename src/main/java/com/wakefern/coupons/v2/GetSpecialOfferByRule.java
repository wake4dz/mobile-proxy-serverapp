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
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Description: get employee, social or store coupon offers
 * 
 * Created by Danny Zheng on 8/18/2020
 */
@Path(ApplicationConstants.Requests.CouponsV2.GetSpecialOfferByRule)
public class GetSpecialOfferByRule extends BaseService {
	private final static Logger logger = Logger.getLogger(GetSpecialOfferByRule.class);

	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn,
			@QueryParam(ApplicationConstants.Requests.CouponsV2.rule) String rule, String jsonString)
			throws Exception, IOException {
		
		StringBuilder sb = ApplicationUtils
				.constructCouponUrl(ApplicationConstants.Requests.CouponsV2.GetSpecialOfferByRule, fsn);
		sb.append(WakefernApplicationConstants.CouponsV2.QueryParam.RuleParam);
		sb.append(rule);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

		try {
			String response = HTTPRequest.executePostJSON(sb.toString(), jsonString, headerMap,
					VcapProcessor.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			String errorData = LogUtil.getRequestData("GetSpecialOfferByRule::Exception",
					LogUtil.getRelevantStackTrace(e), "fsn", fsn, "rule", rule);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
