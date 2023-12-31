package com.wakefern.api.proxy.wakefern.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   9/2/2020
 *  
 *  To provide a simple health checking
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.HealthCheck.Path)
public class HealthCheck {
	private final static Logger logger = LogManager.getLogger(HealthCheck.class);

	@GET
	public Response getHealthStatus() {
		logger.info("Proxy rocks...");

		return Response.ok("Proxy rocks...").build();
	}
}
