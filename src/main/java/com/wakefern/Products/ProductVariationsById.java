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
@Path(ApplicationConstants.Requests.Categories.ProductId)
public class ProductVariationsById extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{productId}/store/{storeId}/all/variations")
    public String getInfo(@PathParam("productId") String productId, @PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductId
                + ApplicationConstants.StringConstants.backSlash + productId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.all
                + ApplicationConstants.StringConstants.variations;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public ProductVariationsById(){
        this.serviceType = new MWGHeader();
    }
}
