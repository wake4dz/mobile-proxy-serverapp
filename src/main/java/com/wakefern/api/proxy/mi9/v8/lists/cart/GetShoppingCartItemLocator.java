package com.wakefern.api.proxy.mi9.v8.lists.cart;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorArray;

/**
 * 
 * @author Danny Zheng
 * @date   2021-09-13
 *  
 * 	https://storefrontgateway.staging.brands.wakefern.com/index.html#/Lists/get_api_lists_planning__retailerstoreid_
	Lists: Get /api/lists/planning/{retailerstoreid} - Retrieves planning for a store
	curl -X 'GET' \
  		'https://storefrontgateway.staging.brands.wakefern.com/api/lists/planning/144' \ 
 *  
 *  
 */
@Path(ApplicationConstants.Requests.Proxy)
public class GetShoppingCartItemLocator extends BaseService {
	private final static Logger logger = Logger.getLogger(GetShoppingCartItemLocator.class);

	@GET
	@Path(ApplicationConstants.Requests.Mi9V8.ShoppingCartItemLocator)
	public Response getShoppingCartItemLocator(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAccept) String accept,
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.xSiteHost) String xSiteHost,
			@PathParam("storeId") String storeId)
	{
		try {

			this.requestPath = ApplicationConstants.Requests.Mi9V8.BaseURL + "/lists/planning/" + storeId;

			Map<String, String> headerMap = new HashMap<String, String>();
			headerMap.put(ApplicationConstants.Requests.Header.contentAccept, accept);
			headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
			headerMap.put(ApplicationConstants.Requests.Header.xSiteHost, xSiteHost);
			
			// for Cloudflare security checking, not sure if needed
			headerMap.put(ApplicationConstants.Requests.Header.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
			
			String response = HTTPRequest.executeGet(this.requestPath, headerMap, VcapProcessor.getApiMediumTimeout());
			
			// updateCartItems() is to call Wakefern's ItemLocator API to get additional data and sort the items in a way
			// that suits with the UI app requirements.
			response = updateCartItems(response, storeId);
			
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_MI9V8_GET_SHOPPING_CART_ITEM_LOCATOR);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"authorization", authToken, "accept", accept, "xSiteHost", xSiteHost,
					"userAgent", ApplicationConstants.StringConstants.wakefernApplication);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}
	
	
	/**
     * Get Item Location data and add it to the Mi9 V8 Shopping Cart JSON Response Data.
     * 
     * 	 Mi9 returns a JSON response with "items" property with all items in a shopping cart.
     *   For each SKU, we remove the check digit, string them with a comma, and call Wakefern Item Locator's API with a wakefern store id
     *   Add/update these 3 properties for a new JSON object
     *   	aisle            → wf_area_desc
	 *      aisleAreaSeqNum  → area_seq_num
	 *		aisleSectionDesc → wf_sect_desc
     * 
     *      "itemLocator": {
	            "aisle": "Seafood",
	            "AisleAreaSeqNum": 10,
	            "AisleSectionDesc": "seafood section description"
			},
     *   
     * 
     */
	private String updateCartItems(String cartResponseData, String storeId) {
		try {
			
			JSONObject origRespJObj = new JSONObject(cartResponseData);
			storeId = (storeId == null) ? "" : storeId;

			if (origRespJObj.has(WakefernApplicationConstants.Mi9V8ItemLocator.Items) && storeId.length() > 0) {
				JSONArray itemsJArray = (JSONArray) origRespJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);
				
				if (itemsJArray.length() > 0) {				
					JSONObject itemsJObj  = new JSONObject();
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
					String authString = auth.getInfo(
							MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
					
					// Can't get Item Location Data w/o a valid Wakefern Auth String.
					if (!authString.isEmpty()) {
						String strItemSkuList = "";
						
						// Get the items in the array and make a comma separated string of them as well trim the first and last digit
						for (int i = 0, size = itemsJArray.length(); i < size; i++) {
							JSONObject itemJObj = (JSONObject) itemsJArray.get(i);
							
							String itemId = itemJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
							String sku = this.updateUPC(itemId);
							
							if (sku.matches("[0-9]+")) {
								strItemSkuList += sku + ",";
								itemsJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.Items, itemJObj);
							
							} else {
								itemJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle, WakefernApplicationConstants.Mi9V8ItemLocator.Other);
								
								// for a shopping cart note, not a real item
								itemJObj.put("aisleAreaSeqNum", -1);
								itemJObj.put("aisleSectionDesc", JSONObject.NULL);
								
								retvalJObj.append(WakefernApplicationConstants.Mi9V8ItemLocator.Items, itemJObj);
							}
						}

						if (itemsJObj.length() == 0) {
							// for a shopping cart has one item only which is a personal shopping note.
							// 2021-09-21 not sure if this would apply to the V8-based mobile app?
							logger.debug("itemsJObj: is empty, return to the UI now");
							return retvalJObj.toString();
						}
						
						itemsJArray = (JSONArray) itemsJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);
						strItemSkuList = strItemSkuList.substring(0, strItemSkuList.length() - 1); //remove trailing comma
						ItemLocatorArray itemLocatorArray = new ItemLocatorArray();
						
						//call Wakefern Item Locator API with
						//  1. a store id
						//  2. a list of skus with a comma as a delimiter 
						//  3. JWT public key
						String locatorArray = itemLocatorArray.getInfo(storeId, strItemSkuList, authString);
						
						Map<Long, Object> itemLocatorData = new HashMap<>();
						Map<Long, Object> areaSeqNumData = new HashMap<>();
						Map<Long, Object> wfSectDescData = new HashMap<>();
						
						try {
							JSONArray jsonArray = new JSONArray(locatorArray);
							int size = jsonArray.length();
							
							for (int i = 0; i < size; i++) {
								JSONObject jsonObject = (JSONObject) jsonArray.get(i);
								Object areaSeqNum = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_seq_num);
								Object areaDesc = jsonObject.get(WakefernApplicationConstants.Mi9V8ItemLocator.area_desc);
								JSONArray itemLocations = jsonObject.getJSONArray(WakefernApplicationConstants.Mi9V8ItemLocator.item_locations);
								
								for (int j = 0; j < itemLocations.length(); j++) {
									Object upc13 = itemLocations.getJSONObject(j).get(WakefernApplicationConstants.Mi9V8ItemLocator.upc_13_num);
									
									try { //if wf_area_code is found from item locator response
										String wfAreaCode = itemLocations.getJSONObject(j).getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_area_code);

										areaSeqNumData.put(
												Long.parseLong(upc13.toString()), 
												(wfAreaCode != null && wfAreaCode.trim().equals("0") ? "0" : areaSeqNum)
										);

									} catch(Exception exx) {
										areaSeqNumData.put(Long.parseLong(upc13.toString()), areaSeqNum);
									}
									
									try {
										String wfSectDesc = itemLocations.getJSONObject(j).getString(WakefernApplicationConstants.Mi9V8ItemLocator.wf_sect_desc);
										wfSectDescData.put(
												Long.parseLong(upc13.toString()), 
												(wfSectDesc != null && wfSectDesc.trim().equals("") ? JSONObject.NULL : wfSectDesc)
										);
									} catch (Exception e) {
										//ignore input item doesn't have "wf_sect_desc" data from Wakefern Item Locator's API call (namely, not found)
									}
									
									
									itemLocatorData.put(
											Long.parseLong(upc13.toString()), 
											(areaDesc != null && !areaDesc.toString().equals("null")) ? areaDesc : WakefernApplicationConstants.Mi9V8ItemLocator.Other
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
							String itemId = item.get(WakefernApplicationConstants.Mi9V8ItemLocator.Sku).toString();
							String upc = this.updateUPC(itemId);
							
		
							JSONObject newItemLocator = new JSONObject();
							// for aisle
							newItemLocator.put(WakefernApplicationConstants.Mi9V8ItemLocator.Aisle, itemLocatorData.get(Long.parseLong(upc)).toString());
							
							// for aisleAreaSeqNum
							Object areaSeqNum = areaSeqNumData.get(Long.parseLong(upc));
							int areaSeqInt = Integer.parseInt(areaSeqNum.toString()); 
							newItemLocator.put("aisleAreaSeqNum", areaSeqInt);
							
							// for aisleSectionDesc
							Object wfSectDesc =  wfSectDescData.get(Long.parseLong(upc));
 							if (wfSectDesc == null) {
 								newItemLocator.put("aisleSectionDesc", JSONObject.NULL);
 							} else {
 								newItemLocator.put("aisleSectionDesc", wfSectDescData.get(Long.parseLong(upc)));
 							}

 							item.put("itemLocator", newItemLocator);
											
							retvalJObj.append(WakefernApplicationConstants.ItemLocator.Items, item);
						}
										
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
