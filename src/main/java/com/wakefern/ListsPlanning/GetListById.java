package com.wakefern.ListsPlanning;

/**
 * Created by zacpuste on 10/6/16.
 */

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
public class GetListById extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/list/{listId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("listId") String listId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, listId, isMember, authToken);

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

    public String getInfo(String userId, String listId, String isMember,  String authToken) throws Exception, IOException {
        prepareResponse(userId, listId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xml);
    }

    public GetListById() {
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String listId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Planning.ShoppingListUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Planning.ShoppingListUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.list
                    + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.isMember;
        }
    }
}