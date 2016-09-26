package com.wakefern.Lists;

import com.wakefern.Lists.Models.GenericListItem;
import com.wakefern.ShoppingLists.ShoppingListItemsGet;
import com.wakefern.ShoppingLists.ShoppingListsGet;
import com.wakefern.global.ApplicationConstants;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slGenericList)
public class GetItemsInList {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/users/{userId}/lists")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
                          @HeaderParam("Authorization") String authToken, @DefaultValue("") @QueryParam("listName") String listName,@DefaultValue("9999") @QueryParam("take") String take,@DefaultValue("0") @QueryParam("skip") String skip, String jsonBody) throws Exception, IOException {

        String listId = ListHelpers.getListId(listName,userId,authToken);
        ShoppingListItemsGet list = new ShoppingListItemsGet();
        return list.getInfo(userId,storeId,listId,take,skip,authToken);

    }
}