package com.wakefern.logging;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.Level;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import org.apache.logging.log4j.core.config.Configurator;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To change some of log4j settings in the real time
 */
@Path(MWGApplicationConstants.Log.logger)
public class LoggerResource {

	final static Logger logger = LogManager.getLogger(LoggerResource.class);

	@PUT
	@Path(MWGApplicationConstants.Log.changeLevel)
	public Response updateLoggerLevel(@PathParam("logLevel") String inputLevel,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {

		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		Level logLevel = null;
		String outputText = "";

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
	@Path(MWGApplicationConstants.Log.getLevel)
	public Response getLoggerLevel(@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		String output = "Log4i current log level is " + LogManager.getRootLogger().getLevel()
				+ ".\nThe available level options: off, trace, debug, info, warn, error, fatal.";
		return Response.status(200).entity(output).build();
	}


	@GET
	@Path("/appender/list")
	public Response listAppenders(@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		StringBuffer sb = new StringBuffer();

		Map<String, Appender> appenderMap = ((org.apache.logging.log4j.core.Logger) LogManager.getRootLogger()).getAppenders();

		for (Appender appender : appenderMap.values()) {
			sb.append(appender.getName() + ", ");
		}

		int index = sb.lastIndexOf(",");
		if (index > 0) {
			sb.replace(index, index+1, ".");
		}

		String output = "List current Log4j appenders: " + sb;
		return Response.status(200).entity(output).build();
	}

}
