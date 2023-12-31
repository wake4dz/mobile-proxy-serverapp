package com.wakefern.api.proxy.apim.srfh.curbside;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Proxy endpoint for customer check-in for curbside pickup order (through SRFH API)
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.CurbsideSession.Proxy.Path)
public class UpdateCurbsideSession extends BaseService {

	private static final Logger logger = LogManager.getLogger(UpdateCurbsideSession.class.getName());

	@POST
	@ValidatePPCWithJWTV2
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	public Response getInfo(@PathParam("ppc") String ppc,
							@QueryParam("orderNumber") String orderNumber,
							@QueryParam("storeNumber") String storeNumber,
							String body) {
		logger.debug("Update curbside session for " + ppc
				+ " body: " + body
				+ " storeNumber: " + storeNumber
				+ " orderNumber: " + orderNumber);
		try {
			URIBuilder uriBuilder = new URIBuilder(EnvManager.getTargetSRFHCurbsideBaseUrl()
					+ WakefernApplicationConstants.CurbsideSession.Upstream.Path);
			uriBuilder.addParameter(WakefernApplicationConstants.CurbsideSession.RequestParamsQuery.orderNumber, orderNumber);
			uriBuilder.addParameter(WakefernApplicationConstants.CurbsideSession.RequestParamsQuery.storeNumber, storeNumber);
			uriBuilder.addParameter(WakefernApplicationConstants.CurbsideSession.RequestParamsQuery.frequentShopperNumber, ppc);
			final String requestURI = uriBuilder.build().toString();

			Map<String, String> headers = new HashMap<>();
			headers.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
			headers.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getTargetSRFHCurbsideApiKey());
			headers.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

			logger.info("request URI = " + requestURI + " body = " + body + " key = " + EnvManager.getTargetSRFHCurbsideApiKey());
			String response = HTTPRequest.executePost(requestURI, body, headers, EnvManager.getApiMediumTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			String errorData = parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json,
					"ppc", ppc,
					"orderNumber", orderNumber,
					"storeNumber", storeNumber,
					WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getTargetSRFHCurbsideApiKey());
			
			Response response = createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}
}
