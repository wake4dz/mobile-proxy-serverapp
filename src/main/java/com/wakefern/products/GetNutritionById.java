package com.wakefern.products;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.services.MI9TimeoutService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetNutritionById extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetNutritionById.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetNutritionById() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.nutritionByID;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.nutritionByID)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.productID) String productID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {	
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.productID, productID);

            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutService.PRODUCTS_GET_NUTRITION_BY_ID, MI9TimeoutService.getTimeout(MI9TimeoutService.PRODUCTS_GET_NUTRITION_BY_ID));
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_GET_NUTRITION_BY_ID);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID, 
        			"productID", productID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}

