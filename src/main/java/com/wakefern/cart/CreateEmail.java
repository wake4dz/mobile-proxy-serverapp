package com.wakefern.cart;

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
public class CreateEmail extends BaseService {
	
	//TODO this class name is misleading one. MWG's end point name is com.wakefern.cart.UpdateItem
	private final static Logger logger = Logger.getLogger(CreateEmail.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CreateEmail() {
        this.requestPath = MWGApplicationConstants.Requests.Cart.prefix + MWGApplicationConstants.Requests.Cart.cart;
    }
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Cart.cart)
    public Response getResponse(    
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,

    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.evtParams) String evtPararms,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,

    		String jsonData
	) {
		try {		
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Cart.email, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			
			// Query string parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.evtParams, evtPararms);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonData, "com.wakefern.cart.UpdateItem");
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("storeId", storeID, "evtParams", evtPararms, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonData);
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {

        	LogUtil.addErrorMaps(e, MwgErrorType.CART_CREATE_EMAIL);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID, 
        			"userId", userID, "evtParams", evtPararms, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonData );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}


