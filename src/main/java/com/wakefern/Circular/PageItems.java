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
 * Created by zacpuste on 9/14/16.
 */
@Path(ApplicationConstants.Requests.Circular.Categories)
public class PageItems extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/{circularId}/pages/{pageId}/items")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("circularId") String circularId, @PathParam("pageId") String pageId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, circularId, pageId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String storeId, String circularId, String isMember, String pageId, String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, circularId, pageId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public PageItems(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String storeId, String circularId, String pageId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Circular.Categories
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.backSlash + pageId + ApplicationConstants.StringConstants.items;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Circular.Categories
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                    + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.pages
                    + ApplicationConstants.StringConstants.backSlash + pageId + ApplicationConstants.StringConstants.items
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}