package com.wakefern.api.mi9.v7.products.categories;

import com.wakefern.global.*;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetSubcatsWithSaleItems extends BaseService {
	
	private final static Logger logger = LogManager.getLogger(GetSubcatsWithSaleItems.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetSubcatsWithSaleItems() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.subCatsWithSales;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.subCatsWithSales)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.parentCatID) String parentCategoryID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.prodsPerCat) String productsPerCategory,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.categories, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.parentCatID, parentCategoryID);
	
			// Build the Map of Request Query parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.prodsPerCat, productsPerCategory);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.categories.GetSubcatsWithSaleItems");
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("storeId", storeID, 
		        			"parentCategoryID", parentCategoryID, "isMember", isMember,
		        			"productsPerCategory", productsPerCategory, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	
        	LogUtil.addErrorMaps(e, MwgErrorType.CATEGORIES_GET_SUB_CATS_WITH_SALE_ITEMS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID, 
        			"parentCategoryID", parentCategoryID, "isMember", isMember,
        			"productsPerCategory", productsPerCategory, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}