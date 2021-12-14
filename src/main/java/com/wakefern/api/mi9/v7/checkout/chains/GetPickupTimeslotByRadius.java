package com.wakefern.api.mi9.v7.checkout.chains;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

import org.json.JSONObject;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetPickupTimeslotByRadius extends BaseService {
	
	private final static Logger logger = LogManager.getLogger(GetDeliveryTimeslotByRadius.class);
	
	private static final String TAG = GetPickupTimeslotByRadius.class.getName();
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetPickupTimeslotByRadius() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.pickupTimeslotByRadius;
    }
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.pickupTimeslotByRadius)
	public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken   		
	) {
		JSONObject jsonData = new JSONObject();
		
        try {	

        	this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.timeslotByRadiusAccept, 
				MWGApplicationConstants.Headers.Checkout.timeslotByRadiusContentType, sessionToken);
        	
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			
			jsonData.put("SearchRadius", VcapProcessor.getTimeslotSearchRadiusInMile());
			logger.trace("jsonData: " + jsonData);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonData.toString(), TAG);

            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.CHAINS_GET_PICKUP_TIMESLOT_BY_RADIUS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID, 
        			"chainID", chainID, "sessionToken", sessionToken, 
        			"accept", MWGApplicationConstants.Headers.Checkout.timeslotByRadiusAccept, 
        			"contentType", MWGApplicationConstants.Headers.Checkout.timeslotByRadiusContentType, 
        			"httpBody", jsonData );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
