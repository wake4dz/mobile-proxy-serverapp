package com.wakefern.api.mi9.v7.checkout.fulfillments;

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
public class GetDeliveryTimes extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetDeliveryTimes.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetDeliveryTimes() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.deliveryTimes;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Checkout.deliveryTimes)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.mwgStoreID) String mwgStoreID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.zipCode) String zipCode,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken    		
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.fulfillTimes, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.mwgStoreID, mwgStoreID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.zipCode, zipCode);
			

            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.checkout.fulfillments.GetDeliveryTimes");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.FULFILLMENTS_GET_DELIVERY_TIMES);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "mwgStoreID", mwgStoreID, 
        			"zipCode", zipCode, "sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}

