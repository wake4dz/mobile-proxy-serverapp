package com.wakefern.shop;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Shop.prefix)
public class CreateCustServFeedbackMsg extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CreateCustServFeedbackMsg() {
        this.requestPath = MWGApplicationConstants.Requests.Shop.prefix + MWGApplicationConstants.Requests.Shop.message;
    }
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.Shop.message)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Shop.message)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData
	) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(ApplicationConstants.jsonHeaderType, MWGApplicationConstants.Headers.Shop.message, sessionToken);
		this.requestParams = new HashMap<String, String>();
	
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
	
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonData);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
}


