package com.wakefern.products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.Search;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/4/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductsStore)
public class CircularItemSearch extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/circular-item/{circularId}")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("circularId") String circularId,
                            @DefaultValue("9999") @QueryParam("take") String take, @DefaultValue("0") @QueryParam("skip") String skip,
                            @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, circularId, take, skip, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String circularId, String take, String skip, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, circularId, take, skip, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public CircularItemSearch() {
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String storeId, String circularId, String take, String skip, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Categories.ProductsStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circularItem
                + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.take
                + take + ApplicationConstants.StringConstants.skip + skip;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Categories.ProductsStore
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circularItem
                    + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.take
                    + take + ApplicationConstants.StringConstants.skip + skip + ApplicationConstants.StringConstants.isMember;
        }
    }
}
