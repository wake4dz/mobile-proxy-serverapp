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
public class GetById extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetById() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.prodByID;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.product)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.prodByID)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.productID) String productID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.product, ApplicationConstants.jsonHeaderType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.productID, productID);
		
		// Build the Map of Query String parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

