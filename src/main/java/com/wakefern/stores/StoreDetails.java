package com.wakefern.stores;

import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Path(MWGApplicationConstants.Requests.Stores.storesPath)
public class StoreDetails extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public StoreDetails(){
        this.serviceType = new MWGHeader();
        this.path = MWGApplicationConstants.Requests.Stores.storesPath + MWGApplicationConstants.Requests.Stores.detailsPath;
    }
    
	@GET
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Stores.detailsPath)
    public Response getInfoResponse(
    		@PathParam(MWGApplicationConstants.chainID) String chainId, 
    		@PathParam(MWGApplicationConstants.storeID) String storeId,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) throws Exception, IOException {
        
        try {
            String jsonResponse = makeRequest(chainId, storeId, sessionToken);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
            return this.createErrorResponse(e);
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
    		this.token = sessionToken;
    	
        ServiceMappings mapping = new ServiceMappings();
        HashMap<String, String> reqParams = new HashMap<String, String>();
        
        reqParams.put(MWGApplicationConstants.chainID, chainId);
        reqParams.put(MWGApplicationConstants.storeID, storeId);
        
        mapping.setGetMapping(this, reqParams);
        
        String reqURL = mapping.getPath();
        Map<String, String> reqHead = mapping.getgenericHeader();
        
        return HTTPRequest.executeGetJSON(reqURL, reqHead, 0);
    }
}
