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
    @Path("/{userId}/store/{storeId}/chain/{chainId}/duplicate") //todo determine path
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("chainId") String chainId, @QueryParam("q") String q,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.duplicate;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        /**
         *  Assumption query is of format ?q=id1&id2
         */
        //Split on &
        String firstId = q.substring(0, 36);
        String secondId = q.substring(38, q.length());

        //Get items from first list
        ShoppingListItemsGet shoppingListItemsGet = new ShoppingListItemsGet();
        String firstList = shoppingListItemsGet.getInfo(chainId, userId, firstId, authToken);

        ShoppingListItemsPost shoppingListItemsPost = new ShoppingListItemsPost();
        return shoppingListItemsPost.getInfo(userId, storeId, secondId, authToken, firstList);
    }
    public DuplicateList(){
        this.serviceType = new MWGHeader();
    }
}