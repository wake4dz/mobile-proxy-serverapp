package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zacpuste on 10/3/16.
 */
@Path(ApplicationConstants.Requests.Checkout.Users)
public class ReferAFriend extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}/stores/{storeId}/refer/friend")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.refer-a-friend+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(HTTPRequest.executePut(secondMapping.getPath(), secondMapping.getGenericBody(), map));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.refer-a-friend+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executePut(secondMapping.getPath(), secondMapping.getGenericBody(), map);
    }

    public ReferAFriend(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.Users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.referFriend;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.Users
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.stores
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.referFriend
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
