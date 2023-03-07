package com.wakefern.api.proxy.wakefern.services;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import com.wakefern.global.ApplicationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;

import java.util.HashMap;
import java.util.Map;

/**
 * This api will make request to several services currently being used by ShopRite app.
 * The purpose of this api is to verify the integrity of the token in VCAP against the services.
 * This new api is created to accommodate the migration of hardcoded token to VCAP environment variable key/value pair.
 * The impact services will be
 * 1) Coupon
 * 2) Item Locator
 * 3) Product Recommendation
 *
 *
 * @author sfl1c
 */

/*
 * 2022-08-01
 * TODO: After selecting a new host platform for the Proxy server, we probably need to refactor/enhance this verification checking
 */

@Path(ApplicationConstants.Requests.VerifyServices)
public class GetServicesStatus extends BaseService {
	private final static Logger logger = LogManager.getLogger(GetServicesStatus.class);

	/**
	 * Custom MIME type for this endpoint.
	 */
	private static final String MIME_TYPE = "application/wakefern-services";

	/**
	 * Request body argument keys
	 */
	private static final String PPC_KEY = "ppc";
	private static final String STORE_ID_KEY = "storeId";

	@POST
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
	public Response getInfoResponse(@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
									@HeaderParam(ApplicationConstants.Requests.Headers.Accept) String accept,
									String jsonBody) {
		// Check for valid content type header.
		if (contentType == null || !contentType.equalsIgnoreCase((MIME_TYPE))) {
			final JSONObject errorJsonObj = new JSONObject("{\"error\":\"wrong header type\"}");
			return Response.status(Response.Status.BAD_REQUEST).entity(errorJsonObj).build();
		}

		StringBuilder sb = new StringBuilder();
		JSONObject jsonData = new JSONObject(jsonBody);

		final String ppc = jsonData.optString(PPC_KEY, null);
		final String externalStoreId = jsonData.optString(STORE_ID_KEY, null);

		// TODO: replace this with JSONArray/some kind of DAO
		sb.append("[");

		//check VCAP names not empty or null
		sb.append(checkVCAPNames());

		// verify Item Locator service..
		sb.append(printServiceName("ITEM LOCATOR / DIGITAL RECEIPT", getItemLocatorStatus()));
		sb.append(",");

		// verify product recommendation service..
		sb.append(printServiceName("PRODUCT RECOMMENDATION", getProdRecommendationStatus(externalStoreId, ppc)));
		sb.append("]");

		return this.createValidResponse(sb.toString());
	}

	private static String printServiceName(String serviceName, String status) {
		return "{\"service name\":\"" + serviceName + "\",\"service status\":\"" + status + "\"}";
	}

	/**
	 * verify Item Locator service..
	 *
	 * @return
	 */
	private String getItemLocatorStatus() {
		String serviceStatus;
		try {
			WakefernAuth.getInfo(ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
			serviceStatus = "active";
		} catch (Exception e) {
			logger.error("[GetServicesStatus]:: Item Locator exception resp: " + e.getMessage());
			serviceStatus = "inactive";
		}
		return serviceStatus;
	}

	/**
	 * verify product recommendation service..
	 *
	 * @param externalStoreId
	 * @param ppc
	 * @return
	 */
	private String getProdRecommendationStatus(String externalStoreId, String ppc) {
		String serviceStatus;
		try {
			final String authToken = ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.SR_PRODUCT_RECOMMENDATION_KEY);

			final String path = WakefernApplicationConstants.Recommendations.BaseRecommendationsURL
					+ WakefernApplicationConstants.Recommendations.ProductRecommendationsv2 + "/" + externalStoreId + "/email//fsn/" + ppc;

			Map<String, String> headers = new HashMap<>();
			headers.put(ApplicationConstants.Requests.Headers.Accept, ApplicationConstants.Requests.Headers.MIMETypes.json);
			headers.put(ApplicationConstants.Requests.Headers.Authorization, authToken);

			// CALL & GET LIST OF RECOMMENDED SKUs
			String prodRecmdResp = HTTPRequest.executeGet(path, headers, 0);
			logger.info("[GetServicesStatus]:: Product Recommendation resp: " + prodRecmdResp);
			serviceStatus = "active";
		} catch (Exception ex) {
			logger.error("[GetServicesStatus]:: Product Recommendation Exception: " + ex.getMessage());
			serviceStatus = "inactive";
		}
		return serviceStatus;
	}

	/**
	 * Checking VCAP names not empty or null
	 *
	 * @return
	 */
	private static String checkVCAPNames() {
		StringBuilder sb = new StringBuilder();
		verifyVCAP(WakefernApplicationConstants.VCAPKeys.COUPON_V3_KEY, sb);
		verifyVCAP(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY, sb);
		verifyVCAP(WakefernApplicationConstants.VCAPKeys.PROD_NOT_FOUND_LOGIN, sb);
		verifyVCAP(WakefernApplicationConstants.VCAPKeys.SR_PRODUCT_RECOMMENDATION_KEY, sb);
		return !sb.toString().isEmpty()
				? "{\"VCAP names\":\"" + sb + "\",\"description\":\"ATTENTION!!! Please check Bluemix VCAP, the listed VCAP name(s) are empty or null.\"},"
				: "";
	}

	private static void verifyVCAP(String vcapName, StringBuilder sb) {
		if (isNullEmptyString(ApplicationUtils.getVcapValue(vcapName))) {
			sb.append(vcapName);
			sb.append(" / ");
		}
	}

	private static boolean isNullEmptyString(String vcapName) {
		return vcapName == null || vcapName.isEmpty();
	}
}
