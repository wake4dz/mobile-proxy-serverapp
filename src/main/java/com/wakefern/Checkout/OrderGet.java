package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/1/16.
 */
@Path(ApplicationConstants.Requests.Checkout.Order)
public class OrderGet extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{orderId}/user/{userId}")
    public String getInfo( @PathParam("orderId") String orderId, @PathParam("userId") String userId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Order
                + ApplicationConstants.StringConstants.backSlash + orderId + ApplicationConstants.StringConstants.user
                + ApplicationConstants.StringConstants.backSlash + userId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);


        System.out.print("Path: " + secondMapping.getPath());
        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public OrderGet(){
        this.serviceType = new MWGHeader();
    }
}

