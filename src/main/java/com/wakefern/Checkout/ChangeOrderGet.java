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
 * Created by zacpuste on 9/1/16.
 */
@Path(ApplicationConstants.Requests.Checkout.Order)
public class ChangeOrderGet extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{orderId}/user/{userId}/store/{storeId}/to/cart")
    public Response getInfoResponse(@PathParam("orderId") String orderId, @PathParam("userId") String userId, @PathParam("storeId") String storeId,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(orderId, userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo( String orderId, String userId, String storeId, String authToken) throws Exception, IOException {
        prepareResponse(orderId, userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public ChangeOrderGet(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String orderId, String userId, String storeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Order
                + ApplicationConstants.StringConstants.backSlash + orderId + ApplicationConstants.StringConstants.user
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.toCart;
    }
}

