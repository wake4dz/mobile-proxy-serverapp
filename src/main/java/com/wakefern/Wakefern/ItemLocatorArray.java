package com.wakefern.Wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/18/16.
 */

@Path(ApplicationConstants.Requests.Wakefern.ItemLocator)
public class ItemLocatorArray extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/{upc}")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("upc") String upc,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        String path = "http://devsrv86a.wakefern.com/api" + prepareResponse(storeId, upc, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(path, secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public ItemLocatorArray(){
        this.serviceType = new MWGHeader();
    }

    private String prepareResponse(String storeId, String upc, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Wakefern.ItemLocator
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.backSlash + upc;
        return this.path;
    }
}