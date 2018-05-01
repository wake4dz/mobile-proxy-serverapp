package com.wakefern.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(ApplicationConstants.Logging.Logging)
public class AppLogging extends BaseService{

    private final static Logger logger = Logger.getLogger("MobileLogging");

	@POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    @Path(ApplicationConstants.Logging.log)
    public Response getResponse(
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		String jsonData) {

		try {
			logger.log(Level.SEVERE, jsonData);
			
			return this.createValidResponse("{\"success\":true, \"message\":\"Message Logged!\"}");
		} catch(Exception e) {
			logger.log(Level.SEVERE, "[AppLogging]::Error logging data: "+e.getMessage());
			return this.createErrorResponse(e);
		}
		
	}

}
