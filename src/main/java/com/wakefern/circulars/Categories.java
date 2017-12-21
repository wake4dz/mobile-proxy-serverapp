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
public class Categories extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Categories() {
        this.requestPath = MWGApplicationConstants.Requests.Circulars.circularsPath + MWGApplicationConstants.Requests.Circulars.categories;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Circulars.categories)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Circulars.categories)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		@PathParam(MWGApplicationConstants.pathChainID) String chainID,    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.categories, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
		this.requestParams.put(MWGApplicationConstants.pathChainID, chainID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

