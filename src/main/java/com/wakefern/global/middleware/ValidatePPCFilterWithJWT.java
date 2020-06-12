package com.wakefern.global.middleware;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.wakefern.account.authentication.UserJWT;
import com.wakefern.global.annotations.ValidatePPCWithJWT;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

/**
 * Middleware (Request Filter) to validate a PPC via UserJWT Bearer Token.
 */
@Provider
@ValidatePPCWithJWT
public class ValidatePPCFilterWithJWT implements ContainerRequestFilter {
	private final static Logger logger = Logger.getLogger(ValidatePPCFilterWithJWT.class);

	@Override
	public void filter(final ContainerRequestContext requestContext) {
		String authorization = requestContext.getHeaderString(MWGApplicationConstants.Headers.Params.auth);
		MultivaluedMap<String, String> pathParameters = requestContext.getUriInfo().getPathParameters();

		if (pathParameters == null || pathParameters.isEmpty()) {
			throw new BadRequestException("Path must contain templated params");
		}

		if (authorization == null) {
			throw new BadRequestException("Missing auth header");
		}

		final String ppc = pathParameters.getFirst("ppc");

		final String bearerToken = authorization;
		if (!UserJWT.isValid(bearerToken, ppc)) {
			throw new NotAuthorizedException("Not authorized");
		} else {
			logger.info("Validate PPC passed for ppc: " + ppc);
		}
	}
}