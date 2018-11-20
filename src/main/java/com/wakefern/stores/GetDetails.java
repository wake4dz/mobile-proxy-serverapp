package com.wakefern.stores;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Stores.prefix)
public class GetDetails extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetDetails.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetDetails(){
        this.requestPath = MWGApplicationConstants.Requests.Stores.prefix + MWGApplicationConstants.Requests.Stores.details;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Stores.details)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainId, 
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeId,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
        
        try {
            String jsonResponse = makeRequest(chainId, storeId, sessionToken);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.SHOP_GET_USER_DASHBOARD);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainID", chainId, 
        		 "storeID", storeId, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

	/**
	 * Not an API endpoint.<br>
	 * Used internally to get details of a particular store.
	 * 
	 * @param chainId
	 * @param storeId
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
    public String getInfo(String chainId, String storeId, String sessionToken) throws Exception, IOException {
    		return makeRequest(chainId, storeId, sessionToken);
    }

	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------
    
    private String makeRequest(String chainId, String storeId, String sessionToken) throws Exception, IOException {
    		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.details, MWGApplicationConstants.Headers.json, sessionToken);
    		this.requestParams = new HashMap<String, String>();
    	
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainId);
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeId);
		
        return this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.stores.GetDetails");
    }
}
