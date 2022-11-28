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

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
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
	private final static String CONVERTED_CORPROATE_STORE = "0163";
	
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

			if (storeId.equalsIgnoreCase(DEFAULT_CORPORATE_STORE)) {
				//Convert the default corporate store 3000 to 0163 before calling Prodx
				wakefernStoreId = CONVERTED_CORPROATE_STORE;
			} else {
				switch (storeId.length()) {
					case 1 : wakefernStoreId = "000" + storeId.trim(); 
						break;
					case 2 : wakefernStoreId = "00" + storeId.trim();
						break;
					case 3 : wakefernStoreId = "0" + storeId.trim();
						break;
					case 4 : wakefernStoreId = storeId.trim();
						break;
					default: throw new RuntimeException("A Wakefern store ID has to be btw 0 and 9999");
					
				}
			}
			
			logger.debug("wakefernStoreId: " + wakefernStoreId);
			
			builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.StoreId, wakefernStoreId);
			
			//Add "primaryOnly" query param to each outgoing request
			if (isPrimaryOnly) {
				builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.PrimaryOnly, "true");
			} else {
				builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.PrimaryOnly, "false");
			}
			
			logger.debug("isPrimaryOnly: " + isPrimaryOnly);
			
	        // note there is no staging URl, use a query parameter/value of test=true to be acting a staging
	        // see JIRA at https://wakefern.atlassian.net/browse/SHOP-701 for Prodx product variations API
			if (VcapProcessor.isServiceStaging(VcapProcessor.getProdxService()) == true) {
				builder.addParameter(WakefernApplicationConstants.Prodx.ProductsVariations.Test, "true");
			}
			
			logger.debug("Is test/staging? : " + VcapProcessor.isServiceStaging(VcapProcessor.getProdxService()));

			final String url = builder.build().toString();

			headers.put(ApplicationConstants.Requests.Headers.Authorization,
					VcapProcessor.getProdxVariationsApiKeyProd());

			return this.createValidResponse(HTTPRequest.executeGet(url, headers, VcapProcessor.getApiMediumTimeout()));

		} catch (Exception e) {
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"productId", productId,
					"storeId", storeId,
					"wakefernStoreId", wakefernStoreId,
					"primaryOnly", isPrimaryOnly,
					"test", VcapProcessor.isServiceStaging(VcapProcessor.getProdxService()));
			
			if (LogUtil.isLoggable(e)) {
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}
			
			return this.createErrorResponse(errorData, e);
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
