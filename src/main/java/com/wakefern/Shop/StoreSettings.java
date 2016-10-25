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
public class StoreSettings extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/settings")
    public Response getInfoResponse(@PathParam("storeId") String storeId,
                                    @QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String isMember,  String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public StoreSettings(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String storeId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Shop.ShopStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.settings;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Shop.ShopStore
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.settings
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
