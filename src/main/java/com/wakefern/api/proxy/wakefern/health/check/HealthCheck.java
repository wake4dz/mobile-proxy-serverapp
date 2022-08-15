package com.wakefern.api.proxy.wakefern.health.check;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
	
	@GET
	public String getHealthStatus() {
		return "Proxy rocks...";
	}
}
