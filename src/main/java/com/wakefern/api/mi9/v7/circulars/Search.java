package com.wakefern.api.mi9.v7.circulars;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Circulars.prefix)
public class Search extends BaseService {
	
	private final static Logger logger = Logger.getLogger(Search.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Search() {
        this.requestPath = MWGApplicationConstants.Requests.Circulars.prefix + MWGApplicationConstants.Requests.Circulars.search;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Circulars.search)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,    		
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String term,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        			
		// Search Term is REQUIRED.
		// If it's missing, we can save a trip to the MWG API & back by just immediately kicking an error back to the UI.
		if (term == null) {
			Exception e = new Exception("HTTP 400: No search term found in query string. Search Term is *required*.");
			return this.createErrorResponse(e);
		
		} else {
	        try {
	    		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.items, MWGApplicationConstants.Headers.json, sessionToken);
	    		this.requestParams = new HashMap<String, String>();
	    		this.queryParams   = new HashMap<String, String>();
	    		
	    		// Build the Map of Request Path parameters
	    		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
	    		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
	    		
	    		// Map of the Query String parameters
	    		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
	    		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
	    		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, term);
	    		
	            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.circulars.Search");
	            return this.createValidResponse(jsonResponse);
	        
	        } catch (Exception e) {
	        	LogUtil.addErrorMaps(e, MwgErrorType.CIRCULARS_SEARCH);
	        	
	        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
	        			"storeID", storeID, "chainID", chainID, "skip", skip, "take", take, 
	        			"term", term, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
	        	
	    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

	            return this.createErrorResponse(errorData, e);
	        }
		}
    }
}

