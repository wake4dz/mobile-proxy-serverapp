package com.wakefern.Cart;

/**
 * Created by zacpuste on 10/17/16.
 */

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

@Path(ApplicationConstants.Requests.Cart.CartUser)
public class ItemAdd extends BaseService {
    @POST
    @Path("/{userId}/store/{storeId}/item")
    /**

     {
     "ItemType":"product",
     "Quantity":1,
     "ItemKey":"product~165175~0.25 lb"
     }

     */
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);
        String path = ApplicationConstants.Requests.baseURLV5  + this.requestPath;

        MWGHeader mwgHeader = new MWGHeader();
        mwgHeader.authenticate(this.token, "application/vnd.mywebgrocer.cart-item+json", "application/vnd.mywebgrocer.grocery-item+json");

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(path, jsonBody, mwgHeader.getMap(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setAllHeadersPutMapping(this, jsonBody);

        return (HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), mapping.getgenericHeader(), 0));
    }

    public ItemAdd(){
        this.requestHeader = new MWGHeader();
    }

    public void prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.item
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
