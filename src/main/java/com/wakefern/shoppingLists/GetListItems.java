package com.wakefern.shoppingLists;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)
public class GetListItems extends BaseService {

	private final static Logger logger = Logger.getLogger("GetListItems");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetListItems() {
        this.requestPath = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.items;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.ShoppingList.items)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.listID) String listID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
    		@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String fd,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.categoryMap) String catMap, // Sort of like a "cat nap", but not really.  :-)
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.items, MWGApplicationConstants.Headers.generic, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.listID, listID);
		
		// Build the Map of Query String parameters.
		//this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeID, storeID); - NOTE: Including Store ID causes MWG to return a "Chain Not Found" error.
		
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.categoryMap, catMap);
		if(!fd.isEmpty()) { 
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.ShoppingList.wakefernItems, MWGApplicationConstants.Headers.generic, sessionToken);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, fd);
		}

        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.shoppingLists.GetListItems");
            
            // Item Location data provided by MWG is never up-to-date.
            // Used Wakefern-supplied Item Location data instead.
            // The Store ID being passed here, is Wakefern's version.
            jsonResponse = this.getItemLocations(jsonResponse, storeID);

            logger.log(Level.INFO, "Cart Response: "+jsonResponse);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

