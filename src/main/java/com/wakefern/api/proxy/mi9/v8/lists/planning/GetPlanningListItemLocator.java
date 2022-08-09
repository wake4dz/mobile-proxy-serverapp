package com.wakefern.api.proxy.mi9.v8.lists.planning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

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

/**
 * 
 * @author Danny Zheng
 * @date 2021-09-13
 * 
 *       https://mobile-gateway.staging.brands.wakefern.com/index.html#/Lists/get_api_lists_planning__retailerstoreid_
 *       Lists: Get /api/lists/planning/{retailerstoreid} - Retrieves planning
 *       for a store curl -X 'GET' \
 *       'https://mobile-gateway.staging.brands.wakefern.com/api/lists/planning/144'
 *       \
 * 
 * 
 */
@Path(ApplicationConstants.Requests.Proxy + WynshopApplicationConstants.Requests.Routes.ShoppingCartItemLocator)
public class GetPlanningListItemLocator extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetPlanningListItemLocator.class);

	@GET
	public Response getPlanningListItemLocator(
			@HeaderParam(Requests.Headers.Accept) String accept,
			@HeaderParam(Requests.Headers.xSiteHost) String xSiteHost,
			@HeaderParam(Requests.Headers.Authorization) String sessionToken,
			@PathParam(WynshopApplicationConstants.Requests.Params.Path.storeID) String storeId) {
		try {

			final String url = WynshopApplicationConstants.BaseURL + "/lists/planning/" + storeId.trim();
			
			Map<String, String> headerMap = new HashMap<String, String>();

			//for the Cloudflare pass-thru
			headerMap.put(Requests.Headers.userAgent,
					ApplicationConstants.StringConstants.wakefernApplication);

			headerMap.put(Requests.Headers.Authorization, sessionToken);
			headerMap.put(Requests.Headers.Accept, accept);
			headerMap.put(Requests.Headers.xSiteHost, xSiteHost);

			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiMediumTimeout());

			// call Wakefern's ItemLocator API to get additional data
			// return empty item locator info for each sku if ItemLocator API fails for some reason
			response = decoratePlanningListWithItemLocations(storeId, response);

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
	private String decoratePlanningListWithItemLocations(String storeId, String planningListResponse) throws Exception {
		try {
			JSONObject retvalJObj = new JSONObject();

			Map<String, JSONObject> processedPartitionItems;
			Map<String, JSONObject> itemsMap = new HashMap<>();
			
			JSONObject origRespJObj = new JSONObject(planningListResponse);

			if (!origRespJObj.has(WakefernApplicationConstants.Mi9V8ItemLocator.Items) || storeId.length() < 1) {
				// The supplied response string does not contain any Items (products).
				// Just return the original string.
				return planningListResponse;
			}

			JSONArray itemsJArray = (JSONArray) origRespJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);
			final int itemsSize = itemsJArray.length();

			if (itemsSize == 0) {
				// The Items Array is empty (no products).
				return planningListResponse;
			}

			// Set up retval with all non-items data
			for (Object key : origRespJObj.keySet()) {
				String keyStr = (String) key;

				if (!keyStr.equals(WakefernApplicationConstants.Mi9V8ItemLocator.Items)) {
					Object keyvalue = origRespJObj.get(keyStr);
					retvalJObj.put(keyStr, keyvalue);
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

					final String itemId = itemJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
					final String sku = removeCheckSumDigit(itemId);
					partitionItemsSB.append(sku).append(",");
					partitionItemsList.add(sku);
						
					currentListPosition++;
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
			
			
			for (int i=0; itemsSize > i; i++) {
				JSONObject itemJObj = (JSONObject) itemsJArray.get(i);

				String itemId = itemJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
				String sku = removeCheckSumDigit(itemId);

				if (itemsMap.get(sku) != null) {
					itemJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocator, itemsMap.get(sku));
				} else { //defense code to add dummy data
					addDummyItemLocatorObj(itemJObj);
				}
				
				retvalJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.Items, itemJObj);
			}

			// pre-sort by AisleAreaSeqNum to ease the UI processing
			JSONArray itemsSortArray = (JSONArray) retvalJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);
			retvalJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Items,
					ItemLocatorUtils.sortItems(itemsSortArray));

			return retvalJObj.toString();

		} catch (Exception e) {	
			logger.error("[updateCartItems::Exception processing item locator. The error message: "
					+ LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));
			
			// if there is an exception, we try to return the Mi9 shopping cart info + empty item locator for each sku.
			// if addEmptyItemLocator() also throw an exception, the caller would get a HTTP 500 with a brief error message
			return addEmptyItemLocator(planningListResponse);

		}
	}

	/**
	 * Create a "dummy" ItemLocator object
	 * @param itemJObj the JSONObject to add the object to.
	 */
	private static void addDummyItemLocatorObj(JSONObject itemJObj) {
		JSONObject dummyObj = new JSONObject();

		dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle, WakefernApplicationConstants.Mi9V8ItemLocator.Other);
		dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc, JSONObject.NULL);
		dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum, 888888);

		itemJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocator, dummyObj);
	}

	/**
	 * Apparently the SKU Wakefern expects, is slightly different from the one MWG
	 * provides. The last digit is the checksum.
	 * 
	 * @param sku
	 * @return
	 */
	private static String removeCheckSumDigit(String sku) {
		return sku.substring(0, sku.length() - 1);
	}
	
	
	/*
	 * add empty item locator for each sku if something goes wrong 
	 */
	private static String addEmptyItemLocator(String mi9ResponseData) throws Exception {
		try {
			JSONObject origRespJObj = new JSONObject(mi9ResponseData);
			
			JSONArray itemsJArray = (JSONArray) origRespJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);

			JSONObject retvalJObj = new JSONObject();

			// Set up retval with all non-items data
			for (Object key : origRespJObj.keySet()) {
				String keyStr = (String) key;

				if (!keyStr.equals(WakefernApplicationConstants.Mi9V8ItemLocator.Items)) {
					Object keyvalue = origRespJObj.get(keyStr);
					retvalJObj.put(keyStr, keyvalue);
				}
			}
			
			for (int i = 0; i < itemsJArray.length(); i++) {
				JSONObject item = itemsJArray.getJSONObject(i);

				addDummyItemLocatorObj(item);

				retvalJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.Items, item);
			}
			
			return retvalJObj.toString();
		} catch (Exception e) {
			logger.error("[addEmptyItemLocator::Exception adding item locator. The error message: "
					+ LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));
			
			throw new Exception(LogUtil.getExceptionMessage(e));
		}
		
	}
}