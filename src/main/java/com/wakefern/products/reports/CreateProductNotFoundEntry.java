package com.wakefern.products.reports;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Endpoint for creating new "Product Not Found" entries. Call out to another API server, since this database is not
 * directly accessibly by the Bluemix server.
 *
 * @author philmayer
 */
@Path(ApplicationConstants.Requests.Reports.NotFoundProductURL)
public class CreateProductNotFoundEntry extends BaseService {

	private final static Logger logger = Logger.getLogger(CreateProductNotFoundEntry.class);

	/**
	 * Create a new "Product Not Found" entry. Note that this endpoint requires a token supplied by the "GetToken"
	 * endpoint within this package.
	 *
	 * @return Response - contains
	 */
	@POST
	@Consumes(MWGApplicationConstants.Headers.json)
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getResponse(
			@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
			@HeaderParam(ApplicationConstants.Requests.Header.contentAccept) String accept,
            @HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
			String requestBody
	) {
		try {
			String productUrl = WakefernApplicationConstants.getBaseWakefernApiUrl() + WakefernApplicationConstants.Reports.NotFound.product;

			Map<String, String> headerMap = new HashMap<String, String>(3);
			headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
			headerMap.put(ApplicationConstants.Requests.Header.contentAccept, accept);
			headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);

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
