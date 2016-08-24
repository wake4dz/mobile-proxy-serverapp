package com.wakefern.Checkout;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/24/16.
 */
@Path(ApplicationConstants.Requests.Checkout.Payments)
public class PaymentOptions extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/{fulfillmentType}")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("fulfillmentType") String fulfillmentType,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Payments + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.backSlash + fulfillmentType;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public PaymentOptions(){
        this.serviceType = new MWGHeader();
    }
}
