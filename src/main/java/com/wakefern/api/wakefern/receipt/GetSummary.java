package com.wakefern.api.wakefern.receipt;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.global.annotations.ValidatePPCWithJWT;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.wynshop.WynshopApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.jwt.token.WakefernApiTokenManager;

/**
 * Retrieve a receipt summary list from Wakefern's Digital Receipt service
 * 
 * @author Danny Zheng
 */
@Path(WakefernApplicationConstants.Receipt.Proxy.Path)
public class GetSummary extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetSummary.class);

	/**
	 * Get a user's receipts. V2 includes user PPC verification using Bearer token
	 * 
	 * @param ppc
	 * @param startDate
	 * @param endDate
	 * @return response containing user receipts or an error if token could not be
	 *         verified
	 */
	@GET
	@ValidatePPCWithJWT
	@Produces(WakefernApplicationConstants.Headers.Accept.v1)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/v2")
	public Response getInfoResponseV2(@PathParam("ppc") String ppc,
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String token,
			@QueryParam(WynshopApplicationConstants.Requests.Params.Query.startDate) String startDate,
			@QueryParam(WynshopApplicationConstants.Requests.Params.Query.endDate) String endDate) {
		return this.getReceipts(ppc, startDate, endDate);
	}

	/**
	 * Get a user's receipts. V2 above should be used instead to avoid fetching user
	 * profile on each request. This remains to support older mobile app versions.
	 * 
	 * @deprecated
	 * @param ppc
	 * @param startDate
	 * @param endDate
	 * @return response containing user receipts
	 */
	@GET
	@ValidatePPC
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/")
	public Response getInfoResponse(@PathParam("ppc") String ppc,
			@QueryParam(WynshopApplicationConstants.Requests.Params.Query.startDate) String startDate,
			@QueryParam(WynshopApplicationConstants.Requests.Params.Query.endDate) String endDate) {
		return this.getReceipts(ppc, startDate, endDate);
	}

	/**
	 * Get a user's receipts from Wakefern API
	 * 
	 * @param ppc
	 * @param startDate
	 * @param endDate
	 * @return response containing array of receipts
	 */
	private Response getReceipts(String ppc, String startDate, String endDate) {
		String jwt = null;

		try {
			logger.info("Thread " + Thread.currentThread().getName() + " starting request (ppc: " + ppc
					+ ") for GetSummary");

			jwt = WakefernApiTokenManager.getToken();

			// This is the Digital Receipt service provided and maintained by Wakefern.
			Map<String, String> wkfn = new HashMap<>();

			String path = WakefernApplicationConstants.Receipt.Upstream.BaseURL
					+ WakefernApplicationConstants.Receipt.Upstream.User + "/" + ppc + "/receipts?startdate="
					+ startDate.trim() + "&enddate=" + endDate.trim();

			wkfn.put(ApplicationConstants.Requests.Headers.contentType, "text/plain");
			wkfn.put(ApplicationConstants.Requests.Headers.jwtToken, jwt);

			String response = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());

			return this.createValidResponse(response);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.RECEIPT_GET_SUMMARY);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc,
					"startDate", startDate, "endDate", endDate, "jwtToken", jwt);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}