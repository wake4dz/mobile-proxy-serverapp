package com.wakefern.shoppingLists;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)
public class GetListItem extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetListItem() {
        this.requestPath = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.item;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.ShoppingList.item)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.listID) String listID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.listItemID) String itemID,

    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.item, MWGApplicationConstants.Headers.generic, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listItemID, itemID);

		// Build the Map of Query String parameters.
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeID, storeID);

        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

