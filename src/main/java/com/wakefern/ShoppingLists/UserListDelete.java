package com.wakefern.ShoppingLists;

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
@Path(ApplicationConstants.Requests.ShoppingLists.slChains)
public class UserListDelete extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{chainId}/users/{userId}/lists/{listId}")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("userId") String userId, @PathParam("listId") String listId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slChains
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                + ApplicationConstants.StringConstants.backSlash + listId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public UserListDelete(){
        this.serviceType = new MWGHeader();
    }
}
