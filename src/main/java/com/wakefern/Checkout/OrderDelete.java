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
public class OrderDelete extends BaseService {
    @DELETE
    @Produces("application/*")
    @Path("/{orderId}/user/{userId}")
    public Response getInfoResponse(@PathParam("orderId") String orderId, @PathParam("userId") String userId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(orderId, userId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader());
            return this.createValidDelete();
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String orderId, String userId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(orderId, userId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeDelete(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public OrderDelete(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String orderId, String userId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.Order
                + ApplicationConstants.StringConstants.backSlash + orderId + ApplicationConstants.StringConstants.user
                + ApplicationConstants.StringConstants.backSlash + userId;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Checkout.Order
                    + ApplicationConstants.StringConstants.backSlash + orderId + ApplicationConstants.StringConstants.user
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.isMember;
        }
    }
}

