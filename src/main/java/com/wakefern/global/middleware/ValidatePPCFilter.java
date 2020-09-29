package com.wakefern.global.middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakefern.dao.user.UserProfileDAO;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import org.apache.log4j.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

/**
 * Middleware (Request Filter) to validate a PPC via MI9 authentication token.
 */
@Provider
@ValidatePPC
public class ValidatePPCFilter implements ContainerRequestFilter {
	private final static Logger logger = Logger.getLogger(ValidatePPCFilter.class);

	@Override
	public void filter(final ContainerRequestContext requestContext) {
		String authHeader = requestContext.getHeaderString(MWGApplicationConstants.Headers.Params.auth);
		MultivaluedMap<String, String> pathParameters = requestContext.getUriInfo().getPathParameters();

		if (pathParameters == null || pathParameters.isEmpty()) {
			throw new BadRequestException("Path must contain templated params");
		}

		// If auth header DNE, fail.
		if (authHeader == null) {
			throw new BadRequestException("Missing auth header");
		}

		final String ppc = pathParameters.getFirst("ppc");

		final String mwgUserId = pathParameters.getFirst("userId");

		// Parse auth token and validate PPC
		final String token = authHeader;

		if (!verifyPPC(token, mwgUserId, ppc)) {
			throw new NotAuthorizedException("Not authorized");
		} else {
			logger.info("Validate PPC passed for ppc: " + ppc);
		}
	}

	/**
	 * Verify the request path PPC param matches the auth token.
	 *
	 * @param authToken MI9 session/auth token
	 * @param ppc       Price plus card number
	 * @return true if the PPC belongs to the authenticated user, false otherwise.
	 */
	private boolean verifyPPC(final String authToken, final String userId, final String ppc) {
		String path = MWGApplicationConstants.getBaseURL() + MWGApplicationConstants.Requests.Account.prefix + "/chains/FBFB139/users/" + userId;

		HashMap<String, String> requestHeaders = new HashMap<>();
		requestHeaders.put("Authorization", authToken);
		requestHeaders.put("Accept", MWGApplicationConstants.Headers.Account.profile);
		requestHeaders.put("Content-Type", MWGApplicationConstants.Headers.Account.profile);
		requestHeaders.put("User-Agent", ApplicationConstants.StringConstants.wakefernApplication);

		try {
			String response = HTTPRequest.executeGet(path, requestHeaders, VcapProcessor.getApiLowTimeout());
			ObjectMapper mapper = new ObjectMapper();
			UserProfileDAO userProfileDAO = mapper.readValue(response, UserProfileDAO.class);
			final String userProfilePPC = userProfileDAO.getFrequentShopperNumber();
			return ppc.contentEquals(userProfilePPC);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.USERS_GET_PROFILE);
			logger.error(LogUtil.getExceptionMessage(e));
			throw new ServerErrorException(502);
		}
	}
}
