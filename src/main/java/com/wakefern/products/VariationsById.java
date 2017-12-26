package com.wakefern.products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.productPath)
public class VariationsById extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public VariationsById() {
        this.requestPath = MWGApplicationConstants.Requests.Products.productPath + MWGApplicationConstants.Requests.Products.prodVarsByID;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.product)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Products.prodVarsByID)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.productID) String productID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@QueryParam(MWGApplicationConstants.queryExcluded) String excludedProds,
    		@QueryParam(MWGApplicationConstants.queryFilters) String searchFilters,
    		@QueryParam(MWGApplicationConstants.queryIsMember) String isMember,
    		@QueryParam(MWGApplicationConstants.querySearchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.querySkip) String skipCount,
    		@QueryParam(MWGApplicationConstants.querySortOrder) String sortOrder,
    		@QueryParam(MWGApplicationConstants.querySeachBySound) String searchBySound,
    		@QueryParam(MWGApplicationConstants.queryTake) String takeCount,
    		@QueryParam(MWGApplicationConstants.queryUserID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.product, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.productID, productID);
		
		// Build the Map of Query String parameters
		this.queryParams.put(MWGApplicationConstants.queryExcluded, excludedProds);
		this.queryParams.put(MWGApplicationConstants.queryFilters, searchFilters);
		this.queryParams.put(MWGApplicationConstants.queryIsMember, isMember);
		this.queryParams.put(MWGApplicationConstants.querySearchTerm, searchTerm);
		this.queryParams.put(MWGApplicationConstants.querySkip, skipCount);
		this.queryParams.put(MWGApplicationConstants.querySortOrder, sortOrder);
		this.queryParams.put(MWGApplicationConstants.querySeachBySound, searchBySound);
		this.queryParams.put(MWGApplicationConstants.queryTake, takeCount);
		this.queryParams.put(MWGApplicationConstants.queryUserID, userID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

