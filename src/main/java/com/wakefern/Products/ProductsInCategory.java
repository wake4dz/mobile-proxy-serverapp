package com.wakefern.Products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductCategory)
public class ProductsInCategory extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}")
    public String getInfo(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductCategory
                + ApplicationConstants.StringConstants.backSlash + categoryId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public ProductsInCategory(){
        this.serviceType = new MWGHeader();
    }
}
