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
public class StoreFulfillmentPickupDateTimes extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{storeId}/pickup/{year}/{month}/{day}/times")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("year") String year, @PathParam("month") String month, @PathParam("day") String day,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.pickup + ApplicationConstants.StringConstants.backSlash
                + year + ApplicationConstants.StringConstants.backSlash + month
                + ApplicationConstants.StringConstants.backSlash + day + ApplicationConstants.StringConstants.times;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public StoreFulfillmentPickupDateTimes(){
        this.serviceType = new MWGHeader();
    }
}
