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
public class CreateCustServFeedbackMsg extends BaseService {
	
	private final static Logger logger = Logger.getLogger(CreateCustServFeedbackMsg.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public CreateCustServFeedbackMsg() {
        this.requestPath = MWGApplicationConstants.Requests.Shop.prefix + MWGApplicationConstants.Requests.Shop.message;
    }
    
	@POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Shop.message)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
    		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.Shop.message, sessionToken);
			this.requestParams = new HashMap<String, String>();
		
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
		
   
            String jsonResponse = this.mwgRequest(BaseService.ReqType.POST, jsonData, "com.wakefern.shop.CreateCustServFeedbackMsg");
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.SHOP_CREATE_CUST_SERV_FEEDBACK_MSG);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRevelantStackTrace(e), 
        			"storeId", storeID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType, "httpBody", jsonData );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}


