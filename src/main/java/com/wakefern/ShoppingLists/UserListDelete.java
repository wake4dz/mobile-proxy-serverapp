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
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slChains)
public class UserListDelete extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{chainId}/users/{userId}/lists/{listId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("userId") String userId, @PathParam("listId") String listId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, userId, listId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
            return this.createValidDelete();
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String userId, String listId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(chainId, userId, listId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public UserListDelete(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String userId, String listId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.ShoppingLists.slChains
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                + ApplicationConstants.StringConstants.backSlash + listId;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.ShoppingLists.slChains
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                    + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.isMember;
        }
    }
}
