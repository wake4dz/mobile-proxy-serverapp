package com.wakefern.stores;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.Map;

@Path(MWGApplicationConstants.Requests.Stores.storesPath)
public class Chains extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Chains() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.storesPath + MWGApplicationConstants.Requests.Stores.listChainsPath;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Stores.chains)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Stores.listChainsPath)
    public Response getChains(
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) throws Exception, IOException {
        
        try {
            String jsonResponse = makeRequest(sessionToken);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------
    
	/**
	 * Make the Request to MyWebGrocer
	 * 
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
    private String makeRequest(String sessionToken) throws Exception, IOException {
    		this.token = sessionToken;
    		this.requestHeader = new MWGHeader(ApplicationConstants.jsonAcceptType, MWGApplicationConstants.Headers.Stores.chains, sessionToken);
    	
        ServiceMappings mapping = new ServiceMappings();
                
        mapping.setGetMapping(this, null);
        
        String reqURL = mapping.getPath();
        Map<String, String> reqHead = mapping.getgenericHeader();
        
        return HTTPRequest.executeGetJSON(reqURL, reqHead, 0);
    }
}
