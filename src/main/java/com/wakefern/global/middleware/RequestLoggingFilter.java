package com.wakefern.global.middleware;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Filter to run before every request in order to create request identifier
 * to allow for better log tracing during a request.
 *
 * Create a correlation id if it does not exist to trace a request or collection of requests.
 */
public class RequestLoggingFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

    }
}
