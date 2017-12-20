package com.wakefern.products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.productPath)
public class BySku extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public BySku() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prodsByCat + MWGApplicationConstants.Requests.Products.prodBySKU;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.bySKU)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Products.prodBySKU)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.pathProductSKU) String productSKU,
    		@PathParam(MWGApplicationConstants.pathStoreID) String storeID,
    		@QueryParam(MWGApplicationConstants.queryIsMember) String isMember,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
        try {
            String jsonResponse = makeRequest(storeID, productSKU, isMember, sessionToken);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
	
	/**
	 * For Internal Use Only.<br>
	 * Not an API endpoint.<br>
	 * Get Product info by SKU.
	 * 
	 * @param storeID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	public String getInfo(String storeID, String productSKU, String isMember, String sessionToken) throws Exception {
		return makeRequest(storeID, productSKU, isMember, sessionToken);
	}
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	/**
	 * Trigger the request to the MWG API.
	 * 
	 * @param storeID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	private String makeRequest(String storeID, String productSKU, String isMember, String sessionToken) throws Exception {		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.bySKU, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.pathStoreID, storeID);
		this.requestParams.put(MWGApplicationConstants.pathProductSKU, productSKU);
		
		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.queryIsMember, isMember);
		
		return this.mwgRequest(BaseService.ReqType.GET, null);
	}
}

