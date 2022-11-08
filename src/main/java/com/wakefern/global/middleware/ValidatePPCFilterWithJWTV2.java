package com.wakefern.global.middleware;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import com.wakefern.global.ApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.UserJWTV2;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;

/**
 * Middleware (Request Filter) to validate a PPC via UserJWT Bearer Token.
 */
@Provider
@ValidatePPCWithJWTV2
public class ValidatePPCFilterWithJWTV2 implements ContainerRequestFilter {
	private final static Logger logger = LogManager.getLogger(ValidatePPCFilterWithJWTV2.class);

	@Override
	public void filter(final ContainerRequestContext requestContext) {
		String authorization = requestContext.getHeaderString(ApplicationConstants.Requests.Headers.Authorization);
		MultivaluedMap<String, String> pathParameters = requestContext.getUriInfo().getPathParameters();

		if (pathParameters == null || pathParameters.isEmpty()) {
			throw new BadRequestException("Path must contain templated params");
		}

		if (authorization == null) {
			throw new BadRequestException("Missing auth header");
		}

		final String ppc = pathParameters.getFirst("ppc");

		final String bearerToken = authorization;
		if (!UserJWTV2.isValid(bearerToken, ppc)) {
			throw new NotAuthorizedException("Not authorized");
		} else {
			logger.debug("Validate PPC passed for ppc: " + ppc);
		}
	}
}
