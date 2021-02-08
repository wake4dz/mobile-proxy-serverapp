package com.wakefern.global.errorHandling;

import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;

public class UpstreamErrorHandler {

	/**
	 * Rewrite upstream authentication errors as 500 errors.
	 * @param upstreamResponse
	 * @return Response
	 */
	public static Response handleResponse(Response upstreamResponse) {
		// We must differentiate between authentication errors with the service,
		// and MI9 session authentication errors.
		if (upstreamResponse.getStatus() == HttpStatus.SC_FORBIDDEN ||
				upstreamResponse.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
			return Response.serverError().build();
		}

		return upstreamResponse;
	}
}
