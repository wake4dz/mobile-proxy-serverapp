package com.wakefern.ListsPlanning;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(ApplicationConstants.Requests.Planning.ShoppingListUser)
public class GetUserLists extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        try {
            String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            return this.createValidResponse(xmLtoJSONConverter.convert(xml));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String authToken) throws Exception, IOException {
        prepareResponse(userId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xml);
    }

    public GetUserLists() {
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Planning.ShoppingListUser
                + ApplicationConstants.StringConstants.backSlash + userId;
    }
}