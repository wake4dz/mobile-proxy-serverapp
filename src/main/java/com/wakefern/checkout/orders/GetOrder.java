package com.wakefern.checkout.orders;


import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetOrder extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetOrder.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetOrder() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.order;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.Checkout.orderV3)
    @Path(MWGApplicationConstants.Requests.Checkout.order)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.orderID) String orderID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.orderV3, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.orderID, orderID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "package com.wakefern.checkout.orders.GetOrder");
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("orderID", orderID, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.ORDERS_GET_ORDER);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"orderID", orderID, "userID", userID, "sessionToken", sessionToken );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}


