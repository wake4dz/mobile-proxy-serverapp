package com.wakefern.Lists;

import com.wakefern.Cart.CartGet;
import com.wakefern.Cart.ItemDelete;
import com.wakefern.ShoppingLists.ShoppingListItemsGet;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slGenericList)
public class DeleteItemFromList extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{storeId}/users/{userId}/lists")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId, @DefaultValue("") @QueryParam("listId") String listId,
                          @HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,@DefaultValue("false") @QueryParam("deleteAll") String deleteAll, String jsonBody) throws Exception, IOException {
        JSONObject requestBody = new JSONObject(jsonBody);
        String requestId = requestBody.getString("ItemKey");

        this.token = authToken;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);


        if(listId.isEmpty()) {
            listId = ListHelpers.getListId(listName, userId, authToken);
        }
        if(!listName.equalsIgnoreCase(ApplicationConstants.Lists.cart)) {
            ShoppingListItemsGet list = new ShoppingListItemsGet();
            String returnString = list.getInfo(userId, storeId, listId, "9999", "0", authToken);
            String itemId = ListHelpers.getItemId(returnString, requestId);
            this.path = "https://shop.shoprite.com/api/shoppinglist/v5/chains/" + "FBFBF139" + "/users/" + userId + "/lists/" + listId + "/items/" + itemId;
            return HTTPRequest.executeDelete(this.path, secondMapping.getgenericHeader());
        }else{
            CartGet cartList = new CartGet();
            String returnString = cartList.getInfo(userId,storeId,authToken);
            String itemId = ListHelpers.getItemId(returnString, requestId);
            ItemDelete deleteItem = new ItemDelete();
            deleteItem.getInfo(userId,storeId,itemId,authToken);
        }
    }

    public DeleteItemFromList() { this.serviceType = new MWGHeader(); }
}
