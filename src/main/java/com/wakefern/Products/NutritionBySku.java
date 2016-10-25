package com.wakefern.Products;

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
@Path(ApplicationConstants.Requests.Categories.ProductStore)
public class NutritionBySku extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/sku/{skuId}/nutrition")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("skuId") String skuId,
                                    @QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, skuId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String skuId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, skuId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public NutritionBySku(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String storeId, String skuId, String isMember,  String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.sku
                + ApplicationConstants.StringConstants.backSlash + skuId + ApplicationConstants.StringConstants.nutrition;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Categories.ProductStore
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.sku
                    + ApplicationConstants.StringConstants.backSlash + skuId + ApplicationConstants.StringConstants.nutrition
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
