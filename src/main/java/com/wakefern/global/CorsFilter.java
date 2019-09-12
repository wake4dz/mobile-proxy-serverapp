package com.wakefern.global;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Add CORS headers to ShopRiteStage server responses for localhost:7000.
 * This allows frontend development in the browser to successfully make cross-origin requests to this server.
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

	@Override
	public void filter(final ContainerRequestContext requestContext, final ContainerResponseContext response) throws IOException {
		final String shouldEnableCors = MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.cors);
		
		if (shouldEnableCors != null && shouldEnableCors.equals("true")) {
			response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:7000"); // allow cross-origin requests from frontend dev server
			
			// on 2019-09-08 DZ removed the HTTP headers of "Origin" and "Token" from the CORS filter list
			response.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Accept, Authorization");
			//response.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization, Token");
			
			response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
			response.getHeaders().add("Access-Control-Max-Age", "1209600");
		}
   }

}
