package com.wakefern.recipes;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Recipes.prefix)
public class GetFeatured extends BaseService {
	
	private final static Logger logger = Logger.getLogger(CreateEmailRequest.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetFeatured() {
        this.requestPath = MWGApplicationConstants.Requests.Recipes.prefix + MWGApplicationConstants.Requests.Recipes.featured;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Recipes.featured)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.promotion) String promo,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.recipeGroup) String group,
    		
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
			
			// Build the Map of Query String parameters.
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeID, storeID);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.promotion, promo);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.recipeGroup, group);
			    
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.recipes.GetFeatured");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_GET_FEATURED);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRevelantStackTrace(e), "chainId", chainID, 
        			"storeID", storeID, "skip", skip, "take", take, "promo", promo, "group", group, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}

