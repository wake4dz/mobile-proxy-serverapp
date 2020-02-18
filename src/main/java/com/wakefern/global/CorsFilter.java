package com.wakefern.global;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

import org.apache.commons.lang3.StringUtils;

/**
 * Add CORS headers to ShopRiteStage server responses.
 *
 * Note: this does not prevent clients that ignore CORS headers from interacting
 * with the server
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {
	private static final class CorsHeaders {
		public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
		public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
		public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
		public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";
	}
	private static final String[] ALLOWED_ORIGINS = { "http://localhost:7000", "http://localhost:7001" };
	private static final String[] ALLOWED_HEADERS_LIST = { MWGApplicationConstants.Headers.Params.contentType,
			MWGApplicationConstants.Headers.Params.accept, MWGApplicationConstants.Headers.Params.auth,
			MWGApplicationConstants.Headers.Params.reservedTimeslot, ApplicationConstants.Requests.Header.appVersion,
			ApplicationConstants.Requests.Header.correlationId };
	private static final String ALLOWED_HEADERS = StringUtils.join(ALLOWED_HEADERS_LIST, ", ");
	private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
	private static final String MAX_AGE = "1209600";

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext response) {
		final String shouldEnableCors = MWGApplicationConstants
				.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.CORS);
		final String origin = requestContext.getHeaderString("Origin");

		if (shouldEnableCors != null && shouldEnableCors.equals("true") && isAcceptedOrigin(origin)) {
			response.getHeaders().add(CorsHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
			response.getHeaders().add(CorsHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ALLOWED_HEADERS);
			response.getHeaders().add(CorsHeaders.ACCESS_CONTROL_ALLOW_METHODS, ALLOWED_METHODS);
			response.getHeaders().add(CorsHeaders.ACCESS_CONTROL_MAX_AGE, MAX_AGE);
		}
	}

	/**
	 * Returns a boolean that represents whether or not the origin should be served
	 * by the server. Checks origins in the ALLOWED_ORIGINS string array.
	 *
	 * @param origin value of the "Origin" HTTP request header
	 * @return if the origin is allowed to be served by the server, false otherwise.
	 */
	private static Boolean isAcceptedOrigin(String origin) {
		for (String allowedOrigin : ALLOWED_ORIGINS) {
			if (allowedOrigin.equals(origin)) {
				return true;
			}
		}
		return false;
	}
}
