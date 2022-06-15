package com.wakefern.logging;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.annotations.ValidateAdminToken;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.core.config.Configurator;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To change some of log4j settings in the real time
 */
@Path(ApplicationConstants.Log.logger)
public class LoggerResource {

	final static Logger logger = LogManager.getLogger(LoggerResource.class);

	@PUT
	@Path(ApplicationConstants.Log.changeLevel)
	@ValidateAdminToken
	public Response updateLoggerLevel(@PathParam("logLevel") String inputLevel,
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		Level logLevel = null;
		String outputText = "";

		if (inputLevel == null) {
			return Response.status(Response.Status.BAD_REQUEST).entity("loglevel must not be null").build();
		}

		inputLevel = inputLevel.trim().toLowerCase();

		switch (inputLevel) {
		case "off":
			logLevel = Level.OFF;
			break;
		case "trace":
			logLevel = Level.TRACE;
			break;
		case "debug":
			logLevel = Level.DEBUG;
			break;
		case "info":
			logLevel = Level.INFO;
			break;
		case "warn":
			logLevel = Level.WARN;
			break;
		case "error":
			logLevel = Level.ERROR;
			break;
		case "fatal":
			logLevel = Level.FATAL;
			break;
		default:
			logLevel = Level.INFO;
		}

		if (LogManager.getRootLogger().getLevel() == logLevel) { // no changed log level
			outputText = "Log4i log level is " + logLevel + ", which is the same as before"
					+ ".\nThe available level options: off, trace, debug, info, warn, error, fatal.";			
		} else {
			Configurator.setRootLevel(logLevel);
			outputText = "Log4i log level is now set to be " + logLevel
					+ ".\nThe available level options: off, trace, debug, info, warn, error, fatal.";
			logger.warn(outputText);
		}

		return Response.status(200).entity(outputText).build();
	}

	@GET
	@Path(ApplicationConstants.Log.getLevel)
	@ValidateAdminToken
	public Response getLoggerLevel(@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		String output = "Log4i current log level is " + LogManager.getRootLogger().getLevel()
				+ ".\nThe available level options: off, trace, debug, info, warn, error, fatal.";
		return Response.status(200).entity(output).build();
	}

	@GET
	@Path(ApplicationConstants.Log.appenderList)
	@ValidateAdminToken
	public Response listAppenders(@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		StringBuilder sb = new StringBuilder();

		Map<String, Appender> appenderMap = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).getAppenders();

		for (Appender appender : appenderMap.values()) {
			sb.append(appender.getName()).append(", ");
		}

		int index = sb.lastIndexOf(",");
		if (index > 0) {
			sb.replace(index, index+1, ".");
		}

		String output = "List current Log4j appenders: " + sb;
		return Response.status(200).entity(output).build();
	}

}
