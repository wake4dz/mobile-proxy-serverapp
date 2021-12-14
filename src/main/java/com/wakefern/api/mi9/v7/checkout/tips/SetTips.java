package com.wakefern.api.mi9.v7.checkout.tips;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Checkout.prefix)
public class SetTips extends BaseService {

	private final static Logger logger = LogManager.getLogger(SetTips.class);

	private static final String TAG = SetTips.class.getName();

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public SetTips() {
		this.requestPath = MWGApplicationConstants.Requests.Checkout.prefix
				+ MWGApplicationConstants.Requests.Checkout.setTips;
	}

	@POST
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Checkout.setTips)
	public Response getResponse(
			@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userId,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeId,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			String jsonBody) {
		
		try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.generic, MWGApplicationConstants.Headers.Checkout.deliveryDataAccept, sessionToken);
			this.requestParams = new HashMap<>();

			// Path params
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userId);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeId);

			String jsonResponse = this.mwgRequest(ReqType.POST, jsonBody, TAG);
			return this.createValidResponse(jsonResponse);
			
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.SET_TIPS);

			String errorData = LogUtil.getRequestData("exceptionLocation",
					LogUtil.getRelevantStackTrace(e),
					"storeId", storeId,
					"userId", userId,
					"sessionToken", sessionToken);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}
}
