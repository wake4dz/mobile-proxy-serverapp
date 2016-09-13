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
@Path(ApplicationConstants.Requests.Categories.ProductStore)
public class NutritionBySku extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/sku/{skuId}/nutrition")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("skuId") String skuId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.sku
                + ApplicationConstants.StringConstants.backSlash + skuId + ApplicationConstants.StringConstants.nutrition;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());

    }
    public NutritionBySku(){
        this.serviceType = new MWGHeader();
    }
}
