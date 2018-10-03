package com.wakefern.checkout.chains;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class DeletePromoCode extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public DeletePromoCode() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.promoCodeDelete;
    }
    
	@DELETE
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.promoCodeDelete)
    
	public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.promoCode) String promoCode,
    		    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		
    		String jsonData
    		
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.promoCode, promoCode);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.DELETE, jsonData, "com.wakefern.checkout.chains.DeletePromoCode");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}


