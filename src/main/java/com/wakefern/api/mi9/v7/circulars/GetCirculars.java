package com.wakefern.api.mi9.v7.circulars;
// Not Currently Used. 
// See GetCircularsDetails.java instead.
//
//
//
//package com.wakefern.circulars;
//
//import com.wakefern.global.BaseService;
//import com.wakefern.mywebgrocer.models.MWGHeader;
//import com.wakefern.mywebgrocer.MWGApplicationConstants;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.Response;
//import java.io.IOException;
//import java.util.HashMap;
//
//@Path(MWGApplicationConstants.Requests.Circulars.prefix)
//public class GetCirculars extends BaseService {
//	
//	//-------------------------------------------------------------------------
//	// Public Methods
//	//-------------------------------------------------------------------------
//
//	/**
//	 * Constructor
//	 */
//    public GetCirculars() {
//        this.requestPath = MWGApplicationConstants.Requests.Circulars.prefix + MWGApplicationConstants.Requests.Circulars.circulars;
//    }
//    
//	@GET
//    @Consumes(MWGApplicationConstants.Headers.generic)
//    @Produces(MWGApplicationConstants.Headers.generic)
//    @Path(MWGApplicationConstants.Requests.Circulars.circulars)
//    public Response getResponse(
//    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
//    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,    		
//    		
//    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
//    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.runState) String runState,
//    		
//    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
//	) throws Exception, IOException {
//        		
//		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Circulars.circulars, MWGApplicationConstants.Headers.json, sessionToken);
//		this.requestParams = new HashMap<String, String>();
//		this.queryParams   = new HashMap<String, String>();
//		
//		// Build the Map of Request Path parameters
//		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
//		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
//		
//		// Map of the Query String parameters
//		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
//		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.runState, runState);
//		
//        try {
//            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.circulars.GetCirculars");
//            return this.createValidResponse(jsonResponse);
//        
//        } catch (Exception e) {
//            return this.createErrorResponse(e);
//        }
//    }
//}
//
