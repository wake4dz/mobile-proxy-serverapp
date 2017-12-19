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
    		@PathParam(MWGApplicationConstants.chainID) String chainID, 
    		@PathParam(MWGApplicationConstants.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.zipCode) String zipCode,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.delivers, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
	
		this.requestParams.put(MWGApplicationConstants.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.zipCode, zipCode);
	
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
}
