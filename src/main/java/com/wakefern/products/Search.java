package com.wakefern.products;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class Search extends BaseService {
	
	private final static Logger logger = Logger.getLogger(Search.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Search() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.prodSearch;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.prodSearch)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.excluded) String excludedProds,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String[] searchFilters,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skipCount,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.sortOrder) String sortOrder,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchBySound) String searchBySound,
    		@DefaultValue("100") @QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String takeCount,  // MWG Docs claim this is optional, but will return an HTTP 400 if it's missing. 
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {	
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.productList, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			
			// The MWG endpoint will puke if the Search Term contains spaces
			// Replace any spaces with a "+" sign.
			if (searchTerm != null && !searchTerm.isEmpty()) {
				searchTerm = URLEncoder.encode(searchTerm, "UTF-8");//searchTerm.trim().replace(' ', '+');
			}
	
			StringBuilder sb = new StringBuilder();
			for(String searchFilter : searchFilters) {
				sb.append(URLEncoder.encode(searchFilter, "UTF-8"));
				sb.append("&fq=");
			}
			String searchFilterStr = sb.toString();
			
			// Build the Map of Query String parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.excluded, excludedProds);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, searchFilterStr.isEmpty() ? searchFilterStr : searchFilterStr.substring(0, searchFilterStr.length()-"&fq=".length()));
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, searchTerm);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skipCount);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sortOrder, sortOrder);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchBySound, searchBySound);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, takeCount);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.Search");
            
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("storeId", storeID, 
		        			"isMember", isMember, "excludedProds", excludedProds, "searchFilters", searchFilters, 
		        			"searchTerm", searchTerm, "skipCount", skipCount, "sortOrder", sortOrder,
		        			"searchBySound", searchBySound, "takeCount", takeCount, "userID", userID, 
		        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_SEARCH);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRevelantStackTrace(e), "storeId", storeID, 
        			"isMember", isMember, "excludedProds", excludedProds, "searchFilters", searchFilters, 
        			"searchTerm", searchTerm, "skipCount", skipCount, "sortOrder", sortOrder,
        			"searchBySound", searchBySound, "takeCount", takeCount, "userID", userID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
        }
    }
}

