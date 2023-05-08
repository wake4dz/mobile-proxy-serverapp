package com.wakefern.wakefern.itemLocator;

import com.wakefern.api.proxy.wakefern.itemLocator.ItemLocatorCache;
import com.wakefern.api.proxy.wakefern.itemLocator.ItemLocatorDto;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.EnvManager;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
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
    private static int getNumRequests(final int itemsSize) {
        final int partitionSize = EnvManager.getItemLocatorPartitionSize();
        int numRequests = itemsSize / partitionSize;
        if (itemsSize % partitionSize > 0) {
            numRequests++;
        }

        logger.trace("ITEM_PARTITION_SIZE: " + partitionSize);

        return numRequests;
    }

    /**
     * Generate an Item Location object for one UPC, given the upstream response
     *
     * @return
     */
    public static ItemLocatorDto generateItemLocatorForUpc(String upc, String itemLocatorResponse) {
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
                
                return itemLocatorDto;
            }
        }

        throw new NotFoundException("Item Locator not found for upc " + upc);
    }


    /**
     * Get Item Location data for each SKU in a cart by using a partition of certain SKUs
     *
     * @param skuList
     * @param itemLocatorResponse
     * @return
     */
    public static Map<String, JSONObject> generateItemLocatorMap(List<String> skuList, String itemLocatorResponse) {
        Map<String, JSONObject> itemsMap = new HashMap<>();
       
        Map<Long, ItemLocatorDto> itemLocatorMap = new HashMap<>();

        JSONArray itemsJArray = new JSONArray(itemLocatorResponse);

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
                        (areaDesc != null && !areaDesc.toString().equals("null") && !areaDesc.toString().equalsIgnoreCase("not found")) ? areaDesc.toString()
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
            
            itemsMap.put(item, itemLocatorMap.get(skuLng).toJSON());
        }
        
        return itemsMap;
    }

    /**
     * Create a "Not Found" ItemLocator object
     *
     * @param itemJObj the JSONObject to add the object to.
     */
    private static void addDummyItemLocatorObj(JSONObject itemJObj) {
        itemJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocator, generateEmptyItemLocator());
    }

    private static JSONObject generateEmptyItemLocator() {
        JSONObject dummyObj = new JSONObject();

        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle, WakefernApplicationConstants.Mi9V8ItemLocator.Other);
        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionDesc, JSONObject.NULL);
        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleAreaSeqNum, 888888);

        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleStore, WakefernApplicationConstants.Mi9V8ItemLocator.Other);
        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleSectionShelfNum, 0);
        dummyObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.AisleShelfPositionNum, 0);
        return dummyObj;
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


    /**
     * add Not Found item locator for each sku if something goes wrong
     * @param mi9ResponseData
     * @param responseType
     * @return
     * @throws Exception
     */
    @Deprecated
    private static String addEmptyItemLocator(String mi9ResponseData, ResponseType responseType) throws Exception {
        try {
            JSONObject origRespJObj = new JSONObject(mi9ResponseData);

            final String itemProp = responseType == ResponseType.CART ? WakefernApplicationConstants.Mi9V8ItemLocator.LineItems
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

    @Deprecated
    public enum ResponseType {
        CART,
        PLANNING_LIST
    }

    /**
     * Decorate a collection of products with item locator information (based on ResponseType)
     * @param storeId
     * @param response
     * @param responseType
     * @return
     * @throws Exception
     */
    @Deprecated
    public static String decorateCollectionWithItemLocations(String storeId, String response, ResponseType responseType) throws Exception {
        try {
            final String itemsKey = responseType == ResponseType.CART ?
                    WakefernApplicationConstants.Mi9V8ItemLocator.LineItems
                    : WakefernApplicationConstants.Mi9V8ItemLocator.Items;

            JSONObject origRespJObj = new JSONObject(response);
            JSONArray itemsJArray = (JSONArray) origRespJObj.get(itemsKey);
            final int itemsSize = itemsJArray.length();

            if (!origRespJObj.has(itemsKey) || storeId.length() < 1 || itemsSize == 0) {
                // The supplied response string does not contain any Items (products).
                // OR The Items Array is empty (no products).
                // Just return the original string.
                return response;
            }

            JSONObject retvalJObj = new JSONObject();

            // Set up retval with all non-items data
            for (String key : origRespJObj.keySet()) {
                if (!key.equals(itemsKey)) {
                    Object keyvalue = origRespJObj.get(key);
                    retvalJObj.put(key, keyvalue);
                }
            }

            Map<String, JSONObject> processedPartitionItems;
            Map<String, JSONObject> itemsMap = new HashMap<>();

            int currentListPosition = 0;

            final int numRequests = getNumRequests(itemsSize);
            List<String> partitionItemsList;
            StringBuilder partitionItemsSB;

            // Get auth token
            final String authToken = WakefernAuth.getAuthToken(EnvManager.getJwtPublicKey());

            for (int i=0; i < numRequests; i++) {
                partitionItemsList = new ArrayList<>();
                partitionItemsSB = new StringBuilder();

                // build each partition data to be used for a Wakefern's Item Locator API call
                while ((EnvManager.getItemLocatorPartitionSize() * (i + 1) > currentListPosition) && (itemsSize > currentListPosition)) {
                    JSONObject itemJObj = itemsJArray.getJSONObject(currentListPosition);

                    final String upc = getUpcFromSkuObject(itemJObj);

                    currentListPosition++;
                    ItemLocatorDto cached = ItemLocatorCache.getInstance().get(storeId, upc);
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
                wkfn.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
                wkfn.put("Authentication", authToken);
                // Call APIM Gateway to avoid any foreign IP addresses
                wkfn.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());

                String responseData = HTTPRequest.executeGet(path, wkfn, EnvManager.getApiMediumTimeout());

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

                final String upc = getUpcFromSkuObject(itemJObj);

                if (itemsMap.get(upc) != null) {
                    JSONObject itemLocator = itemsMap.get(upc);
                    itemJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.ItemLocator, itemLocator);

                    // add to cache
                    ItemLocatorCache.getInstance().add(storeId, upc, ItemLocatorDto.fromJSON(itemLocator));
                } else { //defense code to add dummy data
                    ItemLocatorUtils.addDummyItemLocatorObj(itemJObj);
                }

                retvalJObj.append(itemsKey, itemJObj);
            }

            return retvalJObj.toString();

        } catch (Exception e) {
            logger.error("decorateCollectionWithItemLocations::Exception processing item locator. The error message: "
                    + LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));

            // if there is an exception, we try to return the Mi9 shopping cart info + empty item locator for each sku.
            // if addEmptyItemLocator() also throw an exception, the caller would get a HTTP 500 with a brief error message
            return addEmptyItemLocator(response, responseType);
        }
    }

    /**
     * Utility method to get the upc from an item locator sku
     * @param skuObject
     * @return
     */
    private static String getUpcFromSkuObject(JSONObject skuObject) {
       final String sku = skuObject.getString(WakefernApplicationConstants.Mi9V8ItemLocator.Sku);
       return removeCheckSumDigit(sku);
    }


    /**
     * Decorate a collection of products with item locator information (based on ResponseType)
     * @param storeId
     * @param skus
     * @return
     * @throws Exception
     */
    public static String batchRequest(String storeId, JSONArray skus) throws Exception {
        try {
            if (skus == null || storeId.length() < 1) {
                throw new Exception("No upcs in payload");
            }

            final int numSkus = skus.length();

            if (numSkus == 0) {
                throw new Exception("No upcs in payload");
            }

            // Our final response object
            JSONObject response = new JSONObject();

            Map<String, JSONObject> processedPartitionItems;
            Map<String, JSONObject> itemsMap = new HashMap<>();

            int currentListPosition = 0;

            final int numRequests = getNumRequests(numSkus);
            List<String> partitionItemsList;
            StringBuilder partitionItemsSB;

            // Get auth token
            final String authToken = WakefernAuth.getAuthToken(EnvManager.getJwtPublicKey());

            for (int i=0; i < numRequests; i++) {
                partitionItemsList = new ArrayList<>();
                partitionItemsSB = new StringBuilder();

                // build each partition data to be used for a Wakefern's Item Locator API call
                while ((EnvManager.getItemLocatorPartitionSize() * (i + 1) > currentListPosition) && (numSkus > currentListPosition)) {
                    final String upc = removeCheckSumDigit(skus.getString(currentListPosition));

                    currentListPosition++;
                    ItemLocatorDto cached = ItemLocatorCache.getInstance().get(storeId, upc);
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

                Map<String, String> headers = new HashMap<>();
                headers.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
                headers.put("Authentication", authToken);
                // Call APIM Gateway to avoid any foreign IP addresses
                headers.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getMobilePassThruApiKeyProd());

                String responseData = HTTPRequest.executeGet(path, headers, EnvManager.getApiMediumTimeout());

                logger.trace("partitionNumber: " + (i + 1));
                logger.trace("URL path: " + path);
                logger.trace("PartitionItemsSB: " + partitionItemsSB);
                logger.trace("PartitionItemsList: " + partitionItemsList);
                logger.trace("responseData: " + responseData);

                processedPartitionItems = ItemLocatorUtils.generateItemLocatorMap(partitionItemsList, responseData);

                for (Map.Entry<String, JSONObject> entry : processedPartitionItems.entrySet()) {
                    // build up the entire map for lookup later
                    itemsMap.put(entry.getKey(), entry.getValue());
                    // add the value to the cache
                    ItemLocatorCache.getInstance().add(storeId, entry.getKey(), ItemLocatorDto.fromJSON(entry.getValue()));
                }
            }

            for (int i=0; i < numSkus; i++) {
                final String sku = skus.getString(i);
                final String upc = removeCheckSumDigit(sku);

                JSONObject itemLocator = itemsMap.get(upc);
                response.put(upc, itemLocator == null ? generateEmptyItemLocator() : itemLocator);
            }

            return response.toString();

        } catch (Exception e) {
            logger.error("decorateCollectionWithItemLocations::Exception processing item locator. The error message: "
                    + LogUtil.getExceptionMessage(e) + ", exception location: " + LogUtil.getRelevantStackTrace(e));

            // if there is an exception, we try to return the Mi9 shopping cart info + empty item locator for each sku.
            // if addEmptyItemLocator() also throw an exception, the caller would get a HTTP 500 with a brief error message
            throw e;
        }
    }
}


