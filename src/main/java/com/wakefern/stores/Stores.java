package com.wakefern.stores;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Stores.storesPath)
public class Stores extends BaseService {
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public Stores() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.storesPath + MWGApplicationConstants.Requests.Stores.regions;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Stores.stores)
    @Produces("application/*")
    @Path(MWGApplicationConstants.Requests.Stores.regions)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.chainID) String chainID,
    		
    		@QueryParam(MWGApplicationConstants.queryServices) String services,
    		@QueryParam(MWGApplicationConstants.querySkip) String skip,
    		@QueryParam(MWGApplicationConstants.queryTake) String take,
    		@QueryParam(MWGApplicationConstants.queryCity) String city,
    		@QueryParam(MWGApplicationConstants.queryCoords) String coords,
    		@QueryParam(MWGApplicationConstants.queryToZip) String deliversToZip,
    		@QueryParam(MWGApplicationConstants.queryFilters) String filters,
    		@QueryParam(MWGApplicationConstants.queryIpAddr) String ipAddr,
    		@QueryParam(MWGApplicationConstants.queryZipCode) String zipCode,
    		@QueryParam(MWGApplicationConstants.querySearchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.queryRadius) String radius,
    		@QueryParam(MWGApplicationConstants.queryRegionCode) String isoRegionCode,
    		@QueryParam(MWGApplicationConstants.queryStoreOwnerID) String storeOwnerID,
    		@QueryParam(MWGApplicationConstants.queryStoreStatus) String storeStatus,
    		@QueryParam(MWGApplicationConstants.queryUnitOfMeasure) String distUnitOfMeasure,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	
	) throws Exception, IOException {
        
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.stores, ApplicationConstants.jsonResponseType, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.chainID, chainID);
		
		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.queryServices, services);
		this.queryParams.put(MWGApplicationConstants.querySkip, skip);
		this.queryParams.put(MWGApplicationConstants.queryTake, take);
		this.queryParams.put(MWGApplicationConstants.queryCity, city);
		this.queryParams.put(MWGApplicationConstants.queryCoords, coords);
		this.queryParams.put(MWGApplicationConstants.queryToZip, deliversToZip);
		this.queryParams.put(MWGApplicationConstants.queryFilters, filters);
		this.queryParams.put(MWGApplicationConstants.queryIpAddr, ipAddr);
		this.queryParams.put(MWGApplicationConstants.queryZipCode, zipCode);
		this.queryParams.put(MWGApplicationConstants.querySearchTerm, searchTerm);
		this.queryParams.put(MWGApplicationConstants.queryRadius, radius);
		this.queryParams.put(MWGApplicationConstants.queryRegionCode, isoRegionCode);
		this.queryParams.put(MWGApplicationConstants.queryStoreOwnerID, storeOwnerID);
		this.queryParams.put(MWGApplicationConstants.queryStoreStatus, storeStatus);
		this.queryParams.put(MWGApplicationConstants.queryUnitOfMeasure, distUnitOfMeasure);

        try {
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null);
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }
}
