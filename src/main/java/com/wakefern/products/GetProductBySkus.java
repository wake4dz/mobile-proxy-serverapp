package com.wakefern.products;

import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetProductBySkus extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetProductBySkus() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.prodsBySKUs;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.accept)
    @Produces(MWGApplicationConstants.Headers.Products.accept)
    @Path(MWGApplicationConstants.Requests.Products.prodsBySKUs)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Path.productSKU) ArrayList<String> skus,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        		
        try {
            String jsonResponse = makeRequest(mwgStoreID, skus, sessionToken);
//            
//            // Item Location data provided by MWG is never up-to-date.
//            // Used Wakefern-supplied Item Location data instead.
//            jsonResponse = this.getItemLocations(jsonResponse, wfStoreID);
            
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
	 * @param mwgStoreID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	public String getInfo(String mwgStoreID, ArrayList<String> productSKUs, String sessionToken) throws Exception {
		return makeRequest(mwgStoreID, productSKUs, sessionToken);
	}
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	/**
	 * Trigger the request to the MWG API.
	 * 
	 * @param mwgStoreID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	private String makeRequest(String mwgStoreID, ArrayList<String> productSKUs, String sessionToken) throws Exception {
		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.accept, MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
		
		StringBuilder sb = new StringBuilder();
		String skuStr = "";
		for(String prodSKUsStr : productSKUs) {
			sb.append(prodSKUsStr);
			sb.append("&sku=");
		}
		skuStr = sb.toString();

		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sku, skuStr.substring(0, skuStr.length()-"&sku=".length()));
		
		return this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.GetProductsBySKUs");
	}
}