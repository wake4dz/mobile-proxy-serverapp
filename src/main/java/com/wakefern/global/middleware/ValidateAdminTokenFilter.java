package com.wakefern.global.middleware;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.annotations.ValidateAdminToken;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

/**
 * Middleware (Request Filter) to validate a request with an admin authorization token.
 */
@Provider
@ValidateAdminToken
public class ValidateAdminTokenFilter implements ContainerRequestFilter {
	private final static Logger logger = LogManager.getLogger(ValidateAdminTokenFilter.class);

	@Override
	public void filter(final ContainerRequestContext requestContext) {
		final String authorization = requestContext.getHeaderString(ApplicationConstants.Requests.Headers.Authorization);
		if (authorization == null) {
			throw new BadRequestException("Missing auth header");
		}

		final String adminToken = authorization;
		if (isInvalidAdminToken(adminToken)) {
			throw new NotAuthorizedException("Not authorized");
		} else {
			logger.debug("Validate Admin token failed for token: "+ adminToken);
		}
	}

	/**
	 * Validate the admin token.
	 * @param token
	 * @return
	 */
	private static boolean isInvalidAdminToken(final String token) {
		if (token == null) return true;
		return !token.trim().equalsIgnoreCase(ApplicationUtils
				.getEnvValue(WakefernApplicationConstants.EnvVarsKeys.PROXY_ADMIN_KEY));
	}
}
