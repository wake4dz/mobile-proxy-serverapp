package com.wakefern.global;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;

import org.apache.commons.lang3.StringUtils;

/**
 * Add CORS headers to ShopRiteStage server responses.
 *
 * Note: this does not prevent clients that ignore CORS headers from interacting
 * with the server
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {
	private static final String[] ALLOWED_ORIGINS = { "http://localhost:7000", "http://localhost:7001" };
	private static final String[] ALLOWED_HEADERS_LIST = { MWGApplicationConstants.Headers.Params.contentType,
			MWGApplicationConstants.Headers.Params.accept, MWGApplicationConstants.Headers.Params.auth,
			MWGApplicationConstants.Headers.Params.reservedTimeslot, ApplicationConstants.Requests.Header.appVersion };
	private static final String ALLOWED_HEADERS = StringUtils.join(ALLOWED_HEADERS_LIST, ", ");
	private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
	private static final String MAX_AGE = "1209600";

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext response)
			throws IOException {
		final String shouldEnableCors = MWGApplicationConstants
				.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.cors);
		final String origin = requestContext.getHeaderString("Origin");

		if (shouldEnableCors != null && shouldEnableCors.equals("true") && isAcceptedOrigin(origin)) {
			response.getHeaders().add("Access-Control-Allow-Origin", origin);
			response.getHeaders().add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
			response.getHeaders().add("Access-Control-Allow-Methods", ALLOWED_METHODS);
			response.getHeaders().add("Access-Control-Max-Age", MAX_AGE);
		}
	}

	/**
	 * Returns a boolean that represents whether or not the origin should be served
	 * by the server. Checks origins in the ALLOWED_ORIGINS string array.
	 *
	 * @param origin value of the "Origin" HTTP request header
	 * @return if the origin is allowed to be served by the server
	 */
	private static Boolean isAcceptedOrigin(String origin) {
		for (int i = 0; i < ALLOWED_ORIGINS.length; i++) {
			if (ALLOWED_ORIGINS[i].equals(origin)) {
				return true;
			}
		}

		return false;
	}
}
