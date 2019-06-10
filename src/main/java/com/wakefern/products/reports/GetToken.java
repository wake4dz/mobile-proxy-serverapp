package com.wakefern.products.reports;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Endpoint for obtaining a new token for the Wakefern "Product Not Found" API. Used in subsequent
 * calls to create or delete "Product Not Found" entries.
 *
 * @author philmayer
 */
@Path(ApplicationConstants.Requests.Reports.NotFoundTokenURL)
public class GetToken extends BaseService {

	private final static Logger logger = Logger.getLogger(GetToken.class);
	
	/**
	 * Build and send off the request for a new "Product Not Found" API token. Use Jackson to map
	 * the request body to JSON. When a token is retrieved, it is valid for 24 hours.
	 *
	 * @return Response - if successful, the body will contain a "token" property
	 */
	@GET
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAccept) String accept,
    		@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType
    ) {
		try {
			String authenticateUrl = WakefernApplicationConstants.getBaseWakefernApiUrl() + WakefernApplicationConstants.Reports.NotFound.authenticate;

			Map<String, String> headerMap = new HashMap<String, String>(1);
			headerMap.put(ApplicationConstants.Requests.Header.contentAccept, accept);
			headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);

			Map<String, String> requestBodyMap = new HashMap<>(2);
			requestBodyMap.put("UserName", WakefernApplicationConstants.Reports.NotFound.authUsername);
			requestBodyMap.put("Password", WakefernApplicationConstants.Reports.NotFound.authPassword);
			String requestBodyJson = new ObjectMapper().writeValueAsString(requestBodyMap);

			String responseBody = HTTPRequest.executePost(authenticateUrl, requestBodyJson, headerMap);
			return this.createValidResponse(responseBody);
		} catch (Exception exception) {
			String errorData = LogUtil.getRequestData("GetToken::Exception", LogUtil.getRelevantStackTrace(exception));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(exception));
			return this.createErrorResponse(exception);
		}
	}
}
