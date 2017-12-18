package com.wakefern.stores;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path(MWGApplicationConstants.Requests.Stores.stores)
public class Chains extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Chains() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.stores + MWGApplicationConstants.Requests.Stores.chains;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Stores.chains)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Stores.chains)
    public Response getChains(
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) throws Exception, IOException {
        
        try {
            String jsonResponse = makeRequest(sessionToken);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
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
    		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.chains, ApplicationConstants.jsonResponseType, sessionToken);
        return this.mwgRequest(BaseService.ReqType.GET, null);
    }
}
