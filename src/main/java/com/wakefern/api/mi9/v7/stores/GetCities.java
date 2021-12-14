package com.wakefern.api.mi9.v7.stores;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Stores.prefix)
public class GetCities extends BaseService {
	
	private final static Logger logger = LogManager.getLogger(GetCities.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetCities() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.prefix + MWGApplicationConstants.Requests.Stores.cities;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Stores.cities)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.regionID) String regionID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.services) String services,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
        
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.cities, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.regionID, regionID);
			
			// Build the Map of Request Query parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.services, services);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
			
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.stores.GetCities");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.SHOP_GET_USER_DASHBOARD);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "regionId", regionID, 
        		 "chainID", chainID, "services", services, "skip", skip, "take", take, 
        		 "sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
