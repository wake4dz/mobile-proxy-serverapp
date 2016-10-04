package com.wakefern.Products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.Search;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 10/4/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductsStore)
public class CircularItemSearch extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/circular-item/{circularId}")
    public String getInfo(@PathParam("storeId") String storeId, @PathParam("circularId") String circularId,
                          @DefaultValue("9999") @QueryParam("take") String take,@DefaultValue("0") @QueryParam("skip") String skip,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductsStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circularItem
                + ApplicationConstants.StringConstants.backSlash + circularId + ApplicationConstants.StringConstants.take
                + take + ApplicationConstants.StringConstants.skip + skip;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader());
//        Search search = new Search();
//        return search.search(partialUrl, take, skip, "", "", authToken);
    }

    public CircularItemSearch() {
        this.serviceType = new MWGHeader();
    }
}
