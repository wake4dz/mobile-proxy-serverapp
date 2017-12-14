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
public class FeaturedInCategory extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}/featured")
    public Response getInfoResponse(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(categoryId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String categoryId, String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(categoryId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public FeaturedInCategory(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String categoryId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Categories.ProductCategory
                + ApplicationConstants.StringConstants.backSlash + categoryId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.featured;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Categories.ProductCategory
                    + ApplicationConstants.StringConstants.backSlash + categoryId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.featured
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
