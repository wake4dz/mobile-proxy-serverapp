package com.wakefern.api.proxy.mi9.v8.cart;

import com.wakefern.api.proxy.wakefern.itemLocator.ItemLocatorCache;
import com.wakefern.api.proxy.wakefern.itemLocator.ItemLocatorDto;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorUtils;
import com.wakefern.wynshop.WynshopApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Matt Miller
 * @date 2022-09-06
 */
@Path(Requests.Proxy + WynshopApplicationConstants.Requests.Routes.CartItemLocator)
public class GetCartItemLocator extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetCartItemLocator.class);

	@GET
	public Response getCartItemLocator(
			@HeaderParam(Requests.Headers.Accept) String accept,
			@HeaderParam(Requests.Headers.xSiteHost) String xSiteHost,
			@HeaderParam(Requests.Headers.Authorization) String sessionToken,
			@PathParam(WynshopApplicationConstants.Requests.Params.Path.storeID) String storeId) {
		try {
			final String url = WynshopApplicationConstants.BaseURL + "/stores/" + storeId.trim() + "/cart";
			
			Map<String, String> headerMap = new HashMap<>();

			//for the Cloudflare pass-thru
			headerMap.put(Requests.Headers.userAgent,
					ApplicationConstants.StringConstants.wakefernApplication);

			headerMap.put(Requests.Headers.Authorization, sessionToken);
			headerMap.put(Requests.Headers.Accept, accept);
			headerMap.put(Requests.Headers.xSiteHost, xSiteHost);

			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiMediumTimeout());

			// call Wakefern's ItemLocator API to get additional data
			// return empty item locator info for each sku if ItemLocator API fails for some reason
			response = decorateCartWithItemLocations(storeId, response);

			return this.createValidResponse(response);

		} catch (Exception e) {
			
			if (LogUtil.isLoggable(e)) {
				String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
						Requests.Headers.Accept, accept, Requests.Headers.xSiteHost, xSiteHost,
						Requests.Headers.Authorization, sessionToken, WynshopApplicationConstants.Requests.Params.Path.storeID, storeId);
				logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			}
			
			return this.createErrorResponse(e);
		}
	}

	/**
	 * Get Item Location data and add it to the Mi9 V8 Planning List JSON Response
	 * Data.
	 * 
	 * Mi9 returns a JSON response with "items" property with all items in a
	 * planning list. For each SKU, we remove the check digit, string them with a
	 * comma, and call Wakefern Item Locator's API with a wakefern store id
	 * Add/update these 3 properties for a new JSON object aisle → wf_area_desc
	 * aisleAreaSeqNum → area_seq_num aisleSectionDesc → wf_sect_desc
	 * 
	 * "itemLocator": { "aisle": "Seafood", "AisleAreaSeqNum": 10,
	 * "AisleSectionDesc": "seafood section description" },
	 * 
	 * 
	 */
	private String decorateCartWithItemLocations(String storeId, String cartResponse) throws Exception {
		try {
			JSONObject origRespJObj = new JSONObject(cartResponse);

			if (!origRespJObj.has(WakefernApplicationConstants.Mi9V8ItemLocator.LineItems) || storeId.length() < 1) {
				// The supplied response string does not contain any Items (products).
				// Just return the original string.
				return cartResponse;
			}

			JSONArray itemsJArray = (JSONArray) origRespJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.LineItems);
			final int itemsSize = itemsJArray.length();

			if (itemsSize == 0) {
				// The Items Array is empty (no products).
				return cartResponse;
			}

			JSONObject retvalJObj = new JSONObject();

			Map<String, JSONObject> processedPartitionItems;
			Map<String, JSONObject> itemsMap = new HashMap<>();

			// Set up retval with all non-items data
			for (String key : origRespJObj.keySet()) {
				if (!key.equals(WakefernApplicationConstants.Mi9V8ItemLocator.LineItems)) {
					Object keyvalue = origRespJObj.get(key);
					retvalJObj.put(key, keyvalue);
				}
			}

			int currentListPosition = 0;

			final int numRequests = ItemLocatorUtils.getNumRequests(itemsSize);
			List<String> partitionItemsList;
			StringBuilder partitionItemsSB;

			// Get auth token
			final String authToken = WakefernAuth.getInfo(WakefernApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));

			for (int i=0; i < numRequests; i++) {
				partitionItemsList = new ArrayList<>();
				partitionItemsSB = new StringBuilder();
				
				// build each partition data to be used for a Wakefern's Item Locator API call
				while ((WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE * (i + 1) > currentListPosition) && (itemsSize > currentListPosition)) {
					JSONObject itemJObj = (JSONObject) itemsJArray.get(currentListPosition);

					final String sku = itemJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
					final String upc = ItemLocatorUtils.removeCheckSumDigit(sku);

					currentListPosition++;
					ItemLocatorDto cached = ItemLocatorCache.getInstance().getItemLocation(storeId, upc);
					if (cached != null) {
						itemsMap.put(upc, cached.toJSON());
					} else {
						partitionItemsSB.append(upc).append(",");
						partitionItemsList.add(upc);
					}
				}

				if (partitionItemsList.isEmpty()) {
					continue;
				}
		
				final String path = WakefernApplicationConstants.ItemLocator.baseURL
						+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + partitionItemsSB;
	
				Map<String, String> wkfn = new HashMap<>();
				wkfn.put(Requests.Headers.contentType, Requests.Headers.MIMETypes.json);
				wkfn.put("Authentication", authToken);
	
				String responseData = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());
	
				logger.trace("partitionNumber: " + (i + 1));
				logger.trace("URL path: " + path);
				logger.trace("PartitionItemsSB: " + partitionItemsSB);
				logger.trace("PartitionItemsList: " + partitionItemsList);
				logger.trace("responseData: " + responseData);
	
				processedPartitionItems = ItemLocatorUtils.generateItemLocatorMap(partitionItemsList, responseData);

				for (Map.Entry<String, JSONObject> entry : processedPartitionItems.entrySet()) {
					// build up the entire map for lookup later
					itemsMap.put(entry.getKey(), entry.getValue());
				}
			}

			for (int i=0; i < itemsSize; i++) {
				JSONObject itemJObj = itemsJArray.getJSONObject(i);

				String sku = itemJObj.getString(WakefernApplicationConstants.Mi9V8ItemLocator.Sku);
				String upc = ItemLocatorUtils.removeCheckSumDigit(sku);

				if (itemsMap.get(upc) != null) {
					JSONObject itemLocator = itemsMap.get(upc);
					itemJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocator, itemLocator);

					// add to cache
					ItemLocatorCache.getInstance().putItemLocation(storeId, upc, ItemLocatorDto.fromJSON(itemLocator));
				} else { //defense code to add dummy data
					ItemLocatorUtils.addDummyItemLocatorObj(itemJObj);
				}
				
				retvalJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.LineItems, itemJObj);
			}

			return retvalJObj.toString();

		} catch (Exception e) {	
			logger.error("[updateCartItems::Exception processing item locator. The error message: "
					+ LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));
			
			// if there is an exception, we try to return the Mi9 shopping cart info + empty item locator for each sku.
			// if addEmptyItemLocator() also throw an exception, the caller would get a HTTP 500 with a brief error message
			return ItemLocatorUtils.addEmptyItemLocator(cartResponse, true);
		}
	}
}