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
@Path(ApplicationConstants.Requests.Checkout.Payments)
public class PaymentOptions extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/{fulfillmentType}")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("fulfillmentType") String fulfillmentType,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, fulfillmentType, isMember, authToken);

        Map<String, String> pickup = new HashMap<>();
        String path = "https://shop.shoprite.com/api" + this.path;
        pickup.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.payments-v2+json");
        pickup.put("Authorization", authToken);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(path, pickup, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String fulfillmentType, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, fulfillmentType, isMember, authToken);

        Map<String, String> pickup = new HashMap<>();
        String path = "https://shop.shoprite.com/api" + this.path;
        pickup.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.payments-v2+json");
        pickup.put("Authorization", authToken);

        return HTTPRequest.executeGet(path, pickup, 0);
    }

    public PaymentOptions(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String storeId, String fulfillmentType, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Payments + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.backSlash + fulfillmentType;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Checkout.Payments + ApplicationConstants.StringConstants.backSlash
                    + storeId + ApplicationConstants.StringConstants.backSlash + fulfillmentType
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
