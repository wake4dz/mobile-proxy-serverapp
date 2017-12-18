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
@Path(ApplicationConstants.Requests.ShoppingLists.slUser)
public class ShoppingListsPost extends BaseService {

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}")
    /**
     * JSON Format:
     * {"Name": "List Name"}
     */
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        String path = prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody, null, null);

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(path, mapping.getGenericBody(), mapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken, String jsonBody) throws Exception, IOException {
        String path = prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody, null, null);

        return (HTTPRequest.executePostJSON(path, mapping.getGenericBody(), mapping.getgenericHeader(), 0));
    }

    public ShoppingListsPost(){
        this.requestHeader = new MWGHeader();
    }

    private String prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
        String path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
        if(!isMember.isEmpty()){
            path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.isMember;
        }
        return path;
    }
}

