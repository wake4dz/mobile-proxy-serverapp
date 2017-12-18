package com.wakefern.Cart;

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
@Path(ApplicationConstants.Requests.Cart.CartUser)
public class Review extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/review")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        preparedResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken) throws Exception, IOException {
        preparedResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public Review(){
        this.requestHeader = new MWGHeader();
    }

    private void preparedResponse(String userId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.review;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Cart.CartUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.review
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
