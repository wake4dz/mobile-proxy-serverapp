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

@Path(ApplicationConstants.Requests.Checkout.Checkout)
public class FulfillmentDeliveryDates extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/delivery/{zipCode}/dates")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("zipCode") String zipCode,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.delivery + ApplicationConstants.StringConstants.backSlash
                + zipCode + ApplicationConstants.StringConstants.dates;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public FulfillmentDeliveryDates(){
        this.serviceType = new MWGHeader();
    }
}