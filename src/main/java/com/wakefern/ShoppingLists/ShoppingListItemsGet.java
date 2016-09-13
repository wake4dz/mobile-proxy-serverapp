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
    @Path("/{userId}/store/{storeId}/list/{listId}")
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId ,@PathParam("listId") String listId,
                          @QueryParam("take") String take, @QueryParam("skip") String skip, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.take
                + take + ApplicationConstants.StringConstants.skip + skip;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public ShoppingListItemsGet(){
        this.serviceType = new MWGHeader();
    }
}

