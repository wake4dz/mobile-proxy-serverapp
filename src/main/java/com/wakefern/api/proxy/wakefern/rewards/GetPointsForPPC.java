package com.wakefern.api.proxy.wakefern.rewards;

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
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
/*
 * The reward point program is maintained by the Wakefern, the program is not always available.
 * @author: Danny Zheng
 */
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.RewardPoint.Proxy.rewardPath)
public class GetPointsForPPC extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetPointsForPPC.class);

	/**
	 * Get a user's PPC points. It includes an user PPC via UserJWT Bearer Token to ensure
	 * the PPC# specified in the URL path is matched with the bearer token from Mi9.
	 * 
	 * To get a PPCJWTToken by this calling API first, usually set expires_in to be the OAuth token expiration time.
	 * {{baseURL}}/api/proxy/ppc/jwt/token?expires_in=XX
	 * 
	 * @param ppc
	 * @return
	 */
	@GET
	@ValidatePPCWithJWTV2
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path("/{ppc}")
	public Response getInfoResponse(@PathParam("ppc") String ppc) {
		try {
			// This is a service provided and maintained by Wakefern.
			// So it requires a different Authorization Header Token than the one provided by the UI's Bearer token.
			
			// The Reward Point API key is the same key as the Product Recommendation API
			// of sr_product_recommendation_key defined manifest.yml
			this.requestToken = MWGApplicationConstants.getProductRecmdAuthToken();
			this.requestPath = MWGApplicationConstants.Requests.Rewards.Points + "/" + ppc;

			ServiceMappings srvMap = new ServiceMappings();

			if ((VcapProcessor.getRewardPointService() != null) && (VcapProcessor.getRewardPointService().trim().equalsIgnoreCase("Staging"))) {
				srvMap.setMappingWithURL(this, WakefernApplicationConstants.RewardPoint.Upstream.baseStagingURL);
			} else { // anything else is for Production
				srvMap.setMappingWithURL(this, WakefernApplicationConstants.RewardPoint.Upstream.baseURL);
			}
			
			String srvPath = srvMap.getPath();

			Map<String, String> srvHead = srvMap.getGenericHeader();
			srvHead.remove(ApplicationConstants.Requests.Header.contentType);

			String response = HTTPRequest.executeGet(srvPath, srvHead, 0);
			return this.createValidResponse(response);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PROXY_REWARDS_GET_POINTS_FOR_PPC);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

}
