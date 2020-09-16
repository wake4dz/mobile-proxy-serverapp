package com.wakefern.api.mi9.v7.stores;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Stores.prefix)
public class GetStores extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetStores.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetStores() {
        this.requestPath = MWGApplicationConstants.Requests.Stores.prefix + MWGApplicationConstants.Requests.Stores.stores;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Stores.stores)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.services) String services,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.city) String city,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.coords) String coords,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.toZip) String deliversToZip,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String filters,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.ipAddr) String ipAddr,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.zipCode) String zipCode,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String searchTerm,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.radius) String radius,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.regionCode) String isoRegionCode,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeOwnerID) String storeOwnerID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.storeStatus) String storeStatus,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.unitOfMeasure) String distUnitOfMeasure,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Stores.stores, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			this.queryParams   = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
			
			// The MWG endpoint will puke if the Search Term contains spaces
			// Replace any spaces with a "+" sign.
			if (searchTerm != null) {
				searchTerm = searchTerm.trim().replace(' ', '+');
			}
			
			if (city != null) {
				city = city.trim().replace(' ', '+');
			}
			
			// Build the Map of Request Query parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.services, services);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.city, city);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.coords, coords);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.toZip, deliversToZip);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters, filters);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.ipAddr, ipAddr);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.zipCode, zipCode);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, searchTerm);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.radius, radius);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.regionCode, isoRegionCode);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeOwnerID, storeOwnerID);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.storeStatus, storeStatus);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.unitOfMeasure, distUnitOfMeasure);
	
	       
            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.stores.GetStores");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.SHOP_GET_USER_DASHBOARD);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "servcies", services, 
        		 "skip", skip, "take", take, "city", city, "coords", coords, "deliversToZip", deliversToZip,
        		 "filters", filters, "ipAddr", ipAddr, "zipCode", zipCode, "searchTerm", searchTerm,
        		 "radius", radius, "isoRegionCode", isoRegionCode, "storeOwnerID", storeOwnerID,
        		 "storeStatus", storeStatus, "disUnitOfMeasure", distUnitOfMeasure, 
        		 "sessionToken", sessionToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
