package com.wakefern.api.citrus;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path(WakefernApplicationConstants.CitrusAds.AdsPath)
public class GenerateAds extends BaseService {
	private final static Logger logger = Logger.getLogger(GenerateAds.class);
	private final static String catalogId = VcapProcessor.getCitrusCatalogId();
	private final static String contentStandardId = VcapProcessor.getCitrusContentStandardId();
	private final static String requestURL = VcapProcessor.getCitrusServiceEndpoint()
			+ WakefernApplicationConstants.CitrusAds.Proxy.GetAds;
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
	public Response getResponse(String jsonString) {
		try {
			// add environment fields to Citrus upstream request
			JSONObject requestJSON = new JSONObject(jsonString);
			requestJSON.put("catalogId", catalogId);
			requestJSON.put("contentStandardId", contentStandardId);

			String response = HTTPRequest.executePostJSON(requestURL, requestJSON.toString(), headerMap, timeout);
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.CITRUS_GENERATE_ADS);
			String errorData = LogUtil.getRequestData("GenerateAds$getResponse::Exception",
					LogUtil.getRelevantStackTrace(e), "body", jsonString);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
