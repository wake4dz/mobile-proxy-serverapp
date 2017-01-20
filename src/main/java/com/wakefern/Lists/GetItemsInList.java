package com.wakefern.Lists;

import com.wakefern.Cart.CartGet;
import com.wakefern.Lists.Models.GenericListItem;
import com.wakefern.ShoppingLists.ShoppingListItemsGet;
import com.wakefern.ShoppingLists.ShoppingListsGet;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;

import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slGenericList)
public class GetItemsInList extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/users/{userId}/lists")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
									@DefaultValue("")@QueryParam("isMember") String isMember,
                          @HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,
									@DefaultValue("") @QueryParam("listId") String listId,@DefaultValue("9999") @QueryParam("take") String take,
									@DefaultValue("0") @QueryParam("skip") String skip, String jsonBody){

        if(listId.isEmpty()) {
            try {
				listId = ListHelpers.getListId(listName, userId, isMember,authToken, storeId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }
        ShoppingListItemsGet list = new ShoppingListItemsGet();
        if(!listName.equalsIgnoreCase(ApplicationConstants.Lists.cart)) {
            try {
				return this.createValidResponse(list.getInfo(userId, storeId, listId, take, skip, "", isMember, authToken));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }else{
            CartGet cartList = new CartGet();
            try {
				return this.createValidResponse(cartList.getInfo(userId,storeId,isMember,authToken));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }
    }
    
    public String getInfo(String storeId, String userId, String isMember, String authToken, 
			String listName, String listId, String take, String skip, String jsonBody) {
    	return this.getInfoFilter(storeId, userId, isMember, authToken, listName, listId, take, skip, jsonBody,"");
    }
//
	public String getInfoFilter(String storeId, String userId, String isMember, String authToken, 
			String listName, String listId, String take, String skip, String jsonBody,String filter) {
		//System.out.println("Item List :: Store ID :: " + storeId + ":: User ID ::" + userId + ":: isMemeber ::" + isMember + ":: Auth Token ::" + authToken + ":: List Name ::" + listName  + ":: List Id ::" + listId + ":: Take ::" + take + ":: Skip ::" + skip + ":: JSON Body ::" + jsonBody + ":: Filter ::" + filter);
		if(listId.isEmpty()) {
			try {
				listId = ListHelpers.getListId(listName, userId, isMember, authToken, storeId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e).toString();
			}
		}
		ShoppingListItemsGet list = new ShoppingListItemsGet();
		if(!listName.equalsIgnoreCase(ApplicationConstants.Lists.cart)) {
			try {
				if(filter.isEmpty()){
					return list.getInfo(userId, storeId, listId, take, skip, "", isMember, authToken);
				}else{
					return list.getInfoFilter(userId, storeId, listId, take, skip, isMember, authToken, filter, "");
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e).toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e).toString();
			}
		}else{
			CartGet cartList = new CartGet();
			try {
				return cartList.getInfo(userId,storeId,isMember,authToken);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e).toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e).toString();
			}
		}
	}
}
