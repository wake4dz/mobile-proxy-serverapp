package com.wakefern.Cart;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.wakefern.Wakefern.ItemLocatorArray;
import org.json.*;

import com.wakefern.Wakefern.WakefernAuth;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class CartGet extends BaseService {
	private final static Logger logger = Logger.getLogger("CartGet");

	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path("/{userId}/store/{storeId}")
	public Response getInfoResponse(@PathParam("userId") String userId,
			@PathParam("storeId") String storeId,
			@DefaultValue("") @QueryParam("isMember") String isMember,
			@HeaderParam("Authorization") String authToken,
			@DefaultValue("") @QueryParam("shortStoreId") String shortStoreId)
			throws Exception, IOException {
//		logger.log(Level.INFO, "[getInfoResponse]::path: {0}/store/{1}", new Object[]{userId, storeId});
		
		prepareResponse(userId, storeId, isMember, authToken);

		ServiceMappings secondMapping = new ServiceMappings();
		secondMapping.setGetMapping(this);

		try {
			String cartResp = HTTPRequest.executeGet( secondMapping.getPath(), secondMapping.getgenericHeader(),0);
			try {
				if (shortStoreId.isEmpty()) {
					return this.createValidResponse(cartResp);
				}
				JSONObject cart = new JSONObject(cartResp);
				JSONArray items = (JSONArray) cart.get(ApplicationConstants.AisleItemLocator.Items);
				JSONObject searchAble = new JSONObject();
				JSONObject retval = new JSONObject();

				// Set up retval with all non-items data
				for (Object key : cart.keySet()) {
					String keyStr = (String) key;
					if (!keyStr.equals(ApplicationConstants.AisleItemLocator.Items)) {
						Object keyvalue = cart.get(keyStr);
						retval.put(keyStr, keyvalue);
					}
				}

				if (!items.isNull(0)) {
					WakefernAuth auth = new WakefernAuth();
					String authString = auth.getInfo(ApplicationConstants.AisleItemLocator.WakefernAuth);
					if (!authString.isEmpty()) {
						// return without AISLE Data
						String responseString = "";
						for (int i = 0, size = items.length(); i < size; i++) {
							// Get the items in the array and make a comma separated string of them as well trim the first and last digit
							JSONObject item = (JSONObject) items.get(i);
							String itemId = item.get(ApplicationConstants.AisleItemLocator.Sku).toString();
							String sku = this.updateUPC(itemId);
							if (sku.matches("[0-9]+")) {
								responseString += sku + ",";
								searchAble.append(ApplicationConstants.AisleItemLocator.Items, item);
							} else {
								item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
								retval.append(ApplicationConstants.AisleItemLocator.Items, item);
							}
						}

						items = (JSONArray) searchAble.get(ApplicationConstants.AisleItemLocator.Items);
						responseString = responseString.substring(0, responseString.length() - 1); //remove trailing comma
						ItemLocatorArray itemLocatorArray = new ItemLocatorArray();
						String locatorArray = itemLocatorArray.getInfo(shortStoreId, responseString, authString);
						HashMap<Long, Object> itemLocatorData = new HashMap<>();
						HashMap<Long, Object> areaSeqNumData = new HashMap<>();

						try {
							JSONArray jsonArray = new JSONArray(locatorArray);
							int size = jsonArray.length();
							for (int i = 0; i < size; i++) {
								JSONObject jsonObject = (JSONObject) jsonArray.get(i);
								Object areaSeqNum = jsonObject.get(ApplicationConstants.AisleItemLocator.area_seq_num);
								Object areaDesc = jsonObject.get(ApplicationConstants.AisleItemLocator.area_desc);
								JSONArray itemLocations = jsonObject.getJSONArray(ApplicationConstants.AisleItemLocator.item_locations);
								
								for (int j = 0; j < itemLocations.length(); j++) {
									Object upc13 = itemLocations.getJSONObject(j).get(ApplicationConstants.AisleItemLocator.upc_13_num);
									try{ //if wf_area_code is found from item locator response
										Object wfAreaCode = itemLocations.getJSONObject(j).get(ApplicationConstants.AisleItemLocator.wf_area_code);
										areaSeqNumData.put(Long.parseLong(upc13.toString()), 
												(wfAreaCode != null && wfAreaCode.toString().trim().equals("0") ? "0" : areaSeqNum));
									} catch(Exception e){
										areaSeqNumData.put(Long.parseLong(upc13.toString()), areaSeqNum);
									}
									itemLocatorData.put(Long.parseLong(upc13.toString()), (areaDesc != null && !areaDesc.toString().equals("null"))
											? areaDesc : ApplicationConstants.AisleItemLocator.Other);
								}
							}
						} catch (Exception e) {
							logger.log(Level.WARNING, "[getInfoResponse]::Exception processing item locator: ", e);
//							throw e;
						}

						for (int i = 0; i < items.length(); i++) {
							JSONObject item = items.getJSONObject(i);
							String itemId = item.get(ApplicationConstants.AisleItemLocator.Sku).toString();
							String upc = this.updateUPC(itemId);

							Object areaSeqNum = areaSeqNumData.get(Long.parseLong(upc));
							int areaSeqInt = Integer.parseInt(areaSeqNum.toString()); 
							if(areaSeqInt > 0){
								item.put(ApplicationConstants.AisleItemLocator.Aisle, itemLocatorData.get(Long.parseLong(upc)).toString());
							} else { // area_seq_num = 0, -1, or -999 - INVALID
								item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
							}
							
							retval.append(ApplicationConstants.AisleItemLocator.Items, item);
						}
						
						return this.createValidResponse(retval.toString());
					}
				} else { // Return without anything
					logger.log(Level.INFO, "[getInfoResponse]::Empty cart");

					for (Object item : items) {
						JSONObject currentItem = (JSONObject) item;
						currentItem.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
						retval.append(ApplicationConstants.AisleItemLocator.Items, currentItem);
					}
					return this.createValidResponse(retval.toString());
				}
			}catch (Exception e){
				logger.log(Level.WARNING, "[getInfoResponse]::Exception processing cart item locator: ", e);
				return this.createValidResponse(cartResp);
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "[getInfoResponse]::Exception getting cart from MWG: ", e);
			return this.createErrorResponse(e);
		}
		logger.log(Level.INFO, "[getInfoResponse]::Null return"); 
		return null; //Code should not be reached
	}

	private String updateUPC(String sku) {    return sku.substring(0, sku.length() - 1);    }

	public String getInfo(String userId, String storeId, String isMember,
			String authToken) throws Exception, IOException {

		prepareResponse(userId, storeId, isMember, authToken);

		ServiceMappings secondMapping = new ServiceMappings();
		secondMapping.setGetMapping(this);
		
		String getJsonResp = HTTPRequest.executeGet(secondMapping.getPath(),
				secondMapping.getgenericHeader(), 0);
		return getJsonResp;
	}

	public void prepareResponse(String userId, String storeId, String isMember,
			String authToken) {
		this.requestToken = authToken;
		this.requestPath = ApplicationConstants.Requests.Cart.CartUser
				+ ApplicationConstants.StringConstants.backSlash + userId
				+ ApplicationConstants.StringConstants.store
				+ ApplicationConstants.StringConstants.backSlash + storeId;
		if (!isMember.isEmpty()) {
			this.requestPath = ApplicationConstants.Requests.Cart.CartUser
					+ ApplicationConstants.StringConstants.backSlash + userId
					+ ApplicationConstants.StringConstants.store
					+ ApplicationConstants.StringConstants.backSlash + storeId
					+ ApplicationConstants.StringConstants.isMember;
		}
		logger.log(Level.INFO, "[prepareResponse]::path: "+this.requestPath);
	}

	public CartGet() {
		this.requestHeader = new MWGHeader();
	}
}
