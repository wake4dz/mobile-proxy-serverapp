package com.wakefern.wakefern.itemLocator;

import com.wakefern.api.proxy.wakefern.itemLocator.ItemLocatorDto;
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
     * Generate an Item Location object for one UPC, given the upstream response
     *
     * @return
     */
    public static JSONObject generateItemLocatorForUpc(String upc, String itemLocatorResponse) {
        JSONArray itemsJArray = new JSONArray(itemLocatorResponse);
        final long upcLong = Long.parseLong(upc);

        for (int i = 0; i < itemsJArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) itemsJArray.get(i);

            Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.AreaSeqNum);
            Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.AreaDesc);
            JSONArray itemLocations = jsonObject
                    .getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocations);

            for (int j = 0; j < itemLocations.length(); j++) {
                final long itemUpc = itemLocations.getJSONObject(j)
                        .getLong(WakefernApplicationConstants.Mi9V8ItemLocator.Upc13Num);

                // Skip if the UPC does not match
                if (itemUpc != upcLong) continue;

                int areaSeqInt;

                try { // if wf_area_code is found from item locator response
                    String wfAreaCode = itemLocations.getJSONObject(j)
                            .getString(WakefernApplicationConstants.Mi9V8ItemLocator.WfAreaCode);

                    areaSeqInt = Integer.parseInt(
                            (wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum.toString()));

                } catch (Exception exx) {
                    areaSeqInt = Integer.parseInt(areaSeqNum.toString());
                }

                String aisle =
                        areaDesc != null && !areaDesc.toString().equals("null") ? areaDesc.toString()
                                : WakefernApplicationConstants.Mi9V8ItemLocator.Other;
                
                String aisleStore = null;
                try {
                    String storeAreaDescStr = itemLocations.getJSONObject(j)
                            .getString(WakefernApplicationConstants.Mi9V8ItemLocator.StoreAreaDesc);
                    aisleStore =
                    		storeAreaDescStr != null && storeAreaDescStr.trim().equals("") ? null : storeAreaDescStr;
                } catch (Exception e) {
                    // ignore if input item doesn't have "area_desc" data from Wakefern Item
                    // Locator's API call (namely, not found)
                }
                
                String wfSectDescStr = null;
                try {
                    wfSectDescStr = itemLocations.getJSONObject(j)
                            .getString(WakefernApplicationConstants.Mi9V8ItemLocator.WfSectDesc);
                    wfSectDescStr =
                            wfSectDescStr != null && wfSectDescStr.trim().equals("") ? null : wfSectDescStr;
                } catch (Exception e) {
                    // ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
                    // Locator's API call (namely, not found)
                }

                // section shelf number
                int sectionShelfNum = 0;
                try {
                	Object sectionShelfNumObj = itemLocations.getJSONObject(j)
                            .get(WakefernApplicationConstants.Mi9V8ItemLocator.SectionShelfNum);
                    
                	sectionShelfNum = Integer.parseInt(sectionShelfNumObj.toString());
                	
                    logger.trace("sectionShelfNum: " + sectionShelfNum);
      
                } catch (Exception e) {
                	sectionShelfNum = 0;
                }
                
                // shelf position number
                int shelfPositonfNum = 0;
                try {
                	Object shelfPositionNumObj = itemLocations.getJSONObject(j)
                            .get(WakefernApplicationConstants.Mi9V8ItemLocator.ShelfPositionNum);
                    
                	shelfPositonfNum = Integer.parseInt(shelfPositionNumObj.toString());
                	
                    logger.trace("shelfPositonfNum: " + shelfPositonfNum);
                    
                } catch (Exception e) {
                	shelfPositonfNum = 0;
                }

                ItemLocatorDto itemLocatorDto = new ItemLocatorDto();
                itemLocatorDto.setAreaSeqNum(areaSeqInt);
                itemLocatorDto.setAisle(aisle);
                itemLocatorDto.setAisleStore(aisleStore);
                itemLocatorDto.setSectionDesc(wfSectDescStr);
                itemLocatorDto.setSectionShelfNum(sectionShelfNum);
                itemLocatorDto.setShelfPositionNum(shelfPositonfNum);
                
                return ItemLocatorDto.createItemLocatorObj(itemLocatorDto);
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
       
        Map<Long, ItemLocatorDto> itemLocatorMap = new HashMap<>();

        JSONArray itemsJArray = new JSONArray(itemslocatorResponse);

        for (int i = 0; i < itemsJArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) itemsJArray.get(i);
            Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.AreaSeqNum);
            Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.AreaDesc);
            JSONArray itemLocations = jsonObject
                    .getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocations);

            for (int j = 0; j < itemLocations.length(); j++) {
            	ItemLocatorDto itemLocatorDto = new ItemLocatorDto();
            	
                Object itemFromDB = itemLocations.getJSONObject(j)
                        .get(WakefernApplicationConstants.Mi9V8ItemLocator.Upc13Num);

                final Long upc13 = Long.parseLong(itemFromDB.toString());

                // areaSeqNum
                try { // if wf_area_code is found from item locator response
                    String wfAreaCode = itemLocations.getJSONObject(j)
                            .getString(WakefernApplicationConstants.Mi9V8ItemLocator.WfAreaCode);

                    itemLocatorDto.setAreaSeqNum(
                            (wfAreaCode != null && wfAreaCode.trim().equals("0") ? 0 : Integer.parseInt(areaSeqNum.toString())));                    

                } catch (Exception exx) {

                }
                
                // aisle
                itemLocatorDto.setAisle(
                        (areaDesc != null && !areaDesc.toString().equals("null")) ? areaDesc.toString()
                                : WakefernApplicationConstants.Mi9V8ItemLocator.Other);
                
                // aisleStore
                try {
                    String storeAreaDesc = itemLocations.getJSONObject(j)
                            .getString(WakefernApplicationConstants.Mi9V8ItemLocator.StoreAreaDesc);
                    
                    itemLocatorDto.setAisleStore(
                            (storeAreaDesc != null && storeAreaDesc.trim().equals("") ? null : storeAreaDesc));
                } catch (Exception e) {
                    // ignore input item doesn't have "area_desc" data from Wakefern Item
                    // Locator's API call (namely, not found)
                }
                
                // section description
                try {
                    String wfSectDesc = itemLocations.getJSONObject(j)
                            .getString(WakefernApplicationConstants.Mi9V8ItemLocator.WfSectDesc);
                    
                    itemLocatorDto.setSectionDesc(
                            (wfSectDesc != null && wfSectDesc.trim().equals("") ? null : wfSectDesc));
                } catch (Exception e) {
                    // ignore input item doesn't have "wf_sect_desc" data from Wakefern Item
                    // Locator's API call (namely, not found)
                }
                
                
                // section shelf number
                int sectionShelfNum = 0;
                try {
                	Object sectionShelfNumObj = itemLocations.getJSONObject(j)
                            .get(WakefernApplicationConstants.Mi9V8ItemLocator.SectionShelfNum);
                    
                	sectionShelfNum = Integer.parseInt(sectionShelfNumObj.toString());
                	itemLocatorDto.setSectionShelfNum(sectionShelfNum);
                
                    logger.trace("sectionShelfNum: " + sectionShelfNum);
      
                } catch (Exception e) {
                	itemLocatorDto.setSectionShelfNum(0);
                }
                
                // shelf position number
                int shelfPositonfNum = 0;
                try {
                	Object shelfPositionNumObj = itemLocations.getJSONObject(j)
                            .get(WakefernApplicationConstants.Mi9V8ItemLocator.ShelfPositionNum);
                    
                	shelfPositonfNum = Integer.parseInt(shelfPositionNumObj.toString());
                	itemLocatorDto.setShelfPositionNum(shelfPositonfNum);
                    
                	logger.trace("shelfPositonfNum: " + shelfPositonfNum);
                    
                } catch (Exception e) {
                	itemLocatorDto.setShelfPositionNum(0);
                }
                
                
                itemLocatorMap.put(upc13, itemLocatorDto);

            }
        }

        // display some trace/debug info
        if (logger.isTraceEnabled()) {
            logger.trace("itemLocatorMap:");
            for (Object key : itemLocatorMap.keySet()) {
                Object value = itemLocatorMap.get(key);
                logger.trace("key: " + key + "; value: " + value);
            }
        }

        for (String item : skuList) {
            final String sku = item.trim();
            final long skuLng = Long.parseLong(sku);
            
            itemsMap.put(item, ItemLocatorDto.createItemLocatorObj(itemLocatorMap.get(skuLng)));
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

        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleStore, WakefernApplicationConstants.Mi9V8ItemLocator.Other);
        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionShelfNum, 0);
        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleShelfPositionNum, 0);
        
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


