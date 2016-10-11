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
    public Response getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
                          @HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,@DefaultValue("") @QueryParam("listId") String listId,@DefaultValue("9999") @QueryParam("take") String take,@DefaultValue("0") @QueryParam("skip") String skip, String jsonBody){

        if(listId.isEmpty()) {
            try {
				listId = ListHelpers.getListId(listName, userId, authToken, storeId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }
        ShoppingListItemsGet list = new ShoppingListItemsGet();
        if(!listName.equalsIgnoreCase(ApplicationConstants.Lists.cart)) {
            try {
				return this.createValidResponse(list.getInfo(userId, storeId, listId, take, skip, authToken));
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
				return this.createValidResponse(cartList.getInfo(userId,storeId,authToken));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return this.createErrorResponse(e);
			}
        }
    }
}
