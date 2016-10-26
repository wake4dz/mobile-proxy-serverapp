package com.wakefern.Circular;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularPages extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/{circId}/pages")
    public Response getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("circId") String circId,
                            @DefaultValue("0") @QueryParam("skip") String skip, @DefaultValue("9999") @QueryParam("take") String take,
                            @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        String path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.Circular.Categories
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.take + take + ApplicationConstants.StringConstants.skip + skip;

        if(!isMember.isEmpty()){
            path += ApplicationConstants.StringConstants.isMemberAmp;
        }

        MWGHeader mwgHeader = new MWGHeader();
        mwgHeader.authenticate(authToken, "application/vnd.mywebgrocer.circular-pages-full+json", "application/vnd.mywebgrocer.circular-pages-full+json");

        try {
            return this.createValidResponse(HTTPRequest.executeGetJSON(path, mwgHeader.getMap()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public CircularPages() {
        this.serviceType = new MWGHeader();
    }
}
