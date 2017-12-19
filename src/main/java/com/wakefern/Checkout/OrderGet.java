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
 * Created by zacpuste on 9/1/16.
 */
@Path(ApplicationConstants.Requests.Checkout.Order)
public class OrderGet extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{orderId}/user/{userId}")
    public Response getInfoResponse(@PathParam("orderId") String orderId, @PathParam("userId") String userId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(orderId, userId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-detail+json");
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-detail-v2+json");
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-detail-v3+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try{
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), map, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String orderId, String userId, String isMember,  String authToken) throws Exception, IOException {
        prepareResponse(orderId, userId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-detail+json");
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-detail-v2+json");
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-detail-v3+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executeGet(secondMapping.getPath(), map, 0);
    }

    public OrderGet(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String orderId, String userId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.Order
                + ApplicationConstants.StringConstants.backSlash + orderId + ApplicationConstants.StringConstants.user
                + ApplicationConstants.StringConstants.backSlash + userId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.Order
                    + ApplicationConstants.StringConstants.backSlash + orderId + ApplicationConstants.StringConstants.user
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.isMember;
        }
    }
}

