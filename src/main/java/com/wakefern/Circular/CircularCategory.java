package com.wakefern.Circular;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularCategory extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/categories/{categoryId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("categoryId") String categoryId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken)throws Exception, IOException {
        prepareRespone(chainId, storeId, categoryId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try {
            return this.createValidResponse(HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String storeId, String categoryId, String isMember, String authToken)throws Exception, IOException {
        prepareRespone(chainId, storeId, categoryId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public CircularCategory(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareRespone(String chainId, String storeId, String categoryId, String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.categories + ApplicationConstants.StringConstants.backSlash
                + categoryId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                    + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                    + storeId + ApplicationConstants.StringConstants.categories + ApplicationConstants.StringConstants.backSlash
                    + categoryId + ApplicationConstants.StringConstants.isMember;
        }
    }
}
