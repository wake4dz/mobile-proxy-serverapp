package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/3/16.
 */
@Path(ApplicationConstants.Requests.Checkout.Users)
public class ReferAFriend extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}/stores/{storeId}/refer/friend")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());
    }

    public ReferAFriend(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.referFriend;
    }
}
