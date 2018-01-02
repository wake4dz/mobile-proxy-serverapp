package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Checkout.Checkout)
public class StoreFulfillment extends BaseService {
    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("/{storeId}")
    public Response getInfoResponse(@PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.links+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(HTTPRequest.executeGet( secondMapping.getPath(), map, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.links+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executeGet( secondMapping.getPath(), map, 0);
    }

    public StoreFulfillment(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash + storeId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash
                    + storeId + ApplicationConstants.StringConstants.isMember;
        }
    }
}
