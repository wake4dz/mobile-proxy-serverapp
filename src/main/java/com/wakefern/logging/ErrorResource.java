package com.wakefern.logging;


import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.annotations.ValidateAdminToken;

import com.wakefern.wynshop.WynshopApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To provide the error status summary in the REST end point
 */
@Path(ApplicationConstants.Log.error)
public class ErrorResource {
	@GET
	@Path(ApplicationConstants.Log.errorList)
	@ValidateAdminToken
	public Response getErrorList(@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		return Response.status(200).entity(LogUtil.generateErrorReport()).build();
	}
	
	
	@DELETE
	@Path(ApplicationConstants.Log.errorReset)
	@ValidateAdminToken
	public Response resetLogTrackData(@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		LogUtil.currentDateTime = (new Date()).getTime();
		
		LogUtil.errorMap.clear();
		LogUtil.error4xxMap.clear();
		String output = "The log tracking data has been reset.";

		return Response.status(200).entity(output).build();
	}
}
