package com.wakefern.coupons.v2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by loicao on 10/11/18.
 * Edited by philmayer on 2/27/19.
 */
@Path(ApplicationConstants.Requests.CouponsV2.UserLogin)
public class ObtainUserSession extends BaseService {
	private final static Logger logger = Logger.getLogger(ObtainUserSession.class);

	@PUT
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(
			@HeaderParam("Authorization") String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			String jsonString) throws Exception, IOException {

		StringBuilder sb = new StringBuilder();
		sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURLAuth);
		sb.append(ApplicationConstants.Requests.CouponsV2.UserLogin);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, Requests.Tokens.couponV2Token);

		try {
			// Execute PUT
			String response = HTTPRequest.executePut(sb.toString(), jsonString, headerMap);
			return this.createValidResponse(response);
		} catch (Exception e){
			String errorData = LogUtil.getRequestData("ObtainUserSession::Exception", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
