package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Checkout.Checkout)
public class StoreFulfillment extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}")
    public String getInfo(@PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash + storeId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet( secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public StoreFulfillment(){
        this.serviceType = new MWGHeader();
    }
}