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
 * Created by zacpuste on 8/24/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class CheckoutFulfillmentGet extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/fulfillment")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.fulfillment-slot+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), map, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.fulfillment-slot+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executeGet( secondMapping.getPath(), map, 0);
    }

    public CheckoutFulfillmentGet(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.UserCheckout
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.fulfillment;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.UserCheckout
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.fulfillment
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
