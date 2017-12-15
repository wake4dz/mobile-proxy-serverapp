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
public class ShoppingListsGet extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/users/{userId}/lists")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("userId") String userId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, userId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String userId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(chainId, userId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public ShoppingListsGet(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String userId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.ShoppingLists.slChains
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.ShoppingLists.slChains
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
