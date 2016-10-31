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
@Path(ApplicationConstants.Requests.Categories.ProductCategory)
public class ProductsInCategory extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}")
    public Response getInfoResponse(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("fq") String fq, @DefaultValue("")@QueryParam("sort") String sort,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(categoryId, storeId, fq, sort, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String categoryId, String storeId, String fq, String sort, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(categoryId, storeId, fq, sort, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public ProductsInCategory(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String categoryId, String storeId, String fq, String sort, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductCategory
                + ApplicationConstants.StringConstants.backSlash + categoryId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.take
                + ApplicationConstants.StringConstants.twenty;

        if(sort != ""){
            this.path = this.path + ApplicationConstants.StringConstants.sort + sort;
        }

        if(fq != ""){
            this.path = this.path + ApplicationConstants.StringConstants.fq + fq;
        }

        if(!isMember.isEmpty()){
            this.path = this.path + ApplicationConstants.StringConstants.isMember;
        }
    }
}
