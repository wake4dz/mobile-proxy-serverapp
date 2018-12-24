package com.wakefern.checkout.orders;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;


@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class CreateOrders extends BaseService {
	
	private int timeout = 40000;
	private final static Logger logger = Logger.getLogger(CreateOrders.class);
	
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
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonString
	) {
		long startTime, endTime, actualTime;
		startTime = System.currentTimeMillis();
		
		try {	
		    
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Checkout.orders, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
       
			this.setTimeout(timeout);
			String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonString, "package com.wakefern.checkout.orders.CreateOrders");
			endTime = System.currentTimeMillis();
      
			String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonString);
			
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.ORDERS_CREATE_ORDERS.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is " +
						MwgApiWarnTime.ORDERS_CREATE_ORDERS.getWarnTime() + " ms. The track data: " + trackData);
			}
			
			// On 9/24/2018, per Loi's request, we log the log below for every order in addition to the userTrackOn feature
    			logger.info("[CreateOrders::getResponse]::processing time-"+(endTime - startTime)+" - "+mwgStoreID+" - "+jsonResponse);
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
	        	endTime = System.currentTimeMillis();
	        	
	        	LogUtil.addErrorMaps(e, MwgErrorType.ORDERS_CREATE_ORDERS);
	        	
	        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
	        			"mwgStoreID", mwgStoreID, "processTime", (endTime - startTime),  "userID", userID, "sessionToken", sessionToken, "httpBody", jsonString);
	        	
	     
	    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e) );

            return this.createErrorResponse(errorData, e);
        }
    }
}


