package com.wakefern.products.categories;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.services.MI9TimeoutService;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetCirProductCategoriesWklySpecials extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetCirProductCategoriesWklySpecials.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetCirProductCategoriesWklySpecials() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.circCategoriesWeeklySpecials;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.circCategoriesWeeklySpecials)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
		
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.categoriesPlugin, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutService.CIRCULAR_GET_PRODUCT_CATEGORY_WKLY_SPECIALS, MI9TimeoutService.getTimeout(MI9TimeoutService.CIRCULAR_GET_PRODUCT_CATEGORY_WKLY_SPECIALS));
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
	        	LogUtil.addErrorMaps(e, MwgErrorType.CATEGORIES_GET_CIRC_PROD_CATS_WKLY_SPECIALS);
	        	
	        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID,"accept", accept, "contentType", contentType );
	
	    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}
