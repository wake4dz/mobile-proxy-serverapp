package com.wakefern.Circular;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularCategories extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/categories")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.categories;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return  HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public CircularCategories(){
        this.serviceType = new MWGHeader();
    }
}