package com.wakefern.checkout.fulfillments;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetDeliveryDates extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetDeliveryDates() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.deliveryDates;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.deliveryDates)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.zipCode) String zipCode,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.fulfillDates, MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.zipCode, zipCode);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}


