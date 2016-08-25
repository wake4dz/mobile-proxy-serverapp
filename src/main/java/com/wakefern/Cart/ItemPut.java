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
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("itemId") String itemId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                + ApplicationConstants.StringConstants.backSlash + itemId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        return HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());

    }
    public ItemPut(){
        this.serviceType = new MWGHeader();
    }
}