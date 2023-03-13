package com.wakefern.api.proxy.wakefern.prodx;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.Prodx.ProductsComplements.Proxy.GetProductComplements)
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
			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsComplements.AISLE_ID, EnvManager.getProdxComplementsAisleId());

			final String url = builder.build().toString();

			headers.put(ApplicationConstants.Requests.Headers.Authorization,
					EnvManager.getProdxComplementsApiKey());

			return this.createValidResponse(HTTPRequest.executeGet(url, headers, EnvManager.getApiMediumTimeout()));

		} catch (Exception e) {
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"productId", productId,
					"storeId", storeId,
					"customerId", customerId,
					"availability", availability,
					"groupsCount", groupsCount,
					"productsCount", productsCount,
					"test", test,
					"embeds", embeds);
			
			if (LogUtil.isLoggable(e)) {
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}
			
			return this.createErrorResponse(errorData, e);
		}
	}

	private static String constructUrl(final String productId) {
		String template = EnvManager.getProdxServiceEndpoint() + WakefernApplicationConstants.Prodx.ProductsComplements.GetComplementsPath;
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put("productId", productId);

		UriBuilder builder = UriBuilder.fromPath(template);
		URI output = builder.buildFromMap(pathParams);
		return output.toASCIIString();
	}
}
