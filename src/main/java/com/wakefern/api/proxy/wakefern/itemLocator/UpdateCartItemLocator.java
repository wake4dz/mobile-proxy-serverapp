package com.wakefern.api.proxy.wakefern.itemLocator;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.api.wakefern.items.location.SortItemLocators;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorArray;

@Path(ApplicationConstants.Requests.Proxy + "/cartitemlocator")
public class UpdateCartItemLocator extends BaseService {
	private final static Logger logger = Logger.getLogger(UpdateCartItemLocator.class);

	@POST
	@Produces(MWGApplicationConstants.Headers.json)
	@Path("/item/location/{storeId}")
	public Response updateItem(
			@PathParam("storeId") String storeId,
			String jsonString)
			throws Exception
	{
		try {
			//jsonString is the JSON response data from calling Mi9's GetContents() API for a shopping cart data
			//then updateCartItems() is to call Wakefern's ItemLocator API to get additional data and sort the items in a way
			// that suits with the UI app requirements.
			String response = updateCartItems(jsonString, storeId);
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_UPDATE_CART_ITEM_LOCATOR);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"inputBody", jsonString,
					"storeId", storeId);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
	
    /**
     * Get Item Location data and add it to the Shopping Cart JSON Response Data.
     * 
     * 	 Mi9 returns a JSON response with "Items" property with all items in a shopping cart.
     *   For each SKU, we remove the check digit, string them with a comma, and call Wakefern Item Locator's API with a wakefern store id
     *   Add/update these 3 properties
     *   	Aisle            → wf_area_desc
	 *      AisleAreaSeqNum  → area_seq_num
	 *		AisleSectionDesc → wf_sect_desc
     * 
     * 	 Sort all SKUs with AisleAreaSeqNum asc order
     * 	 Lastly, return the sorted JSON string back to the UI to display 
     * 
     */
	protected String updateCartItems(String cartResponseData, String storeId) {
		try {
			JSONObject origRespJObj = new JSONObject(cartResponseData);
			storeId = (storeId == null) ? "" : storeId;

			if (origRespJObj.has(WakefernApplicationConstants.ItemLocator.Items) && storeId.length() > 0) {
				JSONArray itemsJArray = (JSONArray) origRespJObj.get(WakefernApplicationConstants.ItemLocator.Items);
				
				if (itemsJArray.length() > 0) {				
					JSONObject itemsJObj  = new JSONObject();
					JSONObject retvalJObj = new JSONObject();
	
					// Set up retval with all non-items data
					for (Object key : origRespJObj.keySet()) {
						String keyStr = (String) key;
						
						if (!keyStr.equals(WakefernApplicationConstants.ItemLocator.Items)) {
							Object keyvalue = origRespJObj.get(keyStr);
							retvalJObj.put(keyStr, keyvalue);
						}
					}

					WakefernAuth auth = new WakefernAuth();
					String authString = auth.getInfo(
							MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
					
					// Can't get Item Location Data w/o a valid Wakefern Auth String.
					if (!authString.isEmpty()) {
						String strItemSkuList = "";
						
						// Get the items in the array and make a comma separated string of them as well trim the first and last digit
						for (int i = 0, size = itemsJArray.length(); i < size; i++) {
							JSONObject itemJObj = (JSONObject) itemsJArray.get(i);
							
							String itemId = itemJObj.get(WakefernApplicationConstants.ItemLocator.Sku).toString();
							String sku = this.updateUPC(itemId);
							
							if (sku.matches("[0-9]+")) {
								strItemSkuList += sku + ",";
								itemsJObj.append(WakefernApplicationConstants.ItemLocator.Items, itemJObj);
							
							} else {
								itemJObj.put(WakefernApplicationConstants.ItemLocator.Aisle, WakefernApplicationConstants.ItemLocator.Other);
								
								// for a shopping cart note, not a real item
								itemJObj.put("AisleAreaSeqNum", Integer.MAX_VALUE);
								itemJObj.put("AisleSectionDesc", JSONObject.NULL);
								
								retvalJObj.append(WakefernApplicationConstants.ItemLocator.Items, itemJObj);
							}
						}

						
						if (itemsJObj.length() == 0) {
							// for a shopping cart has one item only which is a personal shopping note.
							logger.debug("itemsJObj: is empty, return to the UI now");
							return retvalJObj.toString();
						}
						
						itemsJArray = (JSONArray) itemsJObj.get(WakefernApplicationConstants.ItemLocator.Items);
						strItemSkuList = strItemSkuList.substring(0, strItemSkuList.length() - 1); //remove trailing comma
						ItemLocatorArray itemLocatorArray = new ItemLocatorArray();
						String locatorArray = itemLocatorArray.getInfo(storeId, strItemSkuList, authString);
						
						Map<Long, Object> itemLocatorData = new HashMap<>();
						Map<Long, Object> areaSeqNumData = new HashMap<>();
						Map<Long, Object> wfSectDescData = new HashMap<>();
						
						try {
							JSONArray jsonArray = new JSONArray(locatorArray);
							int size = jsonArray.length();
							
							for (int i = 0; i < size; i++) {
								JSONObject jsonObject = (JSONObject) jsonArray.get(i);
								Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.ItemLocator.area_seq_num);
								Object areaDesc = jsonObject.get(WakefernApplicationConstants.ItemLocator.area_desc);
								JSONArray itemLocations = jsonObject.getJSONArray(WakefernApplicationConstants.ItemLocator.item_locations);
								
								for (int j = 0; j < itemLocations.length(); j++) {
									Object upc13 = itemLocations.getJSONObject(j).get(WakefernApplicationConstants.ItemLocator.upc_13_num);
									
									try { //if wf_area_code is found from item locator response
										String wfAreaCode = itemLocations.getJSONObject(j).getString(WakefernApplicationConstants.ItemLocator.wf_area_code);

										areaSeqNumData.put(
												Long.parseLong(upc13.toString()), 
												(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum)
										);

									} catch(Exception exx) {
										areaSeqNumData.put(Long.parseLong(upc13.toString()), areaSeqNum);
									}
									
									try {
										String wfSectDesc = itemLocations.getJSONObject(j).getString(WakefernApplicationConstants.ItemLocator.wf_sect_desc);
										wfSectDescData.put(
												Long.parseLong(upc13.toString()), 
												(wfSectDesc != null && wfSectDesc.trim().equals("") ? JSONObject.NULL : wfSectDesc)
										);
									} catch (Exception e) {
										//ignore input item doesn't have "wf_sect_desc" data from Wakefern Item Locator's API call (namely, not found)
									}
									
									
									itemLocatorData.put(
											Long.parseLong(upc13.toString()), 
											(areaDesc != null && !areaDesc.toString().equals("null")) ? areaDesc : WakefernApplicationConstants.ItemLocator.Other
									);
								}
							}
						
						} catch (Exception ex) {
							logger.error("[updateCartItems]::Exception processing item locator: " + ex.getMessage());
							throw ex;
						}

						// display some trace/debug info 
						if (logger.isTraceEnabled()) {
							logger.trace("itemLocatorData:");
							for(Object key : itemLocatorData.keySet()) {
							    Object value = itemLocatorData.get(key);
							    logger.trace("key: " + key + "; value: " + value );
							}
	
							logger.trace("areaSeqNumData:");
							for(Object key : areaSeqNumData.keySet()) {
							    Object value = areaSeqNumData.get(key);
							    logger.trace("key: " + key + "; value: " + value );
							}
	
							logger.trace("wfSectDescData:");
							for(Object key : wfSectDescData.keySet()) {
							    Object value = wfSectDescData.get(key);
							    logger.trace("key: " + key + "; value: " + value );
							}
						}
						
						for (int i = 0; i < itemsJArray.length(); i++) {
							JSONObject item = itemsJArray.getJSONObject(i);
							
							String itemId = item.get(WakefernApplicationConstants.ItemLocator.Sku).toString();

							String upc = this.updateUPC(itemId);

							Object areaSeqNum = areaSeqNumData.get(Long.parseLong(upc));
							int areaSeqInt = Integer.parseInt(areaSeqNum.toString()); 
							
							if (areaSeqInt > 0) {
								item.put(WakefernApplicationConstants.ItemLocator.Aisle, itemLocatorData.get(Long.parseLong(upc)).toString());
								item.put("AisleAreaSeqNum", areaSeqInt);
								
							} else { // area_seq_num = 0, -1, or -999 - INVALID
								item.put(WakefernApplicationConstants.ItemLocator.Aisle, WakefernApplicationConstants.ItemLocator.Other);
								item.put("AisleAreaSeqNum", Integer.MAX_VALUE - 100);	// list before shopping personal note section			
							}
							
							Object wfSectDesc =  wfSectDescData.get(Long.parseLong(upc));
							
 							if (wfSectDesc == null) {
 								item.put("AisleSectionDesc", JSONObject.NULL);
 							} else {
 								item.put("AisleSectionDesc", wfSectDescData.get(Long.parseLong(upc)));
 							}
												
							retvalJObj.append(WakefernApplicationConstants.ItemLocator.Items, item);
						}
						
						// pre-sort by AisleAreaSeqNum to ease the UI processing 
						JSONArray itemsSortArray = (JSONArray) retvalJObj.get(WakefernApplicationConstants.ItemLocator.Items);
						retvalJObj.put(WakefernApplicationConstants.ItemLocator.Items, SortItemLocators.sortItems(itemsSortArray));
						
						return retvalJObj.toString();
					
					} else {
						// Failed to get a Wakefern Authentication String.
						// So we can't get Item Location Data.
						// Just return the original response string.
						logger.error("Failed to get a Wakefern Authentication String.");
						return cartResponseData;
					}
				
				} else {
					// The Items Array is empty (no products).
					return cartResponseData;
				}
			
			} else {
				// The supplied response string does not contain any Items (products).
				// Just return the original string.
				return cartResponseData;
			}

		} catch (Exception e) {
			// Item Locator done gone and blowed up.
			// Return the original response string.
			
			// 2018-11-06 Error case: if a Sku is not found, then it blew up. Maybe because a user find a product from other store.
			//logger.error("[getItemLocations]::Exception processing item locator: ", e);
			logger.error("[updateCartItems::Exception processing item locator. The error message: " + LogUtil.getExceptionMessage(e) 
							+ ", exception location: " +  LogUtil.getRelevantStackTrace(e));
			return cartResponseData;
		}
	}
	
	/**
	 * Apparently the SKU Wakefern expects, is slightly different from the one MWG provides. The last digit is the checksum.
	 * 
	 * @param sku
	 * @return
	 */
	private String updateUPC(String sku) {
		return sku.substring(0, sku.length() - 1);
	}
	
}
