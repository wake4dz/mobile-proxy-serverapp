package com.wakefern.shop;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Shop.prefix)
public class GetEntryResourcesByChain extends BaseService {
	
	private final static Logger logger = Logger.getLogger(GetEntryResourcesByChain.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetEntryResourcesByChain() {
        this.requestPath = MWGApplicationConstants.Requests.Shop.prefix + MWGApplicationConstants.Requests.Shop.entryByChain;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Shop.entryByChain)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {
//			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Shop.links, MWGApplicationConstants.Headers.json, sessionToken);
//			this.requestParams = new HashMap<String, String>();
//		
//			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
//			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
//		
//			String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.shop.GetEntryResourcesByChain");
        	
        	/**
        	 * TODO: this is a temporary patch to the shop entry api call not allowing new registered user to checkout;
        	 *  unfortunately, shop entry by chain fix in v5 is no longer worked in v7, have to call shop entry by store in v7.
        	 *  once frontend's code is updated, revert this change to its original implementation
        	 *   (take out line below & uncomment code snippet above) 
        	 */
        		//assign Aberdeen's store ID - DA87780 as default store, since api is not taking in store ID
            logger.info("calling shop entry web: "+userID);
            String jsonResponse = new GetEntryResourcesByStore().getShopEntryFromSRWeb(userID, "DA87780", sessionToken);

            if(LogUtil.isUserTrackOn) {
              if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
                    String trackData = LogUtil.getRequestData("chainId", chainID, "userID", userID, 
                        "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
                logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
              }
            }
			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
	        	LogUtil.addErrorMaps(e, MwgErrorType.SHOP_GET_ENTRY_RESOURCES_BY_CHAIN);
	        	
	        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "chainId", chainID, 
	        			"userID", userID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
	        	
	    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}


