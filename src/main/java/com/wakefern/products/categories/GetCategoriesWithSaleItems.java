package com.wakefern.products.categories;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetCategoriesWithSaleItems extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetCategoriesWithSaleItems() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.catsWithSales;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.categories)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.catsWithSales)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.prodsPerCat) String productsPerCategory,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
    		
	) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.categories, ApplicationConstants.jsonHeaderType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		
		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.prodsPerCat, productsPerCategory);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}
