package com.wakefern.shop;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Shop.shopPath)
public class StoreSettings extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public StoreSettings() {
        this.requestPath = MWGApplicationConstants.Requests.Shop.shopPath + MWGApplicationConstants.Requests.Shop.settings;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Shop.settings)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Shop.settings)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Shop.settings, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
	
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
	
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
}

