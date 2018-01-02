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
public class ByCategory extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public ByCategory() {
        this.requestPath = MWGApplicationConstants.Requests.Products.productPath + MWGApplicationConstants.Requests.Products.prodsByCat;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.productList)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Products.prodsByCat)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.categoryID) String categoryID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.excluded) String prodsToExclude,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String prodfilters,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.sortOrder) String sortOrder,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchBySound) String searchBySound,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
    		
	) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.productList, ApplicationConstants.jsonHeaderType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.categoryID, categoryID);
		
		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.excluded, prodsToExclude);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, prodfilters);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, searchTerm);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sortOrder, sortOrder);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchBySound, searchBySound);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

