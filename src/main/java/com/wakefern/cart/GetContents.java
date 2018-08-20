package com.wakefern.cart;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(MWGApplicationConstants.Requests.Cart.prefix)
public class GetContents extends BaseService {

	private final static Logger logger = Logger.getLogger("GetContents");
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetContents() {
        this.requestPath = MWGApplicationConstants.Requests.Cart.prefix + MWGApplicationConstants.Requests.Cart.cart;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Cart.cart)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,

    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String wfStoreID,
    		@DefaultValue("") @QueryParam(MWGApplicationConstants.Requests.Params.Query.itemLocator) String itemLocator,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept
	) throws Exception, IOException {
        		
		this.requestHeader = new MWGHeader(accept, MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		
        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.cart.GetContents");
            
            // Item Location data provided by MWG is never up-to-date.
            // Used Wakefern-supplied Item Location data instead.
            // only call item locator when 'In-Store Checklist' option is selected, prevent unnecessary call.
            if(itemLocator!= null && !itemLocator.isEmpty()) {
            		if(itemLocator.equalsIgnoreCase("log")) { //only log cart response when user select 'Cart' Module
            			logger.log(Level.INFO, "Cart Response: "+jsonResponse);
            		} else { // log the cart resp anyway, since not many user select 'In-Store Checklist' option
                		jsonResponse = this.getItemLocations(jsonResponse, wfStoreID);
            			logger.log(Level.INFO, "Getting In-Store Checklist :"+userID);
            		}
            }

            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}

