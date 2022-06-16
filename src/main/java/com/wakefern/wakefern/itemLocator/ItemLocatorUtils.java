package com.wakefern.wakefern.itemLocator;

import com.wakefern.wakefern.WakefernApplicationConstants;

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
	 * Get Item Location data for each SKU in a cart by using a partition of certain SKUs
	 * 
	 * 
	 */
	public static Map<String, JSONObject> generateItemLocator(List<String> itemList, String itemslocatorResponse) throws Exception {

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

		for (int i = 0; i < itemList.size(); i++) {
			String item = itemList.get(i).trim().toString();

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

}
