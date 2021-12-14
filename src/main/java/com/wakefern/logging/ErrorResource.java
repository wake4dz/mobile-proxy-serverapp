package com.wakefern.logging;


import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To provide the error status summary in the REST end point
 */
@Path(MWGApplicationConstants.Log.error)
public class ErrorResource {
	final static Logger logger = LogManager.getLogger(ErrorResource.class);
	
	@GET
	@Path(MWGApplicationConstants.Log.errorList)
	public Response getErrorList(@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
			
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		} 
		
		return Response.status(200).entity(LogUtil.generateErrorReport()).build();
	}
	
	
	@DELETE
	@Path(MWGApplicationConstants.Log.errorReset)
	public Response resetLogTrackData(@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
	
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		LogUtil.currentDateTime = (new Date()).getTime();
		
		LogUtil.errorMap.clear();
		LogUtil.error4xxMap.clear();
		String output = "The log tracking data has been reset.";

		return Response.status(200).entity(output).build();
	}
	

}
