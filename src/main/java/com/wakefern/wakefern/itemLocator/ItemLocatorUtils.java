package com.wakefern.wakefern.itemLocator;

import com.wakefern.logging.LogUtil;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.NotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 6/15/2022
 * Danny Zheng
 */

public class ItemLocatorUtils {

    private final static Logger logger = LogManager.getLogger(ItemLocatorUtils.class);

    /**
     * Calculate the number of requests to made to the Item Locator API based on the number of items.
     *
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
     *
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
        final long upcLong = Long.parseLong(upc);

        for (int i = 0; i < itemsJArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) itemsJArray.get(i);

            Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_seq_num);
            Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_desc);
            JSONArray itemLocations = jsonObject
                    .getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.item_locations);

            for (int j = 0; j < itemLocations.length(); j++) {
                final long itemUpc = itemLocations.getJSONObject(j)
                        .getLong(WakefernApplicationConstants.Mi9V8ItemLocator.upc_13_num);

                // Skip if the UPC does not match
                if (itemUpc != upcLong) continue;

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
     *
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
			if (areaSeqNum == null) {
				continue;
			}
            int areaSeqInt = Integer.parseInt(areaSeqNum.toString());

            Object wfSectDesc = wfSectDescData.get(skuLng);
            Object itemLocator = itemLocatorData.get(skuLng);

            itemsMap.put(item, createItemLocatorObj(areaSeqInt, wfSectDesc, itemLocator));
        }
        return itemsMap;
    }

    /**
     * Create a "dummy" ItemLocator object
     *
     * @param itemJObj the JSONObject to add the object to.
     */
    public static void addDummyItemLocatorObj(JSONObject itemJObj) {
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
    public static String removeCheckSumDigit(String sku) {
        return sku.substring(0, sku.length() - 1);
    }


    /*
     * add empty item locator for each sku if something goes wrong
     */
    public static String addEmptyItemLocator(String mi9ResponseData, boolean isCart) throws Exception {
        try {
            JSONObject origRespJObj = new JSONObject(mi9ResponseData);

            final String itemProp = isCart ? WakefernApplicationConstants.Mi9V8ItemLocator.LineItems
                    : WakefernApplicationConstants.Mi9V8ItemLocator.Items;
            JSONArray itemsJArray = (JSONArray) origRespJObj.get(itemProp);

            JSONObject retvalJObj = new JSONObject();

            // Set up retval with all non-items data
            for (String key : origRespJObj.keySet()) {
                if (!key.equals(itemProp)) {
                    Object keyvalue = origRespJObj.get(key);
                    retvalJObj.put(key, keyvalue);
                }
            }

            for (int i = 0; i < itemsJArray.length(); i++) {
                JSONObject item = itemsJArray.getJSONObject(i);
                addDummyItemLocatorObj(item);
                retvalJObj.append(itemProp, item);
            }

            return retvalJObj.toString();
        } catch (Exception e) {
            logger.error("[addEmptyItemLocator::Exception adding item locator. The error message: "
                    + LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));

            throw new Exception(LogUtil.getExceptionMessage(e));
        }
    }
}


