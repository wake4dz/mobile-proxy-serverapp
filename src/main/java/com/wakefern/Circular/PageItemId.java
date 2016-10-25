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
public class PageItemId extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/{circularId}/pages/{pageId}/items/{itemId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("circularId") String circularId, @PathParam("pageId") String pageId,
                                    @QueryParam("isMember") String isMember,
                            @PathParam("itemId") String itemId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, circularId, pageId, itemId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String storeId, String circularId, String pageId, String itemId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, circularId, pageId, itemId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public PageItemId(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String storeId, String circularId, String pageId, String itemId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Circular.Categories
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.backSlash + pageId + ApplicationConstants.StringConstants.items
                + ApplicationConstants.StringConstants.backSlash + itemId;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Circular.Categories
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                    + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.pages
                    + ApplicationConstants.StringConstants.backSlash + pageId + ApplicationConstants.StringConstants.items
                    + ApplicationConstants.StringConstants.backSlash + itemId + ApplicationConstants.StringConstants.isMember;
        }
    }
}