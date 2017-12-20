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
public class Suggestions extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Suggestions() {
        this.requestPath = MWGApplicationConstants.Requests.Products.productPath + MWGApplicationConstants.Requests.Products.suggestedProds;
    }
    
	@GET
    @Consumes(ApplicationConstants.jsonAcceptType)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Products.suggestedProds)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		
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
        		
		this.requestHeader = new MWGHeader(ApplicationConstants.jsonAcceptType, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
				
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

