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
public class GetPrepTimes extends BaseService {
	
	private final static Logger logger = Logger.getLogger(CreateEmailRequest.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetPrepTimes() {
        this.requestPath = MWGApplicationConstants.Requests.Recipes.prefix + MWGApplicationConstants.Requests.Recipes.prepTimes;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Recipes.prepTimes)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.recipeID) String recipeID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeID) String storeID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {		
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Recipes.prepTimes, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.recipeID, recipeID);
			
			// Build the Map of Query String parameters.
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeID, storeID);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.recipes.GetPrepTimes");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_GET_PREP_TIMES);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainId", chainID, 
        			"storeID", storeID, "recipeID", recipeID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}

