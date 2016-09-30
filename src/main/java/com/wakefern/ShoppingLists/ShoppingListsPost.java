package com.wakefern.ShoppingLists;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slUser)
public class ShoppingListsPost extends BaseService {

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}")
    /**
     * JSON Format:
     * {"Name": "List Name"}
     */
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody);

        String path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;


        return (HTTPRequest.executePostJSON(path, mapping.getGenericBody(), mapping.getgenericHeader()));
    }

    public ShoppingListsPost(){
        this.serviceType = new MWGHeader();
    }

}

