package com.wakefern.ShoppingLists;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
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
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("listId") String listId,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        String path = prepareResponse(userId, storeId, listId, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setAllHeadersPutMapping(this, jsonBody);

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(path, mapping.getGenericBody(), mapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String listId, String authToken, String jsonBody) throws Exception, IOException {
        String path = prepareResponse(userId, storeId, listId, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setAllHeadersPutMapping(this, jsonBody);

        return (HTTPRequest.executePostJSON(path, mapping.getGenericBody(), mapping.getgenericHeader()));
    }

    public ShoppingListItemsPost(){
        this.serviceType = new MWGHeader();
    }

    private String prepareResponse(String userId, String storeId, String listId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId;
        String path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId;
        return path;
    }
}
