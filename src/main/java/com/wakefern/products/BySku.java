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
public class BySku extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public BySku() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prodsByCat + MWGApplicationConstants.Requests.Products.categories;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.bySKU)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Products.prodsByCat)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathCategoryID) String categoryID,
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		
    		@QueryParam(MWGApplicationConstants.queryIsMember) String isMember,
    		@QueryParam(MWGApplicationConstants.queryUserID) String userID,
    		@QueryParam(MWGApplicationConstants.queryExcluded) String prodsToExclude,
    		@QueryParam(MWGApplicationConstants.queryFilters) String prodfilters,
    		@QueryParam(MWGApplicationConstants.querySearchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.querySkip) String skip,
    		@QueryParam(MWGApplicationConstants.querySortOrder) String sortOrder,
    		@QueryParam(MWGApplicationConstants.querySeachBySound) String searchBySound,
    		@QueryParam(MWGApplicationConstants.queryTake) String take,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
    		
	) throws Exception, IOException {
        		
        try {
            String jsonResponse = makeRequest(categoryID, storeID, isMember, userID, prodsToExclude, prodfilters, searchTerm, skip, sortOrder, searchBySound, take, sessionToken);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
	
	public String getInfo(
			String categoryID,
			String storeID,
			String isMember,
			String userID,
			String prodsToExclude,
			String prodfilters,
			String searchTerm,
			String skip,
			String sortOrder,
			String searchBySound,
			String take,
			String sessionToken
	) throws Exception {
		
		return makeRequest(categoryID, storeID, isMember, userID, prodsToExclude, prodfilters, searchTerm, skip, sortOrder, searchBySound, take, sessionToken);
	}
	
	private String makeRequest(
			String categoryID,
			String storeID,
			String isMember,
			String userID,
			String prodsToExclude,
			String prodfilters,
			String searchTerm,
			String skip,
			String sortOrder,
			String searchBySound,
			String take,
			String sessionToken
	) throws Exception {
		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.bySKU, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
		this.requestParams.put(MWGApplicationConstants.pathCategoryID, categoryID);
		
		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.queryIsMember, isMember);
		this.queryParams.put(MWGApplicationConstants.queryUserID, userID);
		this.queryParams.put(MWGApplicationConstants.queryExcluded, prodsToExclude);
		this.queryParams.put(MWGApplicationConstants.queryFilters, prodfilters);
		this.queryParams.put(MWGApplicationConstants.querySearchTerm, searchTerm);
		this.queryParams.put(MWGApplicationConstants.querySkip, skip);
		this.queryParams.put(MWGApplicationConstants.querySortOrder, sortOrder);
		this.queryParams.put(MWGApplicationConstants.querySeachBySound, searchBySound);
		this.queryParams.put(MWGApplicationConstants.queryTake, take);
		
		return this.mwgRequest(BaseService.ReqType.GET, null);
	}
}

