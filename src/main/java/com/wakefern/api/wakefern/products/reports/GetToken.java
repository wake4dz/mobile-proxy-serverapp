package com.wakefern.api.wakefern.products.reports;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

// TODO: MM - evaluate this for v8. Is it integrated into Wynshop now?
/**
 * Endpoint for obtaining a new token for the Wakefern "Product Not Found" API. Used in subsequent
 * calls to create or delete "Product Not Found" entries.
 *
 * @author philmayer
 */
@Path(ApplicationConstants.Requests.Reports.NotFoundTokenURL)
public class GetToken extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetToken.class);
	
	/**
	 * Build and send off the request for a new "Product Not Found" API token. Use Jackson to map
	 * the request body to JSON. When a token is retrieved, it is valid for 24 hours.
	 *
	 * @return Response - if successful, the body will contain a "token" property
	 */
	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Accept) String accept,
    		@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType
    ) {
		try {
			String authenticateUrl = WakefernApplicationConstants.getBaseWakefernApiUrl() + WakefernApplicationConstants.Reports.NotFound.authenticate;

			Map<String, String> headerMap = new HashMap<>(1);
			headerMap.put(ApplicationConstants.Requests.Headers.Accept, accept);
			headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);

			Map<String, String> requestBodyMap = new HashMap<>(2);

			String[] userPass = ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.PROD_NOT_FOUND_LOGIN).split(":");
			requestBodyMap.put("UserName", userPass[0]);
			requestBodyMap.put("Password", userPass[1]);
			String requestBodyJson = new ObjectMapper().writeValueAsString(requestBodyMap);

			String responseBody = HTTPRequest.executePost(authenticateUrl, requestBodyJson, headerMap);
			return this.createValidResponse(responseBody);
		} catch (Exception exception) {
			String errorData = LogUtil.getRequestData("GetToken::Exception", LogUtil.getRelevantStackTrace(exception));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(exception));
			return this.createErrorResponse(exception);
		}
	}
	
	public String getProdNotFoundLogin(){
		String responseBody = "";
		try{
			String authenticateUrl = WakefernApplicationConstants.getBaseWakefernApiUrl() + WakefernApplicationConstants.Reports.NotFound.authenticate;
			Map<String, String> headerMap = new HashMap<>(1);
			headerMap.put(ApplicationConstants.Requests.Headers.Accept, ApplicationConstants.Requests.Headers.MIMETypes.json);
			headerMap.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
	
			Map<String, String> requestBodyMap = new HashMap<>(2);

			String[] userPass = ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.PROD_NOT_FOUND_LOGIN).split(":");
			requestBodyMap.put("UserName", userPass[0]);
			requestBodyMap.put("Password", userPass[1]);
			String requestBodyJson = new ObjectMapper().writeValueAsString(requestBodyMap);
	
			responseBody = HTTPRequest.executePost(authenticateUrl, requestBodyJson, headerMap);
		} catch (Exception exception) {
			responseBody = "{\"error\":\"fail to get BI token\"}";
		}
		return responseBody;
	}
}
