package com.wakefern.api.proxy.wakefern.ppcJwtToken;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.wakefern.wynshop.WynshopApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.UserJWTV2;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;

/**
 * 
 * @author Danny Zheng
 * @date 2021-10-07
 * 
 * 
 * 
 */
@Path(ApplicationConstants.Requests.Proxy + "/ppc/jwt/token")
public class GetPpcJwtToken extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetPpcJwtToken.class);

	@GET
	public Response getJWT(
			@HeaderParam(ApplicationConstants.Requests.Headers.xSiteHost) String xSiteHost,
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String sessionToken,
			@QueryParam("expires_in") int expiresInSeconds) {
		try {

			final String url = WynshopApplicationConstants.BaseURL + "/customers";
			
			Map<String, String> headerMap = new HashMap<>();

			//for the Cloudflare pass-thru
			headerMap.put(ApplicationConstants.Requests.Headers.userAgent,
					ApplicationConstants.StringConstants.wakefernApplication);

			headerMap.put(ApplicationConstants.Requests.Headers.Authorization, sessionToken);
			headerMap.put(ApplicationConstants.Requests.Headers.Accept, "*/*");
			headerMap.put(ApplicationConstants.Requests.Headers.xSiteHost, xSiteHost);

			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiMediumTimeout());

			JSONObject jsonResponse = new JSONObject(response);
			
			String ppc = jsonResponse.getString("loyaltyId");
			
			if ((ppc == null) || ppc.length() == 0) {
				throw new Exception("A PPC# is not found for this access token of " + sessionToken);
			}
			
			logger.trace("expiresInSeconds: " + expiresInSeconds);
			
			String jwtToken = UserJWTV2.generate(ppc, expiresInSeconds);
			
			JSONObject jsonToken = new JSONObject();
			jsonToken.put("PpcJwtToken", jwtToken);
			
			return this.createValidResponse(jsonToken.toString());

		} catch (Exception e) {
			
			if (LogUtil.isLoggable(e)) {
				String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
						ApplicationConstants.Requests.Headers.Accept, "*/*", ApplicationConstants.Requests.Headers.xSiteHost, xSiteHost,
						"expiresInSeconds", expiresInSeconds,
						ApplicationConstants.Requests.Headers.Authorization, sessionToken);
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}

			return this.createErrorResponse(e);
		}
	}
}
