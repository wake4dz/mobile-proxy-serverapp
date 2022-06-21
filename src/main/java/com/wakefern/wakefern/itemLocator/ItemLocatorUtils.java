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
	 * Generate an Item Location object for one UPC, given the upstream response
	 *
	 * @return
	 */
	public static JSONObject generateItemLocator(String upc, String itemlocatorResponse) {
		// TODO: refactor this method to create the nice looking API response object
		// Like this: {"aisleAreaSeqNum":23,"aisleSectionDesc":"BACON & BREAKFAST MEATS (DAIRY)","aisle":"DAIRY DEPARTMENT"}
		// Constants for the keys of the item locator API response object are defined in WakefernApplicationConstants.Mi9V8ItemLocator
		// They are used below.
		JSONObject ret = new JSONObject();

		Map<Long, Object> itemLocatorData = new HashMap<>();
		Map<Long, Object> areaSeqNumData = new HashMap<>();
		Map<Long, Object> wfSectDescData = new HashMap<>();

		JSONArray itemsJArray = new JSONArray(itemlocatorResponse);

		for (int i = 0; i < itemsJArray.length(); i++) {
			JSONObject jsonObject = (JSONObject) itemsJArray.get(i);

			Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_seq_num);
			Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_desc);
			JSONArray itemLocations = jsonObject
					.getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.item_locations);

			for (int j = 0; j < itemLocations.length(); j++) {
				Object itemFromDB = itemLocations.getJSONObject(j)
						.get(WakefernApplicationConstants.Mi9V8ItemLocator.upc_13_num);

				try { // if wf_area_code is found from item locator response
					String wfAreaCode = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_area_code);

					areaSeqNumData.put(Long.parseLong(itemFromDB.toString()),
							(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum));

				} catch (Exception exx) {
					areaSeqNumData.put(Long.parseLong(itemFromDB.toString()), areaSeqNum);
				}

				try {
					String wfSectDesc = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_sect_desc);
					wfSectDescData.put(Long.parseLong(itemFromDB.toString()),
							(wfSectDesc != null && wfSectDesc.trim().equals("") ? JSONObject.NULL : wfSectDesc));
				} catch (Exception e) {
					// ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
					// Locator's API call (namely, not found)
				}

				itemLocatorData.put(Long.parseLong(itemFromDB.toString()),
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

		/*for (String s : itemList) {
			String item = s.trim();

			JSONObject newItemLocator = new JSONObject();

			Object areaSeqNum = areaSeqNumData.get(Long.parseLong(item));
			int areaSeqInt = Integer.parseInt(areaSeqNum.toString());

			// aisle and aisleAreaSeqNum
			if (areaSeqInt > 0) {
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
						itemLocatorData.get(Long.parseLong(item)).toString());
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum, areaSeqInt);

			} else { // area_seq_num = 0, -1, or -999 - INVALID
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
						WakefernApplicationConstants.Mi9V8ItemLocator.Other);
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum,
						Integer.MAX_VALUE - 100);
			}

			// for aisleSectionDesc
			Object wfSectDesc = wfSectDescData.get(Long.parseLong(item));
			if (wfSectDesc == null) {
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc, JSONObject.NULL);
			} else {
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc,
						wfSectDescData.get(Long.parseLong(item)));
			}

			ret.put(item, newItemLocator);
		}*/
		return ret;
	}


	/**
	 * Get Item Location data for each SKU in a cart by using a partition of certain SKUs
	 * @param itemList
	 * @param itemslocatorResponse
	 * @return
	 */
	public static Map<String, JSONObject> generateItemLocator(List<String> itemList, String itemslocatorResponse) {

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

				try { // if wf_area_code is found from item locator response
					String wfAreaCode = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_area_code);

					areaSeqNumData.put(Long.parseLong(itemFromDB.toString()),
							(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum));

				} catch (Exception exx) {
					areaSeqNumData.put(Long.parseLong(itemFromDB.toString()), areaSeqNum);
				}

				try {
					String wfSectDesc = itemLocations.getJSONObject(j)
							.getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_sect_desc);
					wfSectDescData.put(Long.parseLong(itemFromDB.toString()),
							(wfSectDesc != null && wfSectDesc.trim().equals("") ? JSONObject.NULL : wfSectDesc));
				} catch (Exception e) {
					// ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
					// Locator's API call (namely, not found)
				}

				itemLocatorData.put(Long.parseLong(itemFromDB.toString()),
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

		for (String s : itemList) {
			String item = s.trim();

			JSONObject newItemLocator = new JSONObject();

			Object areaSeqNum = areaSeqNumData.get(Long.parseLong(item));
			int areaSeqInt = Integer.parseInt(areaSeqNum.toString());

			// aisle and aisleAreaSeqNum
			if (areaSeqInt > 0) {
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
						itemLocatorData.get(Long.parseLong(item)).toString());
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum, areaSeqInt);

			} else { // area_seq_num = 0, -1, or -999 - INVALID
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle,
						WakefernApplicationConstants.Mi9V8ItemLocator.Other);
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum,
						Integer.MAX_VALUE - 100);
			}

			// for aisleSectionDesc
			Object wfSectDesc = wfSectDescData.get(Long.parseLong(item));
			if (wfSectDesc == null) {
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc, JSONObject.NULL);
			} else {
				newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc,
						wfSectDescData.get(Long.parseLong(item)));
			}

			itemsMap.put(item, newItemLocator);
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
