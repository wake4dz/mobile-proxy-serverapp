package com.wakefern.circulars;

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

@Path(MWGApplicationConstants.Requests.Circulars.prefix)
public class GetCircularsDetails extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetCircularsDetails.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetCircularsDetails() {
        this.requestPath = MWGApplicationConstants.Requests.Circulars.prefix + MWGApplicationConstants.Requests.Circulars.circulars;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Circulars.circulars)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,    		
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.runState) String runState,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {	
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.details, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			
			// Map of the Query String parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.runState, runState);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutService.CIRCULARS_GET_DETAILS, MI9TimeoutService.getTimeout(MI9TimeoutService.CIRCULARS_GET_DETAILS));
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.CIRCULARS_GET_CIRCULARS_DETAILS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"storeID", storeID, "chainID", chainID, "runState", runState, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}

