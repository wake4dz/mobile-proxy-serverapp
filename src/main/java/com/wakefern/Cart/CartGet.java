package com.wakefern.Cart;

import com.wakefern.Wakefern.ItemLocatorArray;
import com.wakefern.Wakefern.WakefernAuth;
import com.wakefern.global.ApplicationConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

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
            @HeaderParam("Authorization") String authToken,
            @DefaultValue("") @QueryParam("shortStoreId") String shortStoreId) throws Exception, IOException {
        prepareResponse(userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try{
            //return this.createValidResponse(HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader()));
        	String cartResp = HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
        	if(shortStoreId.isEmpty()){
                return this.createValidResponse(cartResp);
        	}
        	System.out.println("Cart Resp :: " + cartResp);
        	JSONObject cart = new JSONObject(cartResp);
        	JSONArray items = (JSONArray)cart.get("Items");
        	if(!items.isNull(0)){
        		WakefernAuth auth = new WakefernAuth();
        		String authString = auth.getInfo("eyJleHAiOjE0NzYxMDQyMTM1NDYsInN1YiI6InNmamMxcGFzc3dkIiwiaXNzIjoiaHR0cDovL3dha2VmZXJuLmNvbSJ9");
        		System.out.println("Auth String :: " + authString);
        		if(!authString.isEmpty()){
        			//return without AISLE Data
        			String responseString = "";
    	        	System.out.println("Creating response string");
        			for (int i = 0, size = items.length(); i < size; i++){
        		    	//Get the items in the array and make a comma separated string of them as well trim the first and last digit 
        		    	JSONObject item = (JSONObject) items.get(i);
                    	
                    	String itemId = item.getString("Sku");
                    	String sku = this.updateUPC(itemId);
                    	responseString += sku + ",";
        		    }
        	    	responseString = responseString.substring(0,responseString.length()-1);
    	        	System.out.println("Response string :: " + responseString);
        			ItemLocatorArray itemLocate = new ItemLocatorArray();
        			String itemLocations = itemLocate.getInfo(shortStoreId, responseString, authString);
    	        	System.out.println("Item Locations :: " + itemLocations);
        			try{
        				JSONArray array = new JSONArray(itemLocations);
            		    for (int i = 0, size = array.length(); i < size; i++){
            				JSONObject locationItems = (JSONObject)array.get(i);
            				JSONArray locations = locationItems.getJSONArray("item_locations");
            				
            				//Iterate through these too - inner excluded UPCs 
            				for(int z = 0, sizez = locations.length(); z<sizez;z++){
            				
            					JSONObject aItem = (JSONObject) locations.get(z);
                		    	for (int j = 0, sizej = items.length(); j < sizej; j++){
                    		    	//Get the items in the array and make a comma separated string of them as well trim the first and last digit 
                    		    	JSONObject item = (JSONObject) items.get(j);
                    		    	Long itemNum = aItem.getLong("upc_13_num");
                    		    	String itemString = itemNum.toString();
                    		    	System.out.println("String itemNum ::" + itemString + " :: item sku ::" + item.get("Sku"));
                                	if(item.getString("Sku").contains(itemString)){
                                		if(aItem.has("area_desc")){
                                			item.put("Aisle", aItem.get("area_desc"));
                                    		break;
                                		}else{
                                    		item.put("Aisle", "Other");
                                    		break;
                                    	}
                                	}else if(j+1 == sizej){
                                		item.put("Aisle", "Other");
                                	}
                    		    }
            		    	
            				}
            		    	
            		    	
            		    }
            		    System.out.println("Made it to end");
                        return this.createValidResponse(cart.toString());
        			}catch(Exception e){
        				//Error casting 
        	        	System.out.println("Cast Error :: " + e.getMessage());
                        return this.createValidResponse(cartResp);
        			}
        		}
	        	System.out.println("Bad auth string :: ");
                return this.createValidResponse(cart.toString());	
        	}else{
        		//Return without anything 
                return this.createValidResponse(cartResp);
        	}
        	
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
    
    private String updateUPC(String sku){
    	sku = sku.substring(1);
    	sku = sku.substring(0,sku.length()-1);
    	return sku;
    }
    
    public String getInfo(String userId, String storeId, String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public void prepareResponse(String userId, String storeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
    }

    public CartGet(){
        this.serviceType = new MWGHeader();
    }
}
