package com.wakefern.circulars;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Circulars.prefix)
public class GetPageItem extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetPageItem() {
        this.requestPath = MWGApplicationConstants.Requests.Circulars.prefix + MWGApplicationConstants.Requests.Circulars.pageItem;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Circulars.item)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Circulars.pageItem)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,    		
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.circularID) String circularID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.pageID) String pageID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.itemID) String itemID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.item, ApplicationConstants.jsonHeaderType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.circularID, circularID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.pageID, pageID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.itemID, itemID);
				
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

