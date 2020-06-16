package com.wakefern.rewards;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.annotations.ValidatePPCWithJWT;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(MWGApplicationConstants.Requests.Rewards.Points)
public class GetPointsForPPC extends BaseService {

	private final static Logger logger = Logger.getLogger(GetPointsForPPC.class);

	/**
	 * Get a user's PPC points. V2 includes user PPC verification using Bearer token
	 * 
	 * @param ppc
	 * @return
	 */
	@GET
	@ValidatePPCWithJWT
	@Produces(WakefernApplicationConstants.Headers.Accept.v1)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path("/{ppc}")
	public Response getInfoResponseV2(@PathParam("ppc") String ppc) {
		return this.getPoints(ppc);
	}

	/**
	 * Get a user's PPC points. V2 above should be used instead for security. This
	 * remains to support older mobile app versions.
	 * 
	 * @deprecated
	 * @param ppc
	 * @return points response
	 */
	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path("/{ppc}")
	public Response getInfoResponse(@PathParam("ppc") String ppc) {
		return this.getPoints(ppc);
	}

	/**
	 * Make request for PPC points
	 * 
	 * @param ppc
	 * @return points response
	 */
	private Response getPoints(String ppc) {
		try {
			// We are not going to a MWG endpoint with this request.
			// This is a service provided and maintained by Wakefern.
			// So it requires a different Authorization Header Token than the one provided
			// by the UI.
			this.requestToken = MWGApplicationConstants.getProductRecmdAuthToken();
			this.requestPath = MWGApplicationConstants.Requests.Rewards.Points + "/" + ppc;

			ServiceMappings srvMap = new ServiceMappings();

			srvMap.setMappingWithURL(this, MWGApplicationConstants.Requests.Rewards.baseURL);

			String srvPath = srvMap.getPath();

			Map<String, String> srvHead = srvMap.getgenericHeader();
			srvHead.remove(ApplicationConstants.Requests.Header.contentType);

			String response = HTTPRequest.executeGet(srvPath, srvHead, 0);
			return this.createValidResponse(response);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.REWARDS_GET_POINTS_FOR_PPC);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
