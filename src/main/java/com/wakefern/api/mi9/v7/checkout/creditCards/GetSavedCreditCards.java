package com.wakefern.api.mi9.v7.checkout.creditCards;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

/*
 * Purpose: Get a list of saved credit cards 
 * JIRA:    https://wakefern.atlassian.net/browse/DMAU-1580
 */
@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class GetSavedCreditCards extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetSavedCreditCards.class);
	private static final String TAG = GetSavedCreditCards.class.getName();
	

	/**
	 * Constructor
	 */
    public GetSavedCreditCards() {
        this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix + MWGApplicationConstants.Requests.Checkout.getCreditCards;
    }
 
    
	@GET
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	@Path(MWGApplicationConstants.Requests.Checkout.getCreditCards)
	public Response getCreditCards(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID,
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken) {
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Checkout.creditCardsAccept, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<String, String>();
			
			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);

			String response = this.mwgRequest(BaseService.ReqType.GET, null, TAG);
			
			 return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.CREDIT_CARDS_GET_SAVED_CREDIT_CARDS);
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
					"sessionToken", sessionToken, "accept", MWGApplicationConstants.Headers.Checkout.creditCardsAccept,
					"userId", userID, "storeID", storeID);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(e);
		}
	}

}
