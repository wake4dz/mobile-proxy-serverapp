package com.wakefern.products;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

/**
 * Use this endpoint to grab all Products covered by a given Circular Item. 
 * This happens when an end user is viewing a Circular Page and clicks an Item's "See Products" button.
 */
@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetCircularItemProducts extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetCircularItemProducts() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.circItemProds;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.circItemProds)
    
	public Response getResponse(
		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
		@PathParam(MWGApplicationConstants.Requests.Params.Path.circItemID) String circItemID,
		
		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
		
		@DefaultValue("0")    @QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
		@DefaultValue("9999") @QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
		
		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.circItemProds, MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.circItemID, circItemID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
		
		// Build the Map of Query String parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.GetCircularItemProducts");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

