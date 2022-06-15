package com.wakefern.api.wakefern.receipt;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.jwt.token.WakefernApiTokenManager;

/**
 * Retrieve a receipt detail info based on a receipt id from Wakefern's Digital
 * Receipt service
 * 
 * @author Danny Zheng
 */
@Path(WakefernApplicationConstants.Receipt.Proxy.Path)
public class GetDetail extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetDetail.class);

	/**
	 * Get receipt details. V2 includes user PPC verification using Bearer token
	 * 
	 * @param ppc
	 * @param receiptId
	 * @return response containing receipt details
	 */
	@GET
	@ValidatePPCWithJWT
	@Produces(WakefernApplicationConstants.Headers.Accept.v1)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("v2" + WakefernApplicationConstants.Receipt.Proxy.Detail)
	public Response getInfoResponseV2(@PathParam("ppc") String ppc, @PathParam("receiptId") String receiptId) {
		return this.getReceipt(ppc, receiptId);
	}

	/**
	 * Get receipt details. V2 above should be used instead to avoid fetching user
	 * profile on each request. This remains to support older mobile app versions.
	 * 
	 * @deprecated
	 * @param ppc
	 * @param receiptId
	 * @return response containing receipt details
	 */
	@GET
	@ValidatePPC
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path(WakefernApplicationConstants.Receipt.Proxy.Detail)
	public Response getInfoResponse(@PathParam("ppc") String ppc, @PathParam("receiptId") String receiptId) {
		return this.getReceipt(ppc, receiptId);
	}

	/**
	 * Get details about a receipt
	 * 
	 * @param ppc
	 * @param receiptId
	 * @return response containing receipt details
	 */
	private Response getReceipt(String ppc, String receiptId) {
		logger.info(
				"Thread " + Thread.currentThread().getName() + " starting request (ppc: " + ppc + ") for GetDetail");
		String jwt = null;
		try {
			jwt = WakefernApiTokenManager.getToken();
			// We are not going to a MWG endpoint with this request.
			// This is the Digital Receipt service provided and maintained by Wakefern.
			Map<String, String> wkfn = new HashMap<>();

			String path = WakefernApplicationConstants.Receipt.Upstream.BaseURL
					+ WakefernApplicationConstants.Receipt.Upstream.User + "/" + ppc + "/receipts/" + receiptId;

			wkfn.put(ApplicationConstants.Requests.Headers.contentType, "text/plain");
			wkfn.put(ApplicationConstants.Requests.Headers.jwtToken, jwt);

			String response = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());

			return this.createValidResponse(response);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.RECEIPT_GET_DETAIL);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc,
					"receiptId", receiptId, "jwtToken", jwt);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
