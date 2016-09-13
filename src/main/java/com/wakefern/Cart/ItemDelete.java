package com.wakefern.Cart;

import com.ibm.json.java.JSONObject;
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
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class ItemDelete extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/item/{itemId}")
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("itemId") String itemId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                + ApplicationConstants.StringConstants.backSlash + itemId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public ItemDelete(){
        this.serviceType = new MWGHeader();
    }
}
