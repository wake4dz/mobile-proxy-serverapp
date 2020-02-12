package com.wakefern.recipes;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Recipes.prefix)
public class SearchByCategory extends BaseService {
	
	private final static Logger logger = Logger.getLogger(CreateEmailRequest.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public SearchByCategory() {
        this.requestPath = MWGApplicationConstants.Requests.Recipes.prefix + MWGApplicationConstants.Requests.Recipes.searchByCat;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Recipes.searchByCat)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.categoryID) String catID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String term,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {		
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Recipes.recipes, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.categoryID, catID);
			
			// Build the Map of Query String parameters.
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeID, storeID);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, term);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.recipes.SearchByCategory");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	//TODO 
        	// As of 2018-10-17, the Recipe top-menu would generate a lot of 404s from MWG because not every category has at least a recipe
        	// Either UI needs to code around it or switch to a new third-party recipe supplier
        	// See Danny's email to Mark Covello on 2018-10-16
        	// For now, disable this API for addErrorMaps until it get fixed
        	
        	//LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_SEARCH_BY_CATEGORY);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainId", chainID, 
        			"storeID", storeID, "categoryID", catID, 
        			"skip", skip, "take", take, "term", term, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}

