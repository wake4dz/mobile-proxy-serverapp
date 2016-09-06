package com.wakefern.Products;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Categories.Subcategories)
public class Subcategories extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}/categories")
    public String getInfo(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Categories.Subcategories + ApplicationConstants.StringConstants.backSlash
                + categoryId + ApplicationConstants.StringConstants.store + ApplicationConstants.StringConstants.backSlash + storeId
                + ApplicationConstants.StringConstants.categories;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet( secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public Subcategories(){
        this.serviceType = new MWGHeader();
    }
}
