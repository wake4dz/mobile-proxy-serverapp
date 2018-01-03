package com.wakefern.products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class Search extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Search() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.prodSearch;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.prodSearch)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.excluded) String excludedProds,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String searchFilters,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skipCount,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.sortOrder) String sortOrder,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchBySound) String searchBySound,
    		@DefaultValue("9999") @QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String takeCount,  // MWG Docs claim this is optional, but will return an HTTP 400 if it's missing. 
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.productList, MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		
		// Build the Map of Query String parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.excluded, excludedProds);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, searchFilters);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, searchTerm);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skipCount);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sortOrder, sortOrder);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchBySound, searchBySound);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, takeCount);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

