package com.wakefern.api.proxy.apim.srfh.curbside;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWT;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy endpoint for creating a Curbside session via the SRFH api
 */
@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.CurbsideSession.Proxy.Path)
public class CreateCurbsideSession extends BaseService {

	private static final Logger logger = Logger.getLogger(CreateCurbsideSession.class.getName());

	@PUT
	@ValidatePPCWithJWT
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	public Response getInfo(@PathParam("ppc") String ppc,
							String body) {
		logger.debug("Create curbside session for " + ppc
					+ " body: " + body);
		try {
			URIBuilder uriBuilder = new URIBuilder(VcapProcessor.getTargetSRFHCurbsideBaseUrl()
					+ WakefernApplicationConstants.CurbsideSession.Upstream.Path);

			final String requestURI = uriBuilder.build().toString();
			Map<String, String> headers = new HashMap<>();
			headers.put(WakefernApplicationConstants.APIM.sub_key_header, VcapProcessor.getTargetSRFHCurbsideApiKey());

			String response = HTTPRequest.executePut(requestURI, body, headers, VcapProcessor.getApiMediumTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_APIM_SRFH_CREATE_CURBSIDE_SESSION);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			Response response = createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}
}
