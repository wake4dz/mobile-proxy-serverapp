package com.wakefern.api.mi9.v7.cart;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Cart.prefix)
public class Delete extends BaseService {
	
	private final static Logger logger = Logger.getLogger(Delete.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Delete() {
        this.requestPath = MWGApplicationConstants.Requests.Cart.prefix + MWGApplicationConstants.Requests.Cart.cart;
    }
    
	@DELETE
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Cart.cart)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.generic, MWGApplicationConstants.Headers.generic, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			

            String jsonResponse = this.mwgRequest(BaseService.ReqType.DELETE, null, "com.wakefern.cart.Delete");
            
			
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("storeId", storeID, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType  );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {

        	LogUtil.addErrorMaps(e, MwgErrorType.CART_DELETE);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID, 
        			"userId", userID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}
