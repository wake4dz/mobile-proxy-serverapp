package com.wakefern.api.wakefern.receiptUserSettings;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;

/**
 * Endpoint to fetch the user's "Digital Receipts" settings.
 */
@Path(WakefernApplicationConstants.ReceiptUserSettings.Proxy.Path)
public class GetReceiptSettings extends BaseService {
	private final static Logger logger = Logger.getLogger(GetReceiptSettings.class);

	@GET
	@ValidatePPC
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	public Response getReceiptSettings(@PathParam("userId") String userId, @PathParam("ppc") String ppc) {
		try {
			final String apiKey = MWGApplicationConstants.getProductRecmdAuthToken();
			HashMap<String, String> headers = new HashMap<>();

			String path = WakefernApplicationConstants.ReceiptUserSettings.Upstream.BaseURL
					+ WakefernApplicationConstants.ReceiptUserSettings.Upstream.FetchPath + ppc;

			headers.put(ApplicationConstants.Requests.Header.contentType, MediaType.APPLICATION_JSON);
			headers.put(ApplicationConstants.Requests.Header.contentAuthorization, apiKey);

			String response = HTTPRequest.executeGet(path, headers, VcapProcessor.getApiLowTimeout());
			return this.createValidResponse(response);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.RECEIPT_GET_USER_SETTINGS);
			String errorData = LogUtil.getRequestData("exceptionLocation",
					LogUtil.getRelevantStackTrace(e), "ppc", ppc, "userId", userId);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
			return this.createErrorResponse(errorData, e);
		}
	}
}



