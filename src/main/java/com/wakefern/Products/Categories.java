package com.wakefern.Products;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/18/16.
 */

@Path(ApplicationConstants.Requests.Categories.CategoriesFromStoreId)
public class Categories extends BaseService {

    @GET
    @Produces("application/*")
    @Path("{storeId}")
    public String getInfo(@PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + ApplicationConstants.StringConstants.backSlash
                 + storeId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public Categories(){
        this.serviceType = new MWGHeader();
    }
}
