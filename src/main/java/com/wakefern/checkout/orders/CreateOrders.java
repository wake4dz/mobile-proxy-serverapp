package com.wakefern.checkout.orders;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class CreateOrders extends BaseService {
	
	private int timeout = 40000;
    private final static Logger logger = Logger.getLogger("CreateOrders");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CreateOrders() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.orders;
    }
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.orders)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonString
	) throws Exception, IOException {
		long startTime, endTime;
		startTime = System.currentTimeMillis();
        		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Checkout.orders, sessionToken);
		this.requestParams = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
		
        try {
        		this.setTimeout(timeout);
            String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonString, "package com.wakefern.checkout.orders.CreateOrders");
			endTime = System.currentTimeMillis();
    			logger.log(Level.INFO, "[CreateOrders::getResponse]::processing time: "+(endTime - startTime)+"-"+mwgStoreID+"-"+jsonResponse);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        		endTime = System.currentTimeMillis();
        		logger.log(Level.SEVERE, "[CreateOrders::getResponse]::Error placing order: "+(endTime - startTime)+" "+userID+" "+mwgStoreID+" "+e.getMessage());
            return this.createErrorResponse(e);
        }
    }
}


