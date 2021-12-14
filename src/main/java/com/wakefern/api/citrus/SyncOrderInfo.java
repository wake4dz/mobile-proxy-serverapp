package com.wakefern.api.citrus;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/*
 * Author: Danny Zheng
 * Date:   2/10/2021
 * Release:3.26.0
 * JIRA:   https://wakefern.atlassian.net/browse/DMAU-1470
 * Desc:   The final phase of the Citrus Ad integration, send all order info to Citrus
 */
@Path(WakefernApplicationConstants.CitrusAds.Path)
public class SyncOrderInfo extends BaseService {
	private final static Logger logger = LogManager.getLogger(SyncOrderInfo.class);

	private final static String requestURL = VcapProcessor.getCitrusServiceEndpoint()
			+ WakefernApplicationConstants.CitrusAds.Proxy.SyncOrderInfo;
	private final static Map<String, String> headerMap = new HashMap<String, String>();
	private final static int timeout = VcapProcessor.getApiLowTimeout();

	static {
		headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		headerMap.put(ApplicationConstants.Requests.Header.contentAccept, MWGApplicationConstants.Headers.json);
		headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization,
				"Basic " + VcapProcessor.getCitrusApiKey());
	}

	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	@Path(WakefernApplicationConstants.CitrusAds.SyncOrderInfo)
	public Response syncOrder(String jsonString) {
		try {
			String response = HTTPRequest.executePostJSON(requestURL, jsonString, headerMap, timeout);
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.CITRUS_SYNC_ORDER_INFO);
			String errorData = LogUtil.getRequestData("ExceptionLocation",
					LogUtil.getRelevantStackTrace(e), "body", jsonString);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
