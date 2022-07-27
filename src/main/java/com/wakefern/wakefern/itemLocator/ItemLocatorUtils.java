package com.wakefern.wakefern.itemLocator;

import com.wakefern.wakefern.WakefernApplicationConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.NotFoundException;

/**
 *  6/15/2022
 *  Danny Zheng
 */

public class ItemLocatorUtils {
	
	private final static Logger logger = LogManager.getLogger(ItemLocatorUtils.class);

	/**
	 * Calculate the number of requests to made to the Item Locator API based on the number of items.
	 * @param itemsSize
	 * @return
	 */
	public static int getNumRequests(final int itemsSize) {
		int numRequests = itemsSize / WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE;
		if (itemsSize % WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE > 0) {
			numRequests++;
		}

		logger.trace("ITEM_PARTITION_SIZE: " + WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE);

		return numRequests;
	}

	/**
	 * Create API response data with three fields aisleAreaSeqNum, aisleSectionDesc, aisle
	 * EX: {"aisleAreaSeqNum":23,"aisleSectionDesc":"BACON & BREAKFAST MEATS (DAIRY)","aisle":"DAIRY DEPARTMENT"}
	 * @param areaSeqInt
	 * @param wfSectDescData
	 * @param itemLocatorData
	 * @return
	 */
	private static JSONObject createItemLocatorObj(int areaSeqInt, Object wfSectDescData, Object itemLocatorData) {
		JSONObject newItemLocator = new JSONObject();

		// aisle and aisleAreaSeqNum
		if (areaSeqInt > 0) {
			newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
					itemLocatorData.toString());
			newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum, areaSeqInt);

		} else { // area_seq_num = 0, -1, or -999 - INVALID
			newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
					WakefernApplicationConstants.Mi9V8ItemLocator.Other);
			newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum,
					Integer.MAX_VALUE - 100);
		}

		// for aisleSectionDesc
		if (wfSectDescData == null) {
			newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc, JSONObject.NULL);
		} else {
			newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc,
					wfSectDescData);
		}

		return newItemLocator;
	}

	/**
	 * Generate an Item Location object for one UPC, given the upstream response
	 *
	 * @return
	 */
	public static JSONObject generateItemLocatorForUpc(String upc, String itemLocatorResponse) {
		JSONArray itemsJArray = new JSONArray(itemLocatorResponse);

		for (int i = 0; i < itemsJArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) itemsJArray.get(i);

			Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_seq_num);
			Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_desc);
			JSONArray itemLocations = jsonObject
					.getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.item_locations);

			for (int j = 0; j < itemLocations.length(); j++) {
				Object itemFromDB = itemLocations.getJSONObject(j)
						.get(WakefernApplicationConstants.Mi9V8ItemLocator.upc_13_num);

				if (itemFromDB.toString().contentEquals(upc)) continue;

				int areaSeqInt;

				try { // if wf_area_code is found from item locator response
					String wfAreaCode = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_area_code);

					areaSeqInt = Integer.parseInt(
							(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum.toString()));

				} catch (Exception exx) {
					areaSeqInt = Integer.parseInt(areaSeqNum.toString());
				}

				Object wfSectDesc = JSONObject.NULL;
				try {
					String wfSectDescStr = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_sect_desc);
					wfSectDesc =
							wfSectDescStr != null && wfSectDescStr.trim().equals("") ? JSONObject.NULL : wfSectDescStr;
				} catch (Exception e) {
					// ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
					// Locator's API call (namely, not found)
				}

				Object itemLocator =
						areaDesc != null && !areaDesc.toString().equals("null") ? areaDesc
								: WakefernApplicationConstants.Mi9V8ItemLocator.Other;

				return createItemLocatorObj(areaSeqInt, wfSectDesc, itemLocator);
			}
		}

		throw new NotFoundException("Item Locator not found for upc " + upc);
	}


	/**
	 * Get Item Location data for each SKU in a cart by using a partition of certain SKUs
	 * @param skuList
	 * @param itemslocatorResponse
	 * @return
	 */
	public static Map<String, JSONObject> generateItemLocatorMap(List<String> skuList, String itemslocatorResponse) {
		Map<String, JSONObject> itemsMap = new HashMap<>();
		Map<Long, Object> itemLocatorData = new HashMap<>();
		Map<Long, Object> areaSeqNumData = new HashMap<>();
		Map<Long, Object> wfSectDescData = new HashMap<>();

		JSONArray itemsJArray = new JSONArray(itemslocatorResponse);

		for (int i = 0; i < itemsJArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) itemsJArray.get(i);
			Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_seq_num);
			Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_desc);
			JSONArray itemLocations = jsonObject
					.getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.item_locations);

			for (int j = 0; j < itemLocations.length(); j++) {
				Object itemFromDB = itemLocations.getJSONObject(j)
						.get(WakefernApplicationConstants.Mi9V8ItemLocator.upc_13_num);

				final Long upc13 = Long.parseLong(itemFromDB.toString());

				try { // if wf_area_code is found from item locator response
					String wfAreaCode = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_area_code);

					areaSeqNumData.put(upc13,
							(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum));

				} catch (Exception exx) {
					areaSeqNumData.put(upc13, areaSeqNum);
				}

				try {
					String wfSectDesc = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_sect_desc);
					wfSectDescData.put(upc13,
							(wfSectDesc != null && wfSectDesc.trim().equals("") ? JSONObject.NULL : wfSectDesc));
				} catch (Exception e) {
					// ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
					// Locator's API call (namely, not found)
				}

				itemLocatorData.put(upc13,
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

		for (String item : skuList) {
			final String sku = item.trim();
			final long skuLng = Long.parseLong(sku);

			Object areaSeqNum = areaSeqNumData.get(skuLng);
			int areaSeqInt = Integer.parseInt(areaSeqNum.toString());

			Object wfSectDesc = wfSectDescData.get(skuLng);
			Object itemLocator = itemLocatorData.get(skuLng);

			itemsMap.put(item, createItemLocatorObj(areaSeqInt, wfSectDesc, itemLocator));
		}
		return itemsMap;
	}

	/*
	 * 02/03/2020 Purpose: to sort items by AisleAreaSeqNum before return to the UI.
	 * UI would display as the default; sorting in UI for some old phones is slow.
	 */
	public static JSONArray sortItems(JSONArray jsonArray) {
		List<JSONObject> list = new ArrayList<>();

		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getJSONObject(i));
		}

		Collections.sort(list, new Mi9V8AisleAreaJSONComparator());

		JSONArray array = new JSONArray();
        for (JSONObject jsonObject : list) {
            array.put(jsonObject);
        }
		
		return array;
	}

}

class Mi9V8AisleAreaJSONComparator implements Comparator<JSONObject> {

	@Override
	public int compare(JSONObject o1, JSONObject o2) {
		int seq1 = o1.getJSONObject("itemLocator").getInt("aisleAreaSeqNum");
		int seq2 = o2.getJSONObject("itemLocator").getInt("aisleAreaSeqNum");
		
		if (seq1 < 0) {
			return 1;
		} else {
			return (seq1 - seq2);
		}
	}

}
