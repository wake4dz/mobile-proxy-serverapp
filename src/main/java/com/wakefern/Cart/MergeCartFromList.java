package com.wakefern.Cart;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zacpuste on 9/16/16.
 */
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class MergeCartFromList extends BaseService {
    @GET
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/merge-from/list/{listId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @PathParam("listId") String listId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        String jsonBody = ApplicationConstants.StringConstants.fakeJson;
        String path = prepareResponse(userId, storeId, listId, isMember, authToken);

        MWGHeader mwgHeader = new MWGHeader();
        mwgHeader.authenticate(this.requestToken, ApplicationConstants.jsonResponseType, ApplicationConstants.shoppingListItemPost.contentAccept);

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(path, jsonBody, mwgHeader.getMap(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String listId, String isMember, String authToken) throws Exception, IOException {
        String jsonBody = ApplicationConstants.StringConstants.fakeJson;
        String path = prepareResponse(userId, storeId, listId, isMember, authToken);

        MWGHeader mwgHeader = new MWGHeader();
        mwgHeader.authenticate(this.requestToken, ApplicationConstants.jsonResponseType, ApplicationConstants.shoppingListItemPost.contentAccept);

        return (HTTPRequest.executePostJSON(path, jsonBody, mwgHeader.getMap(), 0));
    }

    public MergeCartFromList(){
        this.requestHeader = new MWGHeader();
    }

    private String prepareResponse(String userId, String storeId, String listId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.baseURLV5 +  ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.mergeFrom
                + ApplicationConstants.StringConstants.list + ApplicationConstants.StringConstants.backSlash + listId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.baseURLV5 +  ApplicationConstants.Requests.Cart.CartUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.mergeFrom
                    + ApplicationConstants.StringConstants.list + ApplicationConstants.StringConstants.backSlash + listId
                    + ApplicationConstants.StringConstants.isMember;
        }
        return this.requestPath;
    }
}
