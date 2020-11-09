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
public class GetPageDetails extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetPageDetails.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetPageDetails() {
        this.requestPath = MWGApplicationConstants.Requests.Circulars.prefix + MWGApplicationConstants.Requests.Circulars.pageDetails;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Circulars.pageDetails)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,    		
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.circularID) String circularID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.pageID) String pageID,
    		    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        		
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.page, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.circularID, circularID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.pageID, pageID);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.circulars.GetPageDetails");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.CIRCULARS_GET_PAGE_DETAILS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"storeID", storeID, "chainID", chainID, "circularID", circularID, "pageID", pageID, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
