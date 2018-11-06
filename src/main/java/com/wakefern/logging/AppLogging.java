package com.wakefern.logging;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(ApplicationConstants.Logging.Logging)
public class AppLogging extends BaseService{

	private final static Logger logger = Logger.getLogger(AppLogging.class);

	@POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    @Path(ApplicationConstants.Logging.log)
    public Response getResponse(
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
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
