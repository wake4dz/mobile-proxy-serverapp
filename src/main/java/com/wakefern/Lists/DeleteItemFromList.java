package com.wakefern.Lists;

import com.wakefern.ShoppingLists.ShoppingListItemsGet;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
public class DeleteItemFromList {
    @DELETE
    @Produces("application/*")
    @Path("/{storeId}/users/{userId}/lists")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
                          @HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,@DefaultValue("false") @QueryParam("deleteAll") String deleteAll, String jsonBody) throws Exception, IOException {
        JSONObject requestBody = new JSONObject(jsonBody);
        String requestId = requestBody.getString("ItemKey");

        String listId = ListHelpers.getListId(listName,userId,authToken);
        ShoppingListItemsGet list = new ShoppingListItemsGet();
        String returnString =  list.getInfo(userId,storeId,listId,"9999","0",authToken);
        String itemId = ListHelpers.getItemId(returnString,requestId);
        return itemId;
    }

}
