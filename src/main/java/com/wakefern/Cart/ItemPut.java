package com.wakefern.Cart;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class ItemPut extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/item/{itemId}")
    /**
     * JSON Format
     {
     "Quantity": 4,
     "Note": ""
     }
     * Item id (Orange juice) for testing: 
     * 24b51a30-d56a-e611-8708-d89d6763b1d9
     */
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("itemId") String itemId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, itemId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody, null);

        try {
            return this.createValidResponse(HTTPRequest.executePut(secondMapping.getPath(), secondMapping.getGenericBody(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String itemId, String isMember, String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, itemId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody, null);

        return HTTPRequest.executePut(secondMapping.getPath(), secondMapping.getGenericBody(), secondMapping.getgenericHeader());
    }

    public ItemPut(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String itemId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                + ApplicationConstants.StringConstants.backSlash + itemId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                    + ApplicationConstants.StringConstants.backSlash + itemId;
        }
    }
}