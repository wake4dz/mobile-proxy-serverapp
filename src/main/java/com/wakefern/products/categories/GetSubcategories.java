package com.wakefern.products.categories;

import com.wakefern.global.*;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetSubcategories extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetSubcategories.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetSubcategories() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.subCategories;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.subCategories)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.parentCatID) String parentCategoryID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.prodsPerCat) String productsPerCategory,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.saleOnlyProds) String onlySaleProducts,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType
	) {
		
		long startTime, endTime, actualTime;
		
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
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.saleOnlyProds, onlySaleProducts);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);

			// trackData is relocated here, so it can be used for both warnTime feature and trackerUserId.
        	String trackData = LogUtil.getRequestData("storeId", storeID, 
        			"parentCategoryID", parentCategoryID, "isMember", isMember,
        			"productsPerCategory", productsPerCategory, "onlySaleProducts", onlySaleProducts, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
			startTime = System.currentTimeMillis();
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.categories.GetSubcategories");
            
            // for warning any API call time exceeding its own upper limited time defined in mwgApiWarnTime.java
			endTime = System.currentTimeMillis();
			actualTime = endTime - startTime;
			if (actualTime > MwgApiWarnTime.CATEGORIES_GET_SUB_CATEGORIES.getWarnTime()) {
				logger.warn("The API call took " + actualTime + " ms to process the request, the warn time is " +
						MwgApiWarnTime.CATEGORIES_GET_SUB_CATEGORIES.getWarnTime() + " ms. The track data: " + trackData);
			}
			
			
			// for trackUserId feature
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
		        	// Note: there will be an extra blank line in log output because com.wakefern.global.errorHandling.ResponseHandler.java line #34 add /r
				}			
			}
						
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	
        	LogUtil.addErrorMaps(e, MwgErrorType.CATEGORIES_GET_SUB_CATEGORIES);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId", storeID, 
        			"parentCategoryID", parentCategoryID, "isMember", isMember,
        			"productsPerCategory", productsPerCategory, "onlySaleProducts", onlySaleProducts, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType  );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}