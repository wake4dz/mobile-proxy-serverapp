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
@Path(ApplicationConstants.Requests.Checkout.UserDelivery)
public class FufillmentDelivery extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/address/delivery")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("userId") String userId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.UserDelivery + ApplicationConstants.StringConstants.backSlash
                + userId + ApplicationConstants.StringConstants.store + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.address + ApplicationConstants.StringConstants.delivery;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public FufillmentDelivery(){
        this.serviceType = new MWGHeader();
    }
}
