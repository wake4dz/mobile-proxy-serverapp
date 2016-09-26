package com.wakefern.ShoppingLists;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/9/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slItemsUser)
public class ShoppingListItemsPost extends BaseService {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/list/{listId}")
    /**
     {
     "Items":[
     {
     "ItemType":"userdefined",
     "ItemKey":"userdefined~random grocery",
     }
     ]
     }
     */
    public String getInfo(@PathParam("userId") String userId,@PathParam("storeId") String storeId,  @PathParam("listId") String listId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId;

        ServiceMappings mapping = new ServiceMappings();
        mapping.setAllHeadersPutMapping(this, jsonBody);
        //
        String path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId;
        return (HTTPRequest.executePostJSON(path, mapping.getGenericBody(), mapping.getgenericHeader()));
    }

    public ShoppingListItemsPost(){
        this.serviceType = new MWGHeader();
    }
}
