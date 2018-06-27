package com.wakefern.checkout.chains;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class CreatePromoCode extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CreatePromoCode() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.promoCodes;
    }
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.promoCodes)
    
	public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		
    		String jsonData
    		
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Checkout.promoCodes, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonData, "com.wakefern.checkout.chains.GetPromoCodes");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}


