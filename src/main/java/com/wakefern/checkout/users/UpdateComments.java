package com.wakefern.checkout.users;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class UpdateComments extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public UpdateComments() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.comments;
    }
    
	@PUT
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.comments)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Checkout.comments, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.PUT, jsonData, "com.wakefern.checkout.users.UpdateComments");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}


