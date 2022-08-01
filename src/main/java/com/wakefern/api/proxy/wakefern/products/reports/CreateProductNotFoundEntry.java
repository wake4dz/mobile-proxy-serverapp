package com.wakefern.api.proxy.wakefern.products.reports;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Endpoint for creating new "Product Not Found" entries. Call out to another API server, since this database is not
 * directly accessibly by the Bluemix server.
 *
 * @author philmayer
 */

/*
 * 2022-08-01
 * TODO: This API is used in the V7 mobile app, but not sure if it is needed in the V8 mobile app.
 * 	     Should delete it after the confirmation. Sherry/BI team said it is up the mobile team.
 * 	     
 *       Sherry/BI said this is initially requested by Mark Covello
 */
@Path(ApplicationConstants.Requests.Reports.NotFoundProductURL)
public class CreateProductNotFoundEntry extends BaseService {

	private final static Logger logger = LogManager.getLogger(CreateProductNotFoundEntry.class);

	/**
	 * Create a new "Product Not Found" entry. Note that this endpoint requires a token supplied by the "GetToken"
	 * endpoint within this package.
	 *
	 * @return Response - contains
	 */
	@POST
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getResponse(
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Headers.Accept) String accept,
            @HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			String requestBody
	) {
		try {
			String productUrl = WakefernApplicationConstants.getBaseWakefernApiUrl() + WakefernApplicationConstants.Reports.NotFound.product;

			Map<String, String> headerMap = new HashMap<>(3);
			headerMap.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
			headerMap.put(ApplicationConstants.Requests.Headers.Accept, accept);
			headerMap.put(ApplicationConstants.Requests.Headers.contentType, contentType);

			HTTPRequest.executePost(productUrl, requestBody, headerMap);

			// The response body contains the string "Created" on success. Intentionally ignore this
			// value and return a valid response shape.
			return this.createValidResponse("");
		} catch (Exception exception) {
			String errorData = LogUtil.getRequestData("CreateProductNotFoundEntry::Exception", LogUtil.getRelevantStackTrace(exception));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(exception));
			return this.createErrorResponse(exception);
		}
	}
}
