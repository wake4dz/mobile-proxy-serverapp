package com.wakefern.api.proxy.wakefern.ppcJwtToken;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.UserJWTV2;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
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
	private final static Logger logger = Logger.getLogger(GetPpcJwtToken.class);

	@GET
	public Response getShoppingCartItemLocator(
			@HeaderParam(MWGApplicationConstants.Headers.Params.xSiteHost) String xSiteHost,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@QueryParam("expires_in") int expiresInSeconds) {
		try {

			final String url = ApplicationConstants.Requests.Mi9V8.BaseURL + "/customers";
			
			Map<String, String> headerMap = new HashMap<String, String>();

			//for the Cloudflare pass-thru
			headerMap.put(ApplicationConstants.Requests.Header.userAgent,
					ApplicationConstants.StringConstants.wakefernApplication);

			headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, sessionToken);
			headerMap.put(ApplicationConstants.Requests.Header.contentAccept, "*/*");
			headerMap.put(ApplicationConstants.Requests.Header.xSiteHost, xSiteHost);

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
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_PPCJWTTOKEN_GET_PPC_JWT_TOKEN);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					MWGApplicationConstants.Headers.Params.accept, "*/*", MWGApplicationConstants.Headers.Params.xSiteHost, xSiteHost,
					"expiresInSeconds", expiresInSeconds,
					MWGApplicationConstants.Headers.Params.auth, sessionToken);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(e);
		}
	}
}
