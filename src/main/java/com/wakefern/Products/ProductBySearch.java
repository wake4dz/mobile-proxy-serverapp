package com.wakefern.Products;

import com.ibm.json.java.JSONObject;
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
@Path(ApplicationConstants.Requests.Categories.ProductsStore)
public class ProductBySearch extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/search")
    public String getInfo(@PathParam("storeId") String storeId,  @QueryParam("q") String q,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductsStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.search
                + ApplicationConstants.StringConstants.queryParam + q + ApplicationConstants.StringConstants.takeAmp
                + ApplicationConstants.StringConstants.twenty + ApplicationConstants.StringConstants.skip
                + ApplicationConstants.StringConstants.zero;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public ProductBySearch(){
        this.serviceType = new MWGHeader();
    }
}