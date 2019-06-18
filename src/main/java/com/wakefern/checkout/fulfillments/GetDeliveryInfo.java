package com.wakefern.checkout.fulfillments;

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
public class GetDeliveryInfo extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetDeliveryInfo.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetDeliveryInfo() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.deliveryInfo;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.deliveryInfo)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.zipCode) String zipCode,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
        try {	
        	/**
        	 * This is to solve the discrepancy between front & backend, frontend sent accept header application/vnd.mywebgrocer.fulfillment-options+json
        	 * and backend hardcoded the accept header to be: application/vnd.mywebgrocer.fulfillment-options+json-v2.
        	 * The resolution is to support district (DMAU-319), we'll add 'v1' (application/vnd.mywebgrocer.fulfillment-options-v1+json) 
        	 * to req header & BE will map it to correct value (ie. application/vnd.mywebgrocer.fulfillment-options+json)
        	 */
        	if(accept.contains("v1")){ // has resp json value: "Name": "Hidden Brook at Franklin Residents",
        		accept = MWGApplicationConstants.Headers.Checkout.fulfillOpts;
        	} else{ // has resp json value:  "Name": "dates.available", [for ver <= 3.8.0]
        		accept = MWGApplicationConstants.Headers.Checkout.fulfillOptsV2;
        	}
			this.requestHeader = new MWGHeader(accept, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.zipCode, zipCode);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.checkout.fulfillments.GetDeliveryInfo");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.FULFILLMENTS_GET_DELIVERY_INFO);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "mwgStoreID", mwgStoreID, 
        			"zipCode", zipCode, "sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}


