package com.wakefern.products.categories;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.productPath)
public class Subcategories extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Subcategories() {
        this.requestPath = MWGApplicationConstants.Requests.Products.productPath + MWGApplicationConstants.Requests.Products.subCategories;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.categories)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Products.subCategories)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		@PathParam(MWGApplicationConstants.pathParentCatID) String parentCategoryID,
    		
    		@QueryParam(MWGApplicationConstants.queryIsMember) String isMember,
    		@QueryParam(MWGApplicationConstants.queryProdsPerCat) String productsPerCategory,
    		@QueryParam(MWGApplicationConstants.querySaleOnlyProds) String onlySaleProducts,
    		@QueryParam(MWGApplicationConstants.queryUserID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	
	) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.categories, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
		
		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.queryIsMember, isMember);
		this.queryParams.put(MWGApplicationConstants.queryProdsPerCat, productsPerCategory);
		this.queryParams.put(MWGApplicationConstants.querySaleOnlyProds, onlySaleProducts);
		this.queryParams.put(MWGApplicationConstants.queryUserID, userID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}