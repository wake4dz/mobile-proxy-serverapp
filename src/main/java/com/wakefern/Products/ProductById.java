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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductId)
public class ProductById extends BaseService {
	
	private final static Logger logger = Logger.getLogger("ProductById");
	
    @GET
    @Produces("application/*")
    @Path("/{productId}/store/{storeId}")
    public Response getInfoResponse(@PathParam("productId") String productId, @PathParam("storeId") String storeId, @DefaultValue("") @QueryParam("circularId") String circId,
                            @QueryParam("page") String page, @QueryParam("circularItemId") String circularItemId,
                            @DefaultValue("")@QueryParam("isMember") String isMember, @HeaderParam("Authorization") String authToken) throws Exception, IOException {

        if(circId != null && !circId.isEmpty()){//(circId != ""){     
            prepareEmpty(storeId, circId, page, circularItemId, isMember, authToken);

            PageItemId pageItemId = new PageItemId();
            String pageItemIdResp = pageItemId.getInfo("FBFB1313", storeId, circId, page, circularItemId, isMember, authToken);

            return this.createValidResponse(pageItemIdResp);
        } else {

            logger.log(Level.INFO, "[getInfoResponse]::Circular is empty");
            prepareCirc(productId, storeId, isMember, authToken);

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setGetMapping(this, null, null);

            try {
                return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
            } catch (Exception e){
            	logger.log(Level.SEVERE, "[getInfoResponse]::Exception processing product Id", e);
                return this.createErrorResponse(e);
            }
        }
    }

    public String getInfo(String productId, String storeId, String circId, String page, String circularItemId, String isMember, String authToken) throws Exception, IOException {
        if(circId != null && !circId.isEmpty()){//(circId != ""){
        	logger.log(Level.INFO, "[getInfo]::circId not empty");
            prepareEmpty(storeId, circId, page, circularItemId, isMember, authToken);

            PageItemId pageItemId = new PageItemId();
            String pageItemIdResp = pageItemId.getInfo("FBFB1313", storeId, circId, page, circularItemId, isMember, authToken);
            return pageItemIdResp;
        } else {
            prepareCirc(productId, storeId, isMember, authToken);

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setGetMapping(this, null, null);

            return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
        }
    }

    public ProductById(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareEmpty(String storeId, String circId, String page, String circularItemId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Circular.Categories +
                ApplicationConstants.StringConstants.backSlash + "FBFB1313" + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.backSlash + page + ApplicationConstants.StringConstants.items
                + ApplicationConstants.StringConstants.backSlash + circularItemId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Circular.Categories +
                    ApplicationConstants.StringConstants.backSlash + "FBFB1313" + ApplicationConstants.StringConstants.stores
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                    + ApplicationConstants.StringConstants.backSlash + circId + ApplicationConstants.StringConstants.pages
                    + ApplicationConstants.StringConstants.backSlash + page + ApplicationConstants.StringConstants.items
                    + ApplicationConstants.StringConstants.backSlash + circularItemId
                    + ApplicationConstants.StringConstants.isMemberAmp;
        }
        logger.log(Level.INFO, "[prepareEmpty]::Product Id path: ", this.requestPath);
    }

    private void prepareCirc(String productId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Categories.ProductId
                + ApplicationConstants.StringConstants.backSlash + productId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Categories.ProductId
                    + ApplicationConstants.StringConstants.backSlash + productId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.isMember;
        }
        logger.log(Level.INFO, "[prepareCirc]::circ path: ", this.requestPath);
    }
}
