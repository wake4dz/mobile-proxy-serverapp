package com.wakefern.logging;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;

@Path(ApplicationConstants.Logging.Logging)
public class AppLogging extends BaseService{

	private final static Logger logger = LogManager.getLogger(AppLogging.class);

	@POST
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.json)
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.json)
    @Path(ApplicationConstants.Logging.log)
    public Response getResponse(
    		@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String sessionToken,
    		@HeaderParam(ApplicationConstants.Requests.Headers.Accept) String accept,
    		@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
    		String jsonData) {

		try {
			logger.error(jsonData);
			
			return this.createValidResponse("{\"success\":true, \"message\":\"Message Logged!\"}");
		} catch(Exception e) {
			logger.error("[AppLogging]::Error logging data: "+e.getMessage());
			return this.createErrorResponse(e);
		}
		
	}

}
