package com.wakefern.Products;

import com.wakefern.Circular.PageItemId;
import com.wakefern.Circular.RetrieveCircular;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductId)
public class ProductById extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{productId}/store/{storeId}")
    public String getInfo(@PathParam("productId") String productId, @PathParam("storeId") String storeId, @DefaultValue("") @QueryParam("circularId") String circId,
                          @QueryParam("page") String page, @QueryParam("product") String product, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        if(circId != ""){
            this.path = ApplicationConstants.Requests.Circular.Categories +
                    ApplicationConstants.StringConstants.backSlash + "FBFB1313" + ApplicationConstants.StringConstants.stores
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                    + ApplicationConstants.StringConstants.backSlash + circId + ApplicationConstants.StringConstants.pages
                    + ApplicationConstants.StringConstants.backSlash + page + ApplicationConstants.StringConstants.items
                    + ApplicationConstants.StringConstants.backSlash + product;

            PageItemId pageItemId = new PageItemId();
            return pageItemId.getInfo("FBFB1313", storeId, circId, page, product, authToken);
        } else {
            this.path = ApplicationConstants.Requests.Categories.ProductId
                    + ApplicationConstants.StringConstants.backSlash + productId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId;

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setMapping(this);

            return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
        }



    }
    public ProductById(){
        this.serviceType = new MWGHeader();
    }
}
