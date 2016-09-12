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
public class ShoppingListItemsGet extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/lists/{listId}/items")
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId ,@PathParam("listId") String listId,
                          @QueryParam("q") String q, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId + q;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public ShoppingListItemsGet(){
        this.serviceType = new MWGHeader();
    }
}

