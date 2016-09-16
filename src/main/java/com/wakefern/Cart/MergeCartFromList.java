package com.wakefern.Cart;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/16/16.
 */
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class MergeCartFromList extends BaseService {
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/merge-from/list/{listId}")
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("listId") String listId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.mergeFrom
                + ApplicationConstants.StringConstants.list + ApplicationConstants.StringConstants.backSlash + listId;

        ServiceMappings mapping = new ServiceMappings();
        mapping.setAllHeadersPutMapping(this, jsonBody);

        return (HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), mapping.getgenericHeader()));
    }

    public MergeCartFromList(){
        this.serviceType = new MWGHeader();
    }
}
