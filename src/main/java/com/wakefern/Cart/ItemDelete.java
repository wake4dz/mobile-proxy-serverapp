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
public class ItemDelete extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/item/{itemId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("itemId") String itemId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, itemId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try{
            HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
            return this.createValidResponse("{}");
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String itemId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, itemId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public ItemDelete(){
        this.requestHeader = new MWGHeader();
    }

    public void prepareResponse(String userId, String storeId, String itemId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                + ApplicationConstants.StringConstants.backSlash + itemId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                    + ApplicationConstants.StringConstants.backSlash + itemId + ApplicationConstants.StringConstants.isMember;
        }
    }
}
