package com.wakefern.api.proxy.wakefern.prodx;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
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

/* 
 *  @author Danny zheng
 *  @date   11/15/2022
 *  
 *  @description Prodx to provide a group of variations for a selected product
 *  @see https://wakefern.atlassian.net/browse/SHOP-701
 *  
 *  @QueryParam("primaryOnly") refers to the type of variation groups returned. 
 *  Setting it to true limits the variation groupings to only groupings marked as primary on the Prodx side (i.e. Size, flavor, weight etc.)  
 *  Secondary variation groups are more niche to the particular product we a retrieving variations for. (i.e. SPF and Form for Lip balm)
 */

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.Prodx.ProductsVariations.Proxy.GetProductVariations)
public class GetProductVariations extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetProductVariations.class);
	private final static String DEFAULT_CORPORATE_STORE = "3000";
	private final static String CONVERTED_CORPORATE_STORE = "0163";
	
	@GET
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	public Response getResponse(@PathParam("productId") String productId,
								@QueryParam("storeId") String storeId,
								@DefaultValue("true") @QueryParam("primaryOnly") boolean isPrimaryOnly,
								@Context UriInfo uriInfo) {

		
		Map<String, String> headers = new HashMap<>();
		String wakefernStoreId = null;
				
		storeId = storeId.trim();
		
		try {
			URIBuilder builder = new URIBuilder(constructUrl(productId.trim()));
			wakefernStoreId = ProdxUtils.storeIdConvertor(storeId);
			
			logger.debug("wakefernStoreId: " + wakefernStoreId);
			
			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.StoreId, wakefernStoreId);
			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.PrimaryOnly, String.valueOf(isPrimaryOnly));
	
			logger.debug("isPrimaryOnly: " + isPrimaryOnly);
			
	        // note there is no staging URl, use a query parameter/value of test=true to be acting a staging
	        // see JIRA at https://wakefern.atlassian.net/browse/SHOP-701 for Prodx product variations API

			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.Test, 
					String.valueOf(EnvManager.isServiceStaging(EnvManager.getProdxService())));

			
			logger.debug("Is test/staging? : " + EnvManager.isServiceStaging(EnvManager.getProdxService()));

			final String url = builder.build().toString();

			headers.put(ApplicationConstants.Requests.Headers.Authorization,
					EnvManager.getProdxVariationsApiKeyProd());

			return this.createValidResponse(HTTPRequest.executeGet(url, headers, EnvManager.getApiMediumTimeout()));

		} catch (Exception e) {
        	
			String errorData = parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.Authorization, EnvManager.getProdxVariationsApiKeyProd(),
					"productId", productId,
					"storeId", storeId,
					"wakefernStoreId", wakefernStoreId,
					"primaryOnly", isPrimaryOnly,
					"test", EnvManager.isServiceStaging(EnvManager.getProdxService()));
			
			Response response = this.createErrorResponse(errorData, e);
			return UpstreamErrorHandler.handleResponse(response);
		}
	}

	private static String constructUrl(final String productId) {
        // note there is no staging URL, use a query parameter/value of test=true to be acting a staging
        // see JIRA at https://wakefern.atlassian.net/browse/SHOP-701 for Prodx product variations API
		String template = WakefernApplicationConstants.Prodx.Upstream.prodBaseUrl + 
				WakefernApplicationConstants.Prodx.ProductsVariations.GetVariationsPath;
		
		Map<String, String> pathParams = new HashMap<>();
		pathParams.put(WakefernApplicationConstants.Prodx.ProductsVariations.ProductId, productId);

		UriBuilder builder = UriBuilder.fromPath(template);
		URI output = builder.buildFromMap(pathParams);
		return output.toASCIIString();
	}
}
