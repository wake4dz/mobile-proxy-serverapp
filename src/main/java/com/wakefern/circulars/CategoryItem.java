package com.wakefern.circulars;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Circulars.circularsPath)
public class CategoryItem extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CategoryItem() {
        this.requestPath = MWGApplicationConstants.Requests.Circulars.circularsPath + MWGApplicationConstants.Requests.Circulars.categoryItem;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Circulars.item)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Circulars.categoryItem)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		@PathParam(MWGApplicationConstants.pathChainID) String chainID,    		
    		@PathParam(MWGApplicationConstants.pathCategoryID) String categoryID,   
    		@PathParam(MWGApplicationConstants.pathItemID) String itemID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.item, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
		this.requestParams.put(MWGApplicationConstants.pathChainID, chainID);
		this.requestParams.put(MWGApplicationConstants.pathCategoryID, categoryID);
		this.requestParams.put(MWGApplicationConstants.pathItemID, itemID);
				
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

