package com.wakefern.api.proxy.apim.srfh.orders;

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
import org.apache.http.client.utils.URIBuilder;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy endpoint for fetching active pickup orders from
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.UpcomingOrders.Proxy.Path)
public class GetUpcomingOrders extends BaseService {

	private static final Logger logger = Logger.getLogger(GetUpcomingOrders.class.getName());

	@GET
	@ValidatePPCWithJWTV2
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	public Response getInfo(@PathParam("ppc") String ppc,
							@QueryParam("fulfillmentDate") String fulfillmentDate,
							@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion) {
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
			headers.put(ApplicationConstants.Requests.Header.contentAccept, WakefernApplicationConstants.UpcomingOrders.Upstream.MimeType);
			headers.put(WakefernApplicationConstants.APIM.sub_key_header, VcapProcessor.getTargetSRFHOrdersApiKey());

			String response = HTTPRequest.executeGet(requestURI, headers, VcapProcessor.getApiMediumTimeout());
			return createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_APIM_SRFH_GET_UPCOMING_ORDERS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc, "fulfillmentDate", fulfillmentDate);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			Response response = createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}
}
