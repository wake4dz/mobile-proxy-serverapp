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
@Path(ApplicationConstants.Requests.Categories.ProductStore)
public class FeaturedInStore extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/featured")
    public String getInfo(@PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.featured;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public FeaturedInStore(){
        this.serviceType = new MWGHeader();
    }
}
