package com.wakefern.Products;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/18/16.
 */

@Path(ApplicationConstants.Requests.Categories.CategoriesFromStoreId)
public class Categories extends BaseService {

    @GET
    @Produces("application/*")
    @Path("{storeId}")
    public Response getInfoResponse(@PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try {
            return this.createValidResponse(HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public Categories(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String storeId, String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + ApplicationConstants.StringConstants.backSlash
                + storeId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + ApplicationConstants.StringConstants.backSlash
                    + storeId + ApplicationConstants.StringConstants.isMember;
        }
    }
}
