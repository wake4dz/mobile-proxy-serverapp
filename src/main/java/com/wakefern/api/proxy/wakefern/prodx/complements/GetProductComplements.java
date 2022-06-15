package com.wakefern.api.proxy.wakefern.prodx.complements;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.VcapProcessor;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.Prodx.Complements.Proxy.GetProductComplements)
public class GetProductComplements extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetProductComplements.class);

	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	public Response getResponse(@PathParam("productId") String productId,
								@QueryParam("storeId") String storeId,
								@QueryParam("customerId") String customerId,
								@QueryParam("availability") String availability,
								@QueryParam("groupscount") int groupsCount,
								@QueryParam("test") boolean test,
								@QueryParam("productsCount") int productsCount,
								@QueryParam("embeds") String embeds,
								@Context UriInfo uriInfo) {

		Map<String, String> headers = new HashMap<>();

		try {
			// Iterate over the query params and apply to the request if not null.
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
			URIBuilder builder = new URIBuilder(constructUrl(productId));
			for (String key: queryParams.keySet()) {
				builder.addParameter(key, queryParams.getFirst(key));
			}

			// Add "aisleId" query param to each outgoing request
			builder.addParameter(WakefernApplicationConstants.Prodx.Complements.AISLE_ID, VcapProcessor.getProdxAisleId());

			final String url = builder.build().toString();

			headers.put(ApplicationConstants.Requests.Headers.Authorization,
					VcapProcessor.getProdxApiKey());

			return this.createValidResponse(HTTPRequest.executeGet(url, headers, VcapProcessor.getApiMediumTimeout()));

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_PRODX_GET_PRODUCT_COMPLEMENTS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"productId", productId,
					"storeId", storeId,
					"customerId", customerId,
					"availability", availability,
					"groupsCount", groupsCount,
					"productsCount", productsCount,
					"test", test,
					"embeds", embeds);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

	private static String constructUrl(final String productId) {
		String template = VcapProcessor.getProdxServiceEndpoint() + WakefernApplicationConstants.Prodx.Complements.Upstream.GetComplementsPath;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("productId", productId);

		UriBuilder builder = UriBuilder.fromPath(template);
		URI output = builder.buildFromMap(pathParams);
		return output.toASCIIString();
	}
}
