package com.wakefern.coupons.v2;

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
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.json.JSONException;
import org.json.JSONObject;

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
			@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion,
			String jsonString) {

		StringBuilder sb = new StringBuilder();
		sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURLAuth);
		sb.append(ApplicationConstants.Requests.CouponsV2.UserLogin);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, 
				MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.COUPON_V2_KEY));

		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(jsonString);
		} catch (JSONException ex) {
			logger.error("ObtainUserSession::Exception -> Error creating json object from request payload: "
					+ ex.getMessage());
			jsonObject = new JSONObject();
		}
		try {
			// Log payload for debugging purposes.
			logger.info("ObtainUserSession appVersion: " + appVersion
					+ " payload: " + jsonString
					+ " fsn: " + jsonObject.optString("fsn")
					+ " email: " + jsonObject.optString("email"));
			// Execute PUT
			String response = HTTPRequest.executePut(sb.toString(), jsonString, headerMap);
			return this.createValidResponse(response);
		} catch (Exception e){
			String errorData = LogUtil.getRequestData("ObtainUserSession::Exception", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
	
	public String getCouponV2Token(String jsonData) {

		StringBuilder sb = new StringBuilder();
		sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURLAuth);
		sb.append(ApplicationConstants.Requests.CouponsV2.UserLogin);

		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, 
				MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.COUPON_V2_KEY));
		String response = "";
		try{
			response = HTTPRequest.executePut(sb.toString(), jsonData, headerMap);
		} catch(Exception e){
			response = "{\"error\":\"fail to get token\"}";
		}
		return response;
		
	}
}
