package com.wakefern.global.errorHandling;

import org.apache.http.HttpStatus;

import javax.ws.rs.core.Response;

public class UpstreamErrorHandler {

	/**
	 * Rewrite upstream authentication errors as 500 errors.
	 * @param upstreamResponse
	 * @param useBadGateway
	 * @return Response
	 */
	public static Response handleResponse(Response upstreamResponse, boolean useBadGateway) {
		// We must differentiate between authentication errors with the service,
		// and MI9 session authentication errors.
		if (upstreamResponse.getStatus() == HttpStatus.SC_FORBIDDEN ||
				upstreamResponse.getStatus() == HttpStatus.SC_UNAUTHORIZED) {
			if (useBadGateway) {
				return Response.status(Response.Status.BAD_GATEWAY).build();
			}
			return Response.serverError().build();
		}

		return upstreamResponse;
	}

	public static Response handleResponse(Response upstreamResponse) {
		return handleResponse(upstreamResponse, false);
	}
}
