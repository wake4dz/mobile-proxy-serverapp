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
public class UserListPut extends BaseService {
    @PUT
    @Produces("application/*")
    /**
     * JSON for put:
     * {"Name": "TestList"}
     */
    @Path("/{chainId}/users/{userId}/lists/{listId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("userId") String userId, @PathParam("listId") String listId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(chainId, userId, listId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody, null);

        try {
            return this.createValidResponse(HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String userId, String listId, String authToken, String isMember, String jsonBody) throws Exception, IOException {
        prepareResponse(chainId, userId, listId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody, null);

        return HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader(), 0);
    }

    public UserListPut(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String userId, String listId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.ShoppingLists.slChains
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                + ApplicationConstants.StringConstants.backSlash + listId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.ShoppingLists.slChains
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                    + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.isMember;
        }
    }
}