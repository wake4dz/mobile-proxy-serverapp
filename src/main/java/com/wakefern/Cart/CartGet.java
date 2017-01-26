package com.wakefern.Cart;

import java.io.IOException;
import java.util.HashMap;

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
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class CartGet extends BaseService {
	@GET
	@Produces("application/*")
	@Path("/{userId}/store/{storeId}")
	public Response getInfoResponse(@PathParam("userId") String userId,
			@PathParam("storeId") String storeId,
			@DefaultValue("") @QueryParam("isMember") String isMember,
			@HeaderParam("Authorization") String authToken,
			@DefaultValue("") @QueryParam("shortStoreId") String shortStoreId)
			throws Exception, IOException {
		prepareResponse(userId, storeId, isMember, authToken);

		ServiceMappings secondMapping = new ServiceMappings();
		secondMapping.setMapping(this);

		try {
			String cartResp = HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader(),0);
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
							searchAble.append(ApplicationConstants.AisleItemLocator.Items,item);
						} else {
							item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
							retval.append(ApplicationConstants.AisleItemLocator.Items, item);
						}
					}

					items = (JSONArray) searchAble.get(ApplicationConstants.AisleItemLocator.Items);
					responseString = responseString.substring(0, responseString.length() - 1); //remove trailing comma
					ItemLocatorArray itemLocatorArray = new ItemLocatorArray();
					String locatorArray = itemLocatorArray.getInfo(shortStoreId, responseString, authString);
					HashMap<String, Object> itemLocatorData = new HashMap<>();

					try {
						JSONArray jsonArray = new JSONArray(locatorArray);
						int size = jsonArray.length();
						for (int i = 0; i < size; i++) {
							JSONObject jsonObject = (JSONObject) jsonArray.get(i);
							Object areaDesc = jsonObject.get(ApplicationConstants.AisleItemLocator.area_desc);
							JSONArray itemLocations = jsonObject.getJSONArray(ApplicationConstants.AisleItemLocator.item_locations);
							for( int j = 0; j < itemLocations.length(); j++ ) {
								Object upc13 = itemLocations.getJSONObject(j).get(ApplicationConstants.AisleItemLocator.upc_13_num);
								itemLocatorData.put(upc13.toString(), areaDesc);
							}
						}
					} catch (Exception e){
						throw e;
					}

					for( int i = 0; i < items.length(); i++ ){
						JSONObject item = items.getJSONObject(i);
						String itemId = item.get(ApplicationConstants.AisleItemLocator.Sku).toString();
						String upc = this.updateUPC(itemId);

						while (upc.charAt(0) == '0'){
							upc = upc.substring( 1, upc.length() );
						}
						Object wfAreaDesc = itemLocatorData.get(upc);
						if( wfAreaDesc != null){
							if( wfAreaDesc.toString() != "null" ){
								item.put(ApplicationConstants.AisleItemLocator.Aisle, wfAreaDesc.toString());
							} else {
								item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
							}
						} else {
							item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
						}
						retval.append(ApplicationConstants.AisleItemLocator.Items, item);
					}
					return this.createValidResponse(retval.toString());
				}
			} else { // Return without anything
				for(Object item: items){
					JSONObject currentItem = (JSONObject) item;
					currentItem.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
					retval.append(ApplicationConstants.AisleItemLocator.Items, currentItem);
				}
				return this.createValidResponse(retval.toString());
			}
		} catch (Exception e) {
			return this.createErrorResponse(e);
		}
		System.out.print("Null return"); return null; //Code should not be reached
	}

	private String updateUPC(String sku) {    return sku.substring(1, sku.length() - 1);    }

	public String getInfo(String userId, String storeId, String isMember,
			String authToken) throws Exception, IOException {
		prepareResponse(userId, storeId, isMember, authToken);

		ServiceMappings secondMapping = new ServiceMappings();
		secondMapping.setMapping(this);

		return HTTPRequest.executeGetJSON(secondMapping.getPath(),
				secondMapping.getgenericHeader(), 0);
	}

	public void prepareResponse(String userId, String storeId, String isMember,
			String authToken) {
		this.token = authToken;
		this.path = ApplicationConstants.Requests.Cart.CartUser
				+ ApplicationConstants.StringConstants.backSlash + userId
				+ ApplicationConstants.StringConstants.store
				+ ApplicationConstants.StringConstants.backSlash + storeId;
		if (!isMember.isEmpty()) {
			this.path = ApplicationConstants.Requests.Cart.CartUser
					+ ApplicationConstants.StringConstants.backSlash + userId
					+ ApplicationConstants.StringConstants.store
					+ ApplicationConstants.StringConstants.backSlash + storeId
					+ ApplicationConstants.StringConstants.isMember;
		}
	}

	public CartGet() {
		this.serviceType = new MWGHeader();
	}
}
