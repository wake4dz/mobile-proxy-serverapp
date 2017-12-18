package com.wakefern.stores;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

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
    		@QueryParam(MWGApplicationConstants.querySvcs) String services,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.chains, ApplicationConstants.jsonResponseType, sessionToken);
		this.queryParams = new HashMap<String, String>();
		this.queryParams.put(MWGApplicationConstants.querySvcs, services);

        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}
