package com.wakefern.Lists;

import com.wakefern.Cart.CartGet;
import com.wakefern.Cart.ItemDelete;
import com.wakefern.Cart.ItemPut;
import com.wakefern.ShoppingLists.ShoppingListItemsGet;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slGenericList)
public class DeleteItemFromList extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{storeId}/users/{userId}/lists")
    public Response getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId, @DefaultValue("") @QueryParam("listId") String listId,
							@DefaultValue("")@QueryParam("isMember") String isMember,
                          	@HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,@DefaultValue("false") @QueryParam("deleteAll") String deleteAll,@DefaultValue("") @QueryParam("update") String update, String jsonBody){
        JSONObject requestBody = new JSONObject(jsonBody);
        String requestId = requestBody.getString("ItemKey");
        String itemId = null;
        try{
        	itemId = requestBody.getString("ItemId");
        }catch(Exception e){
        	itemId = null;
        }
        this.token = authToken;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);


        if(listId.isEmpty()) {
            try {
				listId = ListHelpers.getListId(listName, userId, isMember, authToken, storeId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }
        if(!listName.equalsIgnoreCase(ApplicationConstants.Lists.cart)) {
        	if(itemId.isEmpty()){
        		//If the item ID is passed in the request body 
                ShoppingListItemsGet list = new ShoppingListItemsGet();
                String returnString = null;
    			try {
    				returnString = list.getInfo(userId, storeId, listId, "9999", "0", "", isMember, authToken);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				return this.createErrorResponse(e);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				return this.createErrorResponse(e);
    			}
    			itemId = ListHelpers.getItemId(returnString, requestId);
        	}
            this.path = "https://shop.shoprite.com/api/shoppinglist/v5/chains/" + "FBFB139" + "/users/" + userId + "/lists/" + listId + "/items/" + itemId;
            //System.out.println("DELETE + Update ITEM :: " + this.path);
            if(update.isEmpty()){
				try {
					HTTPRequest.executeDelete(this.path, secondMapping.getgenericHeader(), 0);
					return this.createValidDelete();
				} catch (Exception e){
					return this.createErrorResponse(e);
				}
            }else{
            	//Update the item with a PUT
				try {
					HTTPRequest.executePut("", this.path, "", jsonBody, secondMapping.getgenericHeader(), 0);
					return this.createDefaultResponse();
				} catch (Exception e){
					return this.createErrorResponse(e);
				}
            }
        }else{
        	if(itemId.isEmpty()){
                CartGet cartList = new CartGet();
                String returnString = null;
    			try {
    				returnString = cartList.getInfo(userId,storeId,isMember,authToken);
    			} catch (IOException e) {
    				// TODO Auto-generated catch block
    				return this.createErrorResponse(e);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				return this.createErrorResponse(e);
    			}
                itemId = ListHelpers.getItemId(returnString, requestId);
        	}
            try {
            	if(update.isEmpty()){
            		ItemDelete deleteItem = new ItemDelete();
            		deleteItem.getInfo(userId,storeId,itemId,isMember,authToken);
	            	return this.createValidDelete();
            	}else{
            		//Update item in cart 
            		ItemPut itemPut = new ItemPut();
            		itemPut.getInfo(userId, storeId, itemId, authToken, isMember, jsonBody);
            		return this.createDefaultResponse();
            	}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }


    }

    public DeleteItemFromList() { this.serviceType = new MWGHeader(); }
}
