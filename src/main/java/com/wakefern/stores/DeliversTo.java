package com.wakefern.stores;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Stores.storesPath)
public class DeliversTo extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public DeliversTo() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.storesPath + MWGApplicationConstants.Requests.Stores.delivers;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Stores.delivers)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Stores.delivers)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathChainID) String chainID, 
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		@PathParam(MWGApplicationConstants.pathZipCode) String zipCode,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.delivers, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
	
		this.requestParams.put(MWGApplicationConstants.pathChainID, chainID);
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
		this.requestParams.put(MWGApplicationConstants.pathZipCode, zipCode);
	
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
}
