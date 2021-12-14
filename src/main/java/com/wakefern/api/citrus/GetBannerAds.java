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
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(WakefernApplicationConstants.CitrusAds.Path)
public class GetBannerAds extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetBannerAds.class);
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
	public Response getAds(String jsonString) {
		try {
			// add environment fields to Citrus upstream request
			JSONObject requestJSON = new JSONObject(jsonString);
			requestJSON.put("catalogId", catalogId);
			requestJSON.put("contentStandardId", contentStandardId);

			String response = HTTPRequest.executePostJSON(requestURL, requestJSON.toString(), headerMap, timeout);
			JSONObject responseJSON = new JSONObject(response);

			// extract banners array for client
			return this.createValidResponse(responseJSON.getJSONArray("banners").toString());
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.CITRUS_GET_BANNER_ADS);
			String errorData = LogUtil.getRequestData("GetBannerAds$getAds::Exception",
					LogUtil.getRelevantStackTrace(e), "body", jsonString);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
}
