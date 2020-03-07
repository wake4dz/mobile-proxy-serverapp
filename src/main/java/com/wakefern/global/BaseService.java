package com.wakefern.global;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.errorHandling.ExceptionHandler;
import com.wakefern.items.location.SortItemLocators;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.request.models.Header;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorArray;

public class BaseService {
    protected HashMap<String, String> requestParams = null;
    protected HashMap<String, String> queryParams   = null;
    
    protected Header requestHeader = null;
    protected String requestPath   = null;
    protected String requestToken  = null;
    private int timeout = 30000; //default
    
    protected static enum ReqType { GET, POST, PUT, DELETE };
    
    private final static Logger logger = Logger.getLogger(BaseService.class);
    
    protected String mwgRequest(ReqType reqType, String reqData, String endpointName, int timeout) throws IOException, Exception{
    	this.timeout = timeout;
    	return mwgRequest(reqType, reqData, endpointName);
    }
    /**
     * Trigger a request to the MyWebGrocer REST API.
     * 
     * @param reqType
     * @param reqData
     * @param endpointName - The name of the endpoint making the request. For logging purposes.
     * @return
     * @throws Exception
     * @throws IOException
     */
    protected String mwgRequest(ReqType reqType, String reqData, String endpointName) throws Exception, IOException {
    		String reqTypeStr;
    		String reqBody;
    		String response;
    		
    		boolean isValidType = true;
    		
    		switch (reqType) {
    			case GET    : reqTypeStr = "GET";    break;
    			case POST   : reqTypeStr = "POST";   break;
    			case PUT    : reqTypeStr = "PUT";    break;
    			case DELETE : reqTypeStr = "DELETE"; break;
    			default     : 
    				reqTypeStr  = ""; 
    				isValidType = false;
    		}
    		
    		if ((requestPath == null) || (requestHeader == null)) {
    			throw new Exception("Unable to execute " + reqTypeStr + " request.  Missing required data.");
    			
    		} else {
    			if (!isValidType) {
    				throw new Exception("Unable to execute request. Invalid request type.");
    				
    			} else {
    				ServiceMappings sm = getServiceMapping(reqType, reqData);
    				
    				String reqURL = sm.getPath();
        			Map<String, String> reqHead = sm.getgenericHeader();
        			
            		switch (reqType) {
	        			case GET : 
	        				response = HTTPRequest.executeGet(reqURL, reqHead, this.timeout);
	        				break;
	        			case POST :
	        				reqBody  = sm.getGenericBody();
	        				response = HTTPRequest.executePost(reqURL, reqBody, reqHead, this.timeout);
	        				break;
	        			case PUT :
	        				reqBody  = sm.getGenericBody();
	        				response = HTTPRequest.executePut(reqURL, reqBody, reqHead, this.timeout);
	        				break;
	        			case DELETE :
	        				response = HTTPRequest.executeDelete(reqURL, reqHead, this.timeout);
	        				break;
        				default :
        					response = "{}"; // This should never actually happen.  BUT just in case...
            		}
            		
            		logger.debug("[BaseService::mwgRequest]::" + this.requestPath + "--" + reqType);
            		
        			//Note: Since only about 75% of APIs has the userId query parameter, this log message may not print
        			//      even if isUserTrackOn=on. Just be aware of this fact.
        			//      This block of code is more useful in the future when every API call has the userId parameter.
            		if(LogUtil.isUserTrackOn) {
            			if ((queryParams != null) && queryParams.get(MWGApplicationConstants.Requests.Params.Query.userID) != null) {
		    				if (LogUtil.trackedUserIdsMap.containsKey(queryParams.get(MWGApplicationConstants.Requests.Params.Query.userID))) {
								logger.info("Tracking data for " + queryParams.get(MWGApplicationConstants.Requests.Params.Query.userID) + ": " 
										+ "[BaseService::mwgRequest]::" + this.requestPath + "--" + reqType);
		    				}
            			}
            		}
            		
            		return response;
    			}
    		}
    }
        
    /**
     * Create a standardized Error Response (HTTP 5xx / 4xx) to pass back to the UI.
     * 
     * @param e
     * @return
     */
    protected Response createErrorResponse(Exception e) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        
        try {
        		String jsonErrStart = "{\"ErrorMessage\":\"";
        		String jsonErrEnd   = "\"}";
        		
            String[] array = e.getMessage().split(",");
            String buildError;
            
            if (e.getMessage().contains("400")) {
                return Response.status(400).entity(exceptionHandler.exceptionMessageJson(e)).build();
            }
            
            if (Integer.parseInt(array[0]) == 401 || Integer.parseInt(array[0]) == 403) {
                buildError = jsonErrStart + ApplicationConstants.Requests.forbiddenError + jsonErrEnd;
            
            } else {	
            		StringBuilder sb = new StringBuilder();
            		
            		// We have to allow for the possibility that there's more than one "," in the exception's error message.
            		// For example, the error message may actually be a JSON string.
            		for (int i = 1; i < array.length; i++) {
            			sb.append(array[i] + ",");
            		}
            		
            		// Strip off the trailing comma & convert to a String
            		sb.deleteCharAt(sb.length() - 1);
            		String respBody = sb.toString();
            		
            		// Test to see if the response is already a valid JSON string.
            		// If so, just assign it to the 'buildError' var.
            		// If it's not, assemble the 'buildError' var as a JSON string.
            		try {
            			new JSONObject(respBody);
            			buildError = respBody;
        			
            		} catch (Exception ex) {
            			try {
            				new JSONArray(respBody);
            				buildError = respBody;
            			
            			} catch (Exception exx) {
            				logger.error("MWG returned an unexpected, non-JSON compliant error: " + respBody);

            				// The error is in an unexpected format.
            				// Respond with a default text message.
            				buildError = jsonErrStart + "MWG returned an unexpected, non-JSON compliant error." + jsonErrEnd;
            			}
        			}
            }

            return Response.status(Integer.parseInt(array[0])).entity(buildError).build();
        
        } catch (Exception stringError) {
            return Response.status(500).entity(exceptionHandler.exceptionMessageJson(e)).build();
        }
    }
    
    /**
     * Create a standardized Error Response (HTTP 5xx / 4xx) to pass back to the UI.
     * 
     * @param e
     * @return
     */
    protected Response createErrorResponse(String errorData, Exception e) {
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        
        try {
        	String jsonErrStart = "{\"ErrorMessage\":\"";
        	String jsonErrEnd   = "\"}";
        		
            String[] array = e.getMessage().split(",");
            String buildError;
            
            if (e.getMessage().contains("400")) {
                return Response.status(400).entity(exceptionHandler.exceptionMessageJson(e)).build();
            }
            
            if (Integer.parseInt(array[0]) == 401 || Integer.parseInt(array[0]) == 403) {
                buildError = jsonErrStart + ApplicationConstants.Requests.forbiddenError + jsonErrEnd;
            
            } else {	
            		StringBuilder sb = new StringBuilder();
            		
            		// We have to allow for the possibility that there's more than one "," in the exception's error message.
            		// For example, the error message may actually be a JSON string.
            		for (int i = 1; i < array.length; i++) {
            			sb.append(array[i] + ",");
            		}
            		
            		// Strip off the trailing comma & convert to a String
            		sb.deleteCharAt(sb.length() - 1);
            		String respBody = sb.toString();
            		
            		// Test to see if the response is already a valid JSON string.
            		// If so, just assign it to the 'buildError' var.
            		// If it's not, assemble the 'buildError' var as a JSON string.
            		try {
            			new JSONObject(respBody);
            			buildError = respBody;
        			
            		} catch (Exception ex) {
            			try {
            				new JSONArray(respBody);
            				buildError = respBody;
            			
            			} catch (Exception exx) {
                    		logger.error("MWG returned an unexpected, non-JSON compliant error: " + errorData + " - " + respBody);
            				
            				// The error is in an unexpected format.
            				// Respond with a default text message.
            				buildError = jsonErrStart + "MWG returned an unexpected, non-JSON compliant error." + jsonErrEnd;
            			}
        			}
            }

            return Response.status(Integer.parseInt(array[0])).entity(buildError).build();
        
        } catch (Exception stringError) {
            return Response.status(500).entity(exceptionHandler.exceptionMessageJson(e)).build();
        }
    }
    
    /**
     * Create Valid (HTTP 200) Response.
     * 
     * @param jsonResponse
     * @return
     */
    protected Response createValidResponse(String jsonResponse) {
    		return Response.status(200).entity(jsonResponse).build();
    }
        
    /**
     * Create a generic Response Object, using whatever status is supplied.
     * 
     * @param status
     * @return
     */
    protected Response createResponse(int status) {
        return Response.status(status).build();
    }
    
    /**
     * Get Item Location data and add it to the JSON Response Data.
     * 
     * @param respData
     * @return
     */
	protected String getItemLocations(String origRespStr, String storeID) {
		try {
			JSONObject origRespJObj = new JSONObject(origRespStr);
			storeID = (storeID == null) ? "" : storeID;

			if (origRespJObj.has(WakefernApplicationConstants.ItemLocator.Items) && storeID.length() > 0) {
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
							MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
					
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
						String locatorArray = itemLocatorArray.getInfo(storeID, strItemSkuList, authString);
						
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
							logger.error("[getItemLocations]::Exception processing item locator: " + ex.getMessage());
							throw ex;
						}

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
						return origRespStr;
					}
				
				} else {
					// The Items Array is empty (no products).
					return origRespStr;
				}
			
			} else {
				// The supplied response string does not contain any Items (products).
				// Just return the original string.
				return origRespStr;
			}

		} catch (Exception e) {
			// Item Locator done gone and blowed up.
			// Return the original response string.
			
			// 2018-11-06 Error case: if a Sku is not found, then it blew up. Maybe because a user find a product from other store.
			//logger.error("[getItemLocations]::Exception processing item locator: ", e);
			logger.error("[getItemLocations]::Exception processing item locator. The error message: " + LogUtil.getExceptionMessage(e) 
							+ ", exception location: " +  LogUtil.getRelevantStackTrace(e));
			return origRespStr;
		}
	}
	
	/**
	 * Apparently the SKU Wakefern expects, is slightly different from the one MWG provides?
	 * 
	 * @param sku
	 * @return
	 */
	private String updateUPC(String sku) {
		return sku.substring(0, sku.length() - 1);
	}				

    /**
     * Returns the ServiceMappings Object required to construct a request.
     * 
     * @param data
     * @return
     */
    private ServiceMappings getServiceMapping(ReqType reqType, String data) {
    		ServiceMappings sm = new ServiceMappings();
    		
    		data = (data == null) || (data.length() == 0) ? "{}" : data;
    		
    		switch (reqType) {
    			case DELETE:
    			case GET:
    				sm.setGetMapping(this);
    				break;
    				
    			case POST:
    			case PUT:
    				sm.setPutMapping(this, data);
    				break;
    		}
    		    		
    		return sm;
    }
}
