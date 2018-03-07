package com.wakefern.shoppingLists;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path(MWGApplicationConstants.Requests.ShoppingList.prefix)
public class GetDuplicateListItemLimit extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetDuplicateListItemLimit() {
        this.requestPath = MWGApplicationConstants.Requests.ShoppingList.prefix + MWGApplicationConstants.Requests.ShoppingList.copyLimit;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.ShoppingList.copyLimit)
    
	public Response getResponse(    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	
	) throws Exception, IOException {
		
		// This is a custom endpoint.
		// All it does is return the default cap placed on the number of records that the CreateDuplicate endpoint will copy from one list to the next.
		// Note that this value can be overridden by the UI, by passing the "take" query parameter to the CreateDuplicate endpoint.
		//
		// At the moment, the default cap is just a hardcoded value.
		// So we manually construct a response string and send it back to the UI.
		String response = "{ \"data\" : { \"itemLimit\" : \"" + MWGApplicationConstants.Requests.ShoppingList.strDupeCap + "\" } }";

        return this.createValidResponse(response);
    }
}

