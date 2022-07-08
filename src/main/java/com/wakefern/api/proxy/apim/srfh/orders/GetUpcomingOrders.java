package com.wakefern.api.proxy.apim.srfh.orders;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Proxy endpoint for fetching active pickup orders from
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.UpcomingOrders.Proxy.Path)
public class GetUpcomingOrders extends BaseService {

	private static final Logger logger = LogManager.getLogger(GetUpcomingOrders.class.getName());

	@GET
	@ValidatePPCWithJWTV2
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	public Response getInfo(@PathParam("ppc") String ppc,
							@QueryParam("fulfillmentDate") String fulfillmentDate,
							@HeaderParam(ApplicationConstants.Requests.Headers.appVersion) String appVersion) {
		logger.debug("Fetching upcoming orders for ppc " + ppc + " for fulfillmentDate: " + fulfillmentDate);
		try {
			URIBuilder uriBuilder = new URIBuilder(VcapProcessor.getTargetSRFHOrdersBaseUrl()
					+ WakefernApplicationConstants.UpcomingOrders.Upstream.upcomingOrdersPath);
			uriBuilder.addParameter(WakefernApplicationConstants.UpcomingOrders.RequestParamsQuery.frequentShopperNumber, ppc);
			if (fulfillmentDate != null) {
				uriBuilder.addParameter(WakefernApplicationConstants.UpcomingOrders.RequestParamsQuery.fulfillmentDate, fulfillmentDate);
			}

			final String requestURI = uriBuilder.build().toString();
			Map<String, String> headers = new HashMap<>();
			headers.put("X-ShopRite-Mobile-Version", appVersion);
			headers.put(ApplicationConstants.Requests.Headers.Accept, WakefernApplicationConstants.UpcomingOrders.Upstream.MimeType);
			headers.put(WakefernApplicationConstants.APIM.sub_key_header, VcapProcessor.getTargetSRFHOrdersApiKey());

			String response = HTTPRequest.executeGet(requestURI, headers, VcapProcessor.getApiMediumTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc, "fulfillmentDate", fulfillmentDate);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			Response response = createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}
}
