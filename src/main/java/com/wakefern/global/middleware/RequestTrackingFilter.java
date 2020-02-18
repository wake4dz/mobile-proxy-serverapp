package com.wakefern.global.middleware;

import com.wakefern.global.ApplicationConstants;
import org.apache.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter to run before every request in order to create request identifier
 * to allow for improved log tracing during a request.
 *
 * Create a correlation id if it does not exist to trace a request or collection of requests.
 */
@Provider
public class RequestTrackingFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static Logger logger = Logger.getLogger(RequestTrackingFilter.class);
    /**
     * Add the X-SR-Correlation-Id header to the incoming request, or use it if it already exists.
     * @param request
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        // Get the request correlation id header value
        String correlationId = request.getHeaderString(ApplicationConstants.Requests.Header.correlationId);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = generateCorrelationId();
            request.getHeaders().add(ApplicationConstants.Requests.Header.correlationId, correlationId);
        }
    }

    /**
     * Print a user friendly
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext request, final ContainerResponseContext response) throws IOException {
        // Get the request correlation id header value
        String correlationId = request.getHeaderString(ApplicationConstants.Requests.Header.correlationId);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = generateCorrelationId();
        }
        // Add the correlation id to the response.
        response.getHeaders().add(ApplicationConstants.Requests.Header.correlationId, correlationId);
        logger.trace(String.format("correlation_id=%s req_method=%s req_path=%s res_status=%d res_length=%d",
                request.getMethod(), request.getUriInfo(), correlationId, response.getStatus(),
                response.getLength()));
    }

    /**
     * Generate a correlation id used to trace individual request logs or user sessions.
     * @return the correlation identifier string
     */
    private static String generateCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
