package com.wakefern.api.proxy.apim.srfh.curbside;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Proxy endpoint for fetching an existing Curbside session via the SRFH api
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.CurbsideSession.Proxy.Path)
public class GetCurbsideSession extends BaseService {

	private static final Logger logger = Logger.getLogger(GetCurbsideSession.class.getName());

	@GET
	@ValidatePPCWithJWTV2
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	public Response getInfo(@PathParam("ppc") String ppc,
							@QueryParam("orderNumber") String orderNumber,
							@QueryParam("storeNumber") String storeNumber)
	{
		logger.debug("GET curbside session for " + ppc + " storeNumber: " + storeNumber + " orderNumber: " + orderNumber);
		try {
			URIBuilder uriBuilder = new URIBuilder(VcapProcessor.getTargetSRFHCurbsideBaseUrl()
					+ WakefernApplicationConstants.CurbsideSession.Upstream.Path);
			uriBuilder.addParameter(WakefernApplicationConstants.CurbsideSession.RequestParamsQuery.orderNumber, orderNumber);
			uriBuilder.addParameter(WakefernApplicationConstants.CurbsideSession.RequestParamsQuery.storeNumber, storeNumber);
			uriBuilder.addParameter(WakefernApplicationConstants.CurbsideSession.RequestParamsQuery.frequentShopperNumber, ppc);
			final String requestURI = uriBuilder.build().toString();
			Map<String, String> headers = new HashMap<>();
			headers.put(WakefernApplicationConstants.APIM.sub_key_header, VcapProcessor.getTargetSRFHCurbsideApiKey());

			String response = HTTPRequest.executeGet(requestURI, headers, VcapProcessor.getApiMediumTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_APIM_SRFH_GET_CURBSIDE_SESSION);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			Response response = createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}
}
