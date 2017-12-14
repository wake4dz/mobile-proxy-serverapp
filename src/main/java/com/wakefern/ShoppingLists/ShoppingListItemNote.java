package com.wakefern.ShoppingLists;

/**
 * Created by zacpuste on 10/28/16.
 */

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
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slChains)
public class ShoppingListItemNote extends BaseService {
    @PUT
    @Produces("application/*")
    /**
     * JSON for put:
     * {"Note":1477667596890}
     */
    @Path("/{chainId}/users/{userId}/lists/{listId}/items/{itemId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("userId") String userId, @PathParam("listId") String listId,
                                    @PathParam("itemId") String itemId, @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(chainId, userId, listId, itemId, isMember, authToken);
        Map<String, String> map = new HashMap<>();
        String path = ApplicationConstants.Requests.baseURLV5 + this.requestPath;
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.list-item+json");

        try {
            return this.createValidResponse(HTTPRequest.executePut("", path, "", jsonBody, map, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String userId, String listId, String itemId, String authToken, String isMember, String jsonBody) throws Exception, IOException {
        prepareResponse(chainId, userId, listId, itemId, isMember, authToken);

        Map<String, String> map = new HashMap<>();
        String path = ApplicationConstants.Requests.baseURLV5 + this.requestPath;
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.list-item+json");

        return HTTPRequest.executePut("", path, "", jsonBody, map, 0);
    }

    public ShoppingListItemNote(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String userId, String listId, String itemId, String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.ShoppingLists.slChains
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.items
                + ApplicationConstants.StringConstants.backSlash + itemId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.ShoppingLists.slChains
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.users
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.lists
                    + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.items
                    + ApplicationConstants.StringConstants.backSlash + itemId + ApplicationConstants.StringConstants.isMember;
        }
    }
}