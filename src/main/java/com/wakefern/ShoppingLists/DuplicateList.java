package com.wakefern.ShoppingLists;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zacpuste on 9/1/16.
 */

@Path(ApplicationConstants.Requests.ShoppingLists.slUser)
public class DuplicateList extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/duplicate") //todo determine path
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                          @QueryParam("id1") String id1, @QueryParam("id2") String id2,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.duplicate
                + ApplicationConstants.StringConstants.id1 + id1 + ApplicationConstants.StringConstants.id2 + id2;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        //Get items from first list - max and min hardcoded for take and skip respectively
        ShoppingListItemsGet shoppingListItemsGet = new ShoppingListItemsGet();
        String firstList = shoppingListItemsGet.getInfo(userId, storeId, id1, "9999", "0", authToken);

        ShoppingListItemsPost shoppingListItemsPost = new ShoppingListItemsPost();
        return shoppingListItemsPost.getInfo(userId, storeId, id2, authToken, firstList);
    }
    public DuplicateList(){
        this.serviceType = new MWGHeader();
    }
}