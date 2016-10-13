package com.wakefern.Products;

import com.wakefern.Circular.PageItemId;
import com.wakefern.Circular.RetrieveCircular;
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
@Path(ApplicationConstants.Requests.Categories.ProductId)
public class ProductById extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{productId}/store/{storeId}")
    public Response getInfoResponse(@PathParam("productId") String productId, @PathParam("storeId") String storeId, @DefaultValue("") @QueryParam("circularId") String circId,
                            @QueryParam("page") String page, @QueryParam("circularItemId") String circularItemId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        if(circId != ""){
            prepareEmpty(storeId, circId, page, circularItemId, authToken);

            PageItemId pageItemId = new PageItemId();
            return this.createValidResponse(pageItemId.getInfo("FBFB1313", storeId, circId, page, circularItemId, authToken));
        } else {
            prepareCirc(productId, storeId, authToken);

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setMapping(this);

            try {
                return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader()));
            } catch (Exception e){
                return this.createErrorResponse(e);
            }
        }
    }

    public String getInfo(String productId, String storeId, String circId, String page, String circularItemId, String authToken) throws Exception, IOException {
        if(circId != ""){
            prepareEmpty(storeId, circId, page, circularItemId, authToken);

            PageItemId pageItemId = new PageItemId();
            return pageItemId.getInfo("FBFB1313", storeId, circId, page, circularItemId, authToken);
        } else {
            prepareCirc(productId, storeId, authToken);

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setMapping(this);

            return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
        }
    }

    public ProductById(){
        this.serviceType = new MWGHeader();
    }

    private void prepareEmpty(String storeId, String circId, String page, String circularItemId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Circular.Categories +
                ApplicationConstants.StringConstants.backSlash + "FBFB1313" + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.backSlash + page + ApplicationConstants.StringConstants.items
                + ApplicationConstants.StringConstants.backSlash + circularItemId;
    }

    private void prepareCirc(String productId, String storeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductId
                + ApplicationConstants.StringConstants.backSlash + productId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
    }
}
