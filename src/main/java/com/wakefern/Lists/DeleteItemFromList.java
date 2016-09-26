package com.wakefern.Lists;

import com.wakefern.ShoppingLists.ShoppingListItemsGet;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.request.HTTPRequest;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
public class DeleteItemFromList extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{storeId}/users/{userId}/lists")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
                          @HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,@DefaultValue("false") @QueryParam("deleteAll") String deleteAll, String jsonBody) throws Exception, IOException {
        JSONObject requestBody = new JSONObject(jsonBody);
        String requestId = requestBody.getString("ItemKey");

        this.token = authToken;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);



        String listId = ListHelpers.getListId(listName,userId,authToken);
        ShoppingListItemsGet list = new ShoppingListItemsGet();
        String returnString =  list.getInfo(userId,storeId,listId,"9999","0",authToken);
        String itemId = ListHelpers.getItemId(returnString,requestId);

        this.path =  "https://shop.shoprite.com/api/shoppinglist/v5/chains/" + "" + "/users/"+userId+"/lists/"+listId+"/items/"+ itemId;

        return HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader());

    }

}
