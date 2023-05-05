package com.wakefern.api.proxy.wakefern.itemLocator;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.global.ApplicationConstants.Requests;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Path(ApplicationConstants.Requests.Proxy + "/itemlocator")
public class GetItemLocator extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetItemLocator.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Deprecated
	@Path("/item/location/{storeId}/{upc}")
	public Response getItem(@PathParam("storeId") String storeId, 
			@PathParam("upc") String upc) { // note: upc's last digit of checksum of an UPC is already removed by caller
		Map<String, String> wkfn = new HashMap<>();

		String authToken = null;
		
		try {
			String path = WakefernApplicationConstants.ItemLocator.baseURL
					+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;

			authToken = WakefernAuth.getAuthToken(EnvManager.getJwtPublicKey());
			wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
			wkfn.put("Authentication", authToken);
			wkfn.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
			// Call APIM Gateway to avoid any foreign IP addresses
			wkfn.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());

			logger.trace("URL path: " + path);

			return this.createValidResponse(HTTPRequest.executeGet(path, wkfn, EnvManager.getApiMediumTimeout()));
		} catch (Exception e) {

			String errorData = parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.contentType, "application/json",
					"upc", upc,
					"Authentication", authToken,
					WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());

			return this.createErrorResponse(errorData, e);
		}
	}
	
	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/item/location/v2/{storeId}/{upc}")
	public Response getItemV2(@PathParam("storeId") String storeId,
							@PathParam("upc") String upc) {
		Map<String, String> wkfn = new HashMap<>();
		ItemLocatorDto cached = ItemLocatorCache.getInstance().get(storeId, upc);

		if (cached != null) {
			logger.debug("Return cached item locator for {storeId, upc}: " + storeId + ", " + upc);
			return createValidResponse(cached.toApiResponse());
		}

		String authToken = null;
		
		try {
			String path = WakefernApplicationConstants.ItemLocator.baseURL
					+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;

			authToken = WakefernAuth.getAuthToken(EnvManager.getJwtPublicKey());
			wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
			wkfn.put("Authentication", authToken);
			// Call APIM Gateway to avoid any foreign IP addresses
			wkfn.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());
			wkfn.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

			logger.trace("auth token:" + authToken);
			logger.trace("URL path: " + path);

			String response = HTTPRequest.executeGet(path, wkfn, EnvManager.getApiMediumTimeout());
			logger.trace("item locator fetch resp: " + response);
			ItemLocatorDto itemLocator = ItemLocatorUtils.generateItemLocatorForUpc(upc, response);
			ItemLocatorCache.getInstance().add(storeId, upc, itemLocator);
			return createValidResponse(itemLocator.toApiResponse());
		} catch (Exception e) {
			String errorData = parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.contentType, "application/json",
					"storeId", storeId,
					"upc", upc,
					"Authentication", authToken,
					WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());

			return createErrorResponse(errorData, e);
		}
	}

	@POST
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/item/location/v2/{storeId}/_batch")
	public Response getBatchItemLocators(@PathParam("storeId") String storeId,
							  String jsonBody) {
		JSONObject body = null;
		try {
			body = new JSONObject(jsonBody);
			return createValidResponse(ItemLocatorUtils.batchRequest(storeId, body.getJSONArray("upcs")));
		} catch (Exception e) {
			String errorData = parseAndLogException(logger, e,
					ApplicationConstants.Requests.Headers.contentType, "application/json",
					"storeId", storeId,
					"body", jsonBody,
					WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());

			return createErrorResponse(errorData, e);
		}
	}
}