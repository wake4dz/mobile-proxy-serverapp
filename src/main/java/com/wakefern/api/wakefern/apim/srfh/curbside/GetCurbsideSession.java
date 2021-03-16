package com.wakefern.api.wakefern.apim.srfh.curbside;

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
 * Proxy endpoint for fetching an existing Curbside session via the SRFH api
 */
@Path(WakefernApplicationConstants.CurbsideSession.Proxy.Path)
public class GetCurbsideSession extends BaseService {

	private static final Logger logger = Logger.getLogger(GetCurbsideSession.class.getName());

	@GET
	@ValidatePPCWithJWT
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
			LogUtil.addErrorMaps(e, MwgErrorType.APIM_SRFH_GET_CURBSIDE_SESSION);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			Response response = createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}
}
