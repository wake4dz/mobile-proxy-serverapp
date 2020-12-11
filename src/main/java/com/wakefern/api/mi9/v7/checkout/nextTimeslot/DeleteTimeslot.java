package com.wakefern.api.mi9.v7.checkout.nextTimeslot;

import java.util.HashMap;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

/*
 * Next Available Timeslot feature
 * https://wakefern.atlassian.net/browse/DMAU-1442
 * https://mi9retail.atlassian.net/browse/WFD-27137
 */
@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class DeleteTimeslot extends BaseService {
	
	private final static Logger logger = Logger.getLogger(DeleteTimeslot.class);
	private static final String TAG = DeleteTimeslot.class.getName();
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public DeleteTimeslot() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.removeNextTimeslot;
    }
    
	@DELETE
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.removeNextTimeslot)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.timeslotID) String timeslotID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.fulfillType) String fulfillmentType,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.nextTimeslotAccept,
					MWGApplicationConstants.Headers.Checkout.nextTimeslotContentType, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.timeslotID, timeslotID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.fulfillType, fulfillmentType);

            String jsonResponse = this.mwgRequest(BaseService.ReqType.DELETE, null, TAG);
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("mwgStoreID", mwgStoreID, "userID", userID, 
		        			"timeslotID", timeslotID, "fulfillmentType", fulfillmentType,
		        			"sessionToken", sessionToken, "accept", MWGApplicationConstants.Headers.Checkout.nextTimeslotAccept, 
		        			"contentType", MWGApplicationConstants.Headers.Checkout.nextTimeslotContentType );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.NEXT_TIMESLOT_DELETE_TIMESLOT);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"mwgStoreID", mwgStoreID, "userID", userID, "timeslotID", timeslotID, "fulfillmentType", fulfillmentType, 
        			"sessionToken", sessionToken, "accept", MWGApplicationConstants.Headers.Checkout.nextTimeslotAccept, 
        			"contentType", MWGApplicationConstants.Headers.Checkout.nextTimeslotContentType );

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}