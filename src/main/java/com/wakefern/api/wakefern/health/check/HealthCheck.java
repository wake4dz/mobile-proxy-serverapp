package com.wakefern.api.wakefern.health.check;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.wakefern.WakefernApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   9/2/2020
 *  
 *  To provide a simplest return for health checking with CIS's Global Load Balancer
 *  and reduce the logging in the Papertrail log destination for this repeated API calls
 */
@Path(WakefernApplicationConstants.HealthCheck.Path)
public class HealthCheck {
	final static Logger logger = LogManager.getLogger(HealthCheck.class);
	
	@GET
	public String getHealthStatus() {
		return "";
	}
}
