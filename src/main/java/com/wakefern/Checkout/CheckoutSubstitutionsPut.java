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
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class CheckoutSubstitutionsPut extends BaseService {
    @PUT
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("/{userId}/store/{storeId}/substitutions")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.substitutions+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(HTTPRequest.executePut(secondMapping.getPath(), secondMapping.getGenericBody(), map));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.substitutions+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executePut(secondMapping.getPath(), secondMapping.getGenericBody(), map);
    }

    public CheckoutSubstitutionsPut(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.substitutions;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                    + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.substitutions
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}