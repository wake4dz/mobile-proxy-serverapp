package com.wakefern.api.proxy.wakefern.rewards;

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
import com.wakefern.global.ApplicationConstants.Requests;

import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
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
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/{ppc}")
	public Response getInfoResponse(@PathParam("ppc") String ppc) {
		String requestToken = null;
		try {
			// This is a service provided and maintained by Wakefern.
			// So it requires a different Authorization Header Token than the one provided by the UI's Bearer token.
			
			// The Reward Point API key is the same key as the Product Recommendation API
			requestToken = EnvManager.getRewardPointKey();

			String baseUrl;

			baseUrl = EnvManager.getTargetRewardServiceEndpoint();
	
			final String srvPath = baseUrl + WakefernApplicationConstants.RewardPoint.Upstream.Points + "/" + ppc;

			Map<String, String> headers = new HashMap<>();
			headers.put(ApplicationConstants.Requests.Headers.Accept, ApplicationConstants.Requests.Headers.MIMETypes.json);
			headers.put(ApplicationConstants.Requests.Headers.Authorization, requestToken);
			headers.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

			final String response = HTTPRequest.executeGet(srvPath, headers, 0);
			return createValidResponse(response);
		} catch (Exception e) {

			String errorData = parseAndLogException(logger, e, 
					ApplicationConstants.Requests.Headers.Authorization, requestToken,
					"ppc", ppc,
					ApplicationConstants.Requests.Headers.Accept, ApplicationConstants.Requests.Headers.MIMETypes.json);
			
			return createErrorResponse(errorData, e);
		}
	}

}
