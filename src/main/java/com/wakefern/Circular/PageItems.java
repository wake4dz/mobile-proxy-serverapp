package com.wakefern.Circular;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/14/16.
 */
@Path(ApplicationConstants.Requests.Circular.Categories)
public class PageItems extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/{circularId}/pages/{pageId}/items")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("circularId") String circularId, @PathParam("pageId") String pageId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Circular.Categories
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.backSlash + pageId + ApplicationConstants.StringConstants.items;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public PageItems(){
        this.serviceType = new MWGHeader();
    }
}