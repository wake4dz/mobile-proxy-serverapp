package com.wakefern.api.proxy.mi9.v8.shopping.cart;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.api.wakefern.items.location.SortMi9V8ItemLocators;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorArray;
import com.wakefern.wakefern.itemLocator.Mi9V8ItemLocatorArray;

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
@Path(ApplicationConstants.Requests.Proxy + Requests.Mi9V8.ShoppingCartItemLocator)
public class GetShoppingCartItemLocator extends BaseService {
	private final static Logger logger = Logger.getLogger(GetShoppingCartItemLocator.class);

	@GET
	public Response getShoppingCartItemLocator(
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.xSiteHost) String xSiteHost,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeId) {
		try {

			final String url = ApplicationConstants.Requests.Mi9V8.BaseURL + "/lists/planning/" + storeId.trim();
			
			Map<String, String> headerMap = new HashMap<String, String>();

			//for the Cloudflare pass-thru
			headerMap.put(ApplicationConstants.Requests.Header.userAgent,
					ApplicationConstants.StringConstants.wakefernApplication);

			headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, sessionToken);
			headerMap.put(ApplicationConstants.Requests.Header.contentAccept, accept);
			headerMap.put(ApplicationConstants.Requests.Header.xSiteHost, xSiteHost);

			String response = HTTPRequest.executeGet(url, headerMap, VcapProcessor.getApiMediumTimeout());

			// call Wakefern's ItemLocator API to get additional data
			// return empty item locator info for each sku if ItemLocator API fails for some reason
			response = updateCartItems(storeId, response);

			return this.createValidResponse(response);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_MI9V8_GET_SHOPPING_CART_ITEM_LOCATOR);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					MWGApplicationConstants.Headers.Params.accept, accept, MWGApplicationConstants.Headers.Params.xSiteHost, xSiteHost,
					MWGApplicationConstants.Headers.Params.auth, sessionToken, MWGApplicationConstants.Requests.Params.Path.storeID, storeId);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(e);
		}
	}

	/**
	 * Get Item Location data and add it to the Mi9 V8 Shopping Cart JSON Response
	 * Data.
	 * 
	 * Mi9 returns a JSON response with "items" property with all items in a
	 * shopping cart. For each SKU, we remove the check digit, string them with a
	 * comma, and call Wakefern Item Locator's API with a wakefern store id
	 * Add/update these 3 properties for a new JSON object aisle → wf_area_desc
	 * aisleAreaSeqNum → area_seq_num aisleSectionDesc → wf_sect_desc
	 * 
	 * "itemLocator": { "aisle": "Seafood", "AisleAreaSeqNum": 10,
	 * "AisleSectionDesc": "seafood section description" },
	 * 
	 * 
	 */
	private String updateCartItems(String storeId, String cartResponseData) throws Exception{
		try {

			JSONObject origRespJObj = new JSONObject(cartResponseData);

			if (!origRespJObj.has(WakefernApplicationConstants.Mi9V8ItemLocator.Items) || storeId.length() < 1) {
				// The supplied response string does not contain any Items (products).
				// Just return the original string.
				return cartResponseData;
			}

			JSONArray itemsJArray = (JSONArray) origRespJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);

			if (itemsJArray.length() == 0) {
				// The Items Array is empty (no products).
				return cartResponseData;
			}

			JSONObject itemsJObj = new JSONObject();
			JSONObject retvalJObj = new JSONObject();

			// Set up retval with all non-items data
			for (Object key : origRespJObj.keySet()) {
				String keyStr = (String) key;

				if (!keyStr.equals(WakefernApplicationConstants.Mi9V8ItemLocator.Items)) {
					Object keyvalue = origRespJObj.get(keyStr);
					retvalJObj.put(keyStr, keyvalue);
				}
			}

			WakefernAuth auth = new WakefernAuth();
			String authString = auth.getInfo(MWGApplicationConstants
					.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));

			// Can't get Item Location Data w/o a valid Wakefern Auth String.
			if ((authString == null) || authString.isEmpty()) {
				// Failed to get a Wakefern Authentication String.
				// So we can't get Item Location Data.
				logger.error("Failed to get a Wakefern Authentication String.");
				throw new Exception("Failed to get a Wakefern Authentication String.");
			}

			String strItemSkuList = "";

			// Get the items in the array and make a comma separated string of them as well
			// trim the first and last digit
			for (int i = 0, size = itemsJArray.length(); i < size; i++) {
				JSONObject itemJObj = (JSONObject) itemsJArray.get(i);

				String itemId = itemJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
				String sku = this.updateUPC(itemId);

				if (sku.matches("[0-9]+")) {
					strItemSkuList += sku + ",";
					itemsJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.Items, itemJObj);

				}
			}

			if (itemsJObj.length() == 0) {
				// for a shopping cart has one item only which is a personal shopping note.
				// 2021-09-21 not sure if this would apply to the V8-based mobile app?
				logger.debug("itemsJObj: is empty, return to the UI now");
				return retvalJObj.toString();
			}

			itemsJArray = (JSONArray) itemsJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);
			strItemSkuList = strItemSkuList.substring(0, strItemSkuList.length() - 1); // remove trailing comma
			Mi9V8ItemLocatorArray itemLocatorArray = new Mi9V8ItemLocatorArray();

			// call Wakefern Item Locator API with
			// 1. a store id
			// 2. a list of skus with a comma as a delimiter
			// 3. JWT public key
			String locatorArray = itemLocatorArray.getInfo(storeId, strItemSkuList, authString);

			Map<Long, Object> itemLocatorData = new HashMap<>();
			Map<Long, Object> areaSeqNumData = new HashMap<>();
			Map<Long, Object> wfSectDescData = new HashMap<>();

			JSONArray jsonArray = new JSONArray(locatorArray);
			int size = jsonArray.length();

			for (int i = 0; i < size; i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_seq_num);
				Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_desc);
				JSONArray itemLocations = jsonObject
						.getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.item_locations);

				for (int j = 0; j < itemLocations.length(); j++) {
					Object upc13 = itemLocations.getJSONObject(j)
							.get(WakefernApplicationConstants.Mi9V8ItemLocator.upc_13_num);

					try { // if wf_area_code is found from item locator response
						String wfAreaCode = itemLocations.getJSONObject(j)
								.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_area_code);

						areaSeqNumData.put(Long.parseLong(upc13.toString()),
								(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum));

					} catch (Exception exx) {
						areaSeqNumData.put(Long.parseLong(upc13.toString()), areaSeqNum);
					}

					try {
						String wfSectDesc = itemLocations.getJSONObject(j)
								.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_sect_desc);
						wfSectDescData.put(Long.parseLong(upc13.toString()),
								(wfSectDesc != null && wfSectDesc.trim().equals("") ? JSONObject.NULL : wfSectDesc));
					} catch (Exception e) {
						// ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
						// Locator's API call (namely, not found)
					}

					itemLocatorData.put(Long.parseLong(upc13.toString()),
							(areaDesc != null && !areaDesc.toString().equals("null")) ? areaDesc
									: WakefernApplicationConstants.Mi9V8ItemLocator.Other);
				}
			}

			// display some trace/debug info
			if (logger.isTraceEnabled()) {
				logger.trace("itemLocatorData:");
				for (Object key : itemLocatorData.keySet()) {
					Object value = itemLocatorData.get(key);
					logger.trace("key: " + key + "; value: " + value);
				}

				logger.trace("areaSeqNumData:");
				for (Object key : areaSeqNumData.keySet()) {
					Object value = areaSeqNumData.get(key);
					logger.trace("key: " + key + "; value: " + value);
				}

				logger.trace("wfSectDescData:");
				for (Object key : wfSectDescData.keySet()) {
					Object value = wfSectDescData.get(key);
					logger.trace("key: " + key + "; value: " + value);
				}
			}

			for (int i = 0; i < itemsJArray.length(); i++) {
				JSONObject item = itemsJArray.getJSONObject(i);
				String itemId = item.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
				String upc = this.updateUPC(itemId);

				JSONObject newItemLocator = new JSONObject();

				Object areaSeqNum = areaSeqNumData.get(Long.parseLong(upc));
				int areaSeqInt = Integer.parseInt(areaSeqNum.toString());

				// aisle and aisleAreaSeqNum
				if (areaSeqInt > 0) {
					newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
							itemLocatorData.get(Long.parseLong(upc)).toString());
					newItemLocator.put("aisleAreaSeqNum", areaSeqInt);

				} else { // area_seq_num = 0, -1, or -999 - INVALID
					newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
							WakefernApplicationConstants.Mi9V8ItemLocator.Other);
					newItemLocator.put("aisleAreaSeqNum", Integer.MAX_VALUE - 100); // list before shopping personal
																					// note section
				}

				// for aisleSectionDesc
				Object wfSectDesc = wfSectDescData.get(Long.parseLong(upc));
				if (wfSectDesc == null) {
					newItemLocator.put("aisleSectionDesc", JSONObject.NULL);
				} else {
					newItemLocator.put("aisleSectionDesc", wfSectDescData.get(Long.parseLong(upc)));
				}

				item.put("itemLocator", newItemLocator);

				retvalJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.Items, item);
			}

			// pre-sort by AisleAreaSeqNum to ease the UI processing
			JSONArray itemsSortArray = (JSONArray) retvalJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);
			retvalJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Items,
					SortMi9V8ItemLocators.sortItems(itemsSortArray));

			return retvalJObj.toString();

		} catch (Exception e) {
			// Item Locator done gone and blowed up.
			// Return the original response string.
			logger.error("[updateCartItems::Exception processing item locator. The error message: "
					+ LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));
			
			return addFakeItemLocator(cartResponseData);
			//throw new Exception(LogUtil.getExceptionMessage(e));
		}
	}

	/**
	 * Apparently the SKU Wakefern expects, is slightly different from the one MWG
	 * provides. The last digit is the checksum.
	 * 
	 * @param sku
	 * @return
	 */
	private String updateUPC(String sku) {
		return sku.substring(0, sku.length() - 1);
	}
	
	
	/*
	 * add fake item locator for each sku if something goes wrong 
	 */
	private String addFakeItemLocator(String mi9ResponseData) {
		
		return mi9ResponseData;
	}
}
