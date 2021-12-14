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
public class GetChains extends BaseService {
	
	private final static Logger logger = LogManager.getLogger(GetChains.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetChains() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.prefix + MWGApplicationConstants.Requests.Stores.chains;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Stores.chains)
    public Response getResponse(
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.services) String services,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
        
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.chains, MWGApplicationConstants.Headers.json, sessionToken);
			this.queryParams = new HashMap<String, String>();
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.services, services);

            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.stores.GetChains");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.SHOP_GET_USER_DASHBOARD);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "services", services
        			, "sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
