package com.wakefern.Shop;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Shop.ShopStore)
public class CustomerFeedbackMessages extends BaseService {

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{storeId}/contact/messages")
    public Response getInfoResponse(@PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody, null, null);

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), mapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String authToken, String isMember, String jsonBody) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody, null, null);

        return (HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), mapping.getgenericHeader(), 0));
    }

    public CustomerFeedbackMessages(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Shop.ShopStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.contact
                + ApplicationConstants.StringConstants.messages;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Shop.ShopStore
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.contact
                    + ApplicationConstants.StringConstants.messages + ApplicationConstants.StringConstants.isMember;
        }
    }
}

