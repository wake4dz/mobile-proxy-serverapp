package com.wakefern.api.proxy.wakefern.prodx;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;

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
								@QueryParam("groupsCount") int groupsCount,
								@QueryParam("productsCount") int productsCount,
								@QueryParam("embeds") String embeds,
								@Context UriInfo uriInfo) {

		Map<String, String> headers = new HashMap<>();
		String wakefernStoreId = null;

		storeId = storeId.trim();

		try {
			// Iterate over the query params and apply to the request if not null.
			MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
			URIBuilder builder = new URIBuilder(constructUrl(productId));
			for (String key: queryParams.keySet()) {
				builder.addParameter(key, queryParams.getFirst(key));
			}

			wakefernStoreId = ProdxUtils.storeIdConvertor(storeId);

			// Update the Store ID param with the wakefernStoreId
			logger.debug("wakefernStoreId: " + wakefernStoreId);
			builder.setParameter(WakefernApplicationConstants.Prodx.ProductsComplements.StoreId, wakefernStoreId);

			// Add "aisleId" query param to each outgoing request (Aisle ID is optional prop...)
			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsComplements.AISLE_ID, EnvManager.getProdxComplementsAisleId());

			// note there is no staging URl, use a query parameter/value of test=true to be acting a staging
			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsComplements.Test,
					String.valueOf(EnvManager.isServiceStaging(EnvManager.getProdxService())));

			logger.debug("Is test/staging? : " + EnvManager.isServiceStaging(EnvManager.getProdxService()));

			final String url = builder.build().toString();

			headers.put(ApplicationConstants.Requests.Headers.Authorization,
					EnvManager.getTargetProdxComplementsApiKey());

			return this.createValidResponse(HTTPRequest.executeGet(url, headers, EnvManager.getApiMediumTimeout()));

		} catch (Exception e) {
        	
			String errorData = parseAndLogException(logger, e, 
					WakefernApplicationConstants.Prodx.ProductsComplements.AISLE_ID, EnvManager.getProdxComplementsAisleId(),
					ApplicationConstants.Requests.Headers.Authorization, EnvManager.getTargetProdxComplementsApiKey(),
					"productId", productId,
					"storeId", storeId,
					"wakefernStoreId", wakefernStoreId,
					"customerId", customerId,
					"availability", availability,
					"groupsCount", groupsCount,
					"productsCount", productsCount,
					"test", EnvManager.isServiceStaging(EnvManager.getProdxService()),
					"embeds", embeds);

			Response response = this.createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}

	private static String constructUrl(final String productId) {
		String template = WakefernApplicationConstants.Prodx.Upstream.prodBaseUrl +
				WakefernApplicationConstants.Prodx.ProductsComplements.GetComplementsPath;

		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(WakefernApplicationConstants.Prodx.ProductsComplements.ProductId, productId);

		UriBuilder builder = UriBuilder.fromPath(template);
		URI output = builder.buildFromMap(pathParams);
		return output.toASCIIString();
	}
}
