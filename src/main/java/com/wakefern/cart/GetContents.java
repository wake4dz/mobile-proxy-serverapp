package com.wakefern.cart;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;


@Path(MWGApplicationConstants.Requests.Cart.prefix)
public class GetContents extends BaseService {

	private final static Logger logger = Logger.getLogger(GetContents.class);
	
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
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
		long startTime, endTime, actualTime;
		
		try {
			this.requestHeader = new MWGHeader(accept, contentType, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			
        	String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "wfStoreID", wfStoreID, "userID", userID, 
        			"itemLocator", itemLocator, "sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
        	startTime = System.currentTimeMillis();
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.cart.GetContents");
            
            // for warning any API call time exceeding its own upper limited time defined in mwgApiWarnTime.java
			endTime = System.currentTimeMillis();
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.CART_GET_CONTENTS.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is " +
						MwgApiWarnTime.CART_GET_CONTENTS.getWarnTime() + " ms. The track data: " + trackData);
			}
			
            // Item Location data provided by MWG is never up-to-date.
            // Used Wakefern-supplied Item Location data instead.
            // only call item locator when 'In-Store Checklist' option is selected, prevent unnecessary call.
            if(itemLocator!= null && !itemLocator.isEmpty()) {
            		if(itemLocator.equalsIgnoreCase("log")) { //only log cart response when user select 'Cart' Module
            			logger.info("Cart Response: "+jsonResponse);
            		} else { // log the cart resp anyway, since not many user select 'In-Store Checklist' option
            			
                		jsonResponse = this.getItemLocations(jsonResponse, wfStoreID);
            			logger.debug("Getting In-Store Checklist: "+userID);
            		}
            }

			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
       
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {

        	LogUtil.addErrorMaps(e, MwgErrorType.CART_GET_CONTENTS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "mwgStoreID", mwgStoreID, 
        			"userId", userID, "wfStoreID", wfStoreID, "itemLocator", itemLocator, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}

