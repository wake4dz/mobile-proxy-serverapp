package com.wakefern.logging;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To provide REST end points to update the properties configuration
 */
@Path(MWGApplicationConstants.Log.email)
public class EmailResource {
	final static Logger logger = LogManager.getLogger(EmailResource.class);

	@GET
	@Path(MWGApplicationConstants.Log.status)
	public Response getEmailReportStatus(@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(" --- Current Property Settings ---\n");
		sb.append("isMailOn=" + LogUtil.isEmailOn + "\n");
		sb.append("isErrorMapUpdatable=" + LogUtil.isErrorMapUpdatable + "\n");
		
		sb.append("fromEmailAddress=" + LogUtil.fromEmailAddress + "\n");
		StringBuffer emailSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey() + ", " );
	    }
		sb.append("toEmailAddresses=" + emailSb.toString() + "\n");
		
		sb.append("emailWithErrorSubject=" + LogUtil.emailWithErrorSubject + "\n");
		sb.append("emailNoErrorSubject=" + LogUtil.emailNoErrorSubject + "\n");
		sb.append("mailSmtpUser=" + LogUtil.mailSmtpUser + "\n");
		sb.append("mailSmtpAuth=" + LogUtil.mailSmtpAuth + "\n");
		sb.append("mailSmtpHost=" + LogUtil.mailSmtpHost + "\n");
		sb.append("mailSmtpPort=" + LogUtil.mailSmtpPort + "\n");
		sb.append("isUserTrackOn=" + LogUtil.isUserTrackOn + "\n");
		StringBuffer userSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
	    	userSb.append(entry.getKey() + ", " );
	    }
		sb.append("trackedUserIds=" + userSb.toString() + "\n");

		
	
		return Response.status(200).entity(sb.toString()).build();
	}
	
	@PUT
	@Path(MWGApplicationConstants.Log.address)
	public Response updateEmailAddress(@PathParam("addresses") String addresses, 
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		StringBuffer sb = new StringBuffer();

		List<String> emailList = Arrays.asList(addresses.split("\\s*,\\s*"));
		
		for (String address : emailList) {
			if (LogUtil.toEmailAddressesMap.containsKey(address)) {
				// do nothing, it is already there
			} else {
				if (LogUtil.toEmailAddressMaxSize > LogUtil.toEmailAddressesMap.size() ) {
					LogUtil.toEmailAddressesMap.put(address, address);
				
				} else {
					logger.warn("toEmailAddressMaxSize of " + LogUtil.toEmailAddressMaxSize + " is reached, the " + address + " email is omitted.");
				}
				LogUtil.toEmailAddressesMap.put(address, address);
			}
		}
		
		sb.append("The current email addresses listed below after the addition:\n");
		
		StringBuffer emailSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey() + ", " );
	    }
	    
		sb.append("emailAddresses=" + emailSb.toString() + "\n");

		return Response.status(200).entity(sb.toString()).build();
	}
			
	@DELETE
	@Path(MWGApplicationConstants.Log.address)
	public Response deleteEmailAddress(@PathParam("addresses") String addresses, 
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		StringBuffer sb = new StringBuffer();

		List<String> emailList = Arrays.asList(addresses.split("\\s*,\\s*"));
		
		for (String address : emailList) {
			if (LogUtil.toEmailAddressesMap.containsKey(address)) {
				LogUtil.toEmailAddressesMap.remove(address);
			} else {
				// do nothing since it is not in the first place
			}
		}
		
		sb.append("The current email addresses listed below after the deletion:\n");
		
		StringBuffer emailSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey() + ", " );
	    }
	    
		sb.append("toEmailAddresses=" + emailSb.toString() + "\n");

		return Response.status(200).entity(sb.toString()).build();
	}
	
	
	@PUT
	@Path(MWGApplicationConstants.Log.updateSetting)
	public Response updateEmailSettings(
			@QueryParam("isEmailOn") String isEmailOn,
			@QueryParam("fromEmailAddress") String fromEmailAddress,
			@QueryParam("toEmailAddressMaxSize") Integer toEmailAddressMaxSize,
			@QueryParam("emailWithErrorSubject") String emailWithErrorSubject,
			@QueryParam("emailNoErrorSubject") String emailNoErrorSubject,
			
			@QueryParam("mailSmtpUser") String mailSmtpUser,
			@QueryParam("mailSmtpPassword") String mailSmtpPassword,
			@QueryParam("mailSmtpAuth") String isMailSmtpAuth,
			@QueryParam("mailSmtpHost") String mailSmtpHost,
			@QueryParam("mailSmtpPort") Integer mailSmtpPort,
			
			@QueryParam("isUserTrackOn") String isUserTrackOn,
			
			@QueryParam("isErrorMapUpdatable") String isErrorMapUpdatable,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		if (isEmailOn != null) {
			LogUtil.isEmailOn = Boolean.parseBoolean(isEmailOn.toLowerCase().trim());
		}
		
		if (isErrorMapUpdatable != null) {
			LogUtil.isErrorMapUpdatable = Boolean.parseBoolean(isErrorMapUpdatable.toLowerCase().trim());
		}
								
		if ((fromEmailAddress != null) && (fromEmailAddress.trim().length() > 0)){
			LogUtil.fromEmailAddress = fromEmailAddress;
		}	
		
		if (toEmailAddressMaxSize != null) {
			try {
				LogUtil.toEmailAddressMaxSize = toEmailAddressMaxSize;
			} catch (Exception e) {
				// log the error message and don't change the setting value
				logger.error("No update to toEmailAddressMaxSize. " + e.getMessage());
			}
		}
		
		if ((emailWithErrorSubject != null) && (emailWithErrorSubject.trim().length() > 0)){
			LogUtil.emailWithErrorSubject = emailWithErrorSubject;
		}	
		
		
		if ((emailNoErrorSubject != null) && (emailNoErrorSubject.trim().length() > 0)){
			LogUtil.emailNoErrorSubject = emailNoErrorSubject;
		}	
		
		
		if ((mailSmtpUser != null) && (mailSmtpUser.trim().length() > 0)){
			LogUtil.mailSmtpUser = mailSmtpUser;
		}	
		
		if ((mailSmtpPassword != null) && (mailSmtpPassword.trim().length() > 0)){
			LogUtil.mailSmtpPassword = mailSmtpPassword;
		}	
		
		if (isMailSmtpAuth != null) {
			LogUtil.mailSmtpAuth = Boolean.parseBoolean(isMailSmtpAuth.toLowerCase().trim());
		}
		
		if ((mailSmtpHost != null) && (mailSmtpHost.trim().length() > 0)){
			LogUtil.mailSmtpHost = mailSmtpHost;
		}	
	
		if (mailSmtpPort != null) {
			try {
				LogUtil.mailSmtpPort = mailSmtpPort;
			} catch (Exception e) {
				// log the error message and don't change the setting value
				logger.error("No update to mailSmtpPort. " + e.getMessage());
			}
		}
		
		if (isUserTrackOn != null) {
			LogUtil.isUserTrackOn = Boolean.parseBoolean(isUserTrackOn.toLowerCase().trim());
		}
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("The current settings listed below after the update:\n");
		

		sb.append("isEmailOn=" + LogUtil.isEmailOn + "\n");
		sb.append("isErrorMapUpdatable=" + LogUtil.isErrorMapUpdatable + "\n");
		
		sb.append("fromEmailAddress=" + LogUtil.fromEmailAddress + "\n");
		sb.append("toEmailAddressMaxSize=" + LogUtil.toEmailAddressMaxSize + "\n");
		StringBuffer emailSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey() + ", " );
	    }
		sb.append("toEmailAddresses=" + emailSb.toString() + "\n");
		sb.append("emailWithErrorSubject=" + LogUtil.emailWithErrorSubject + "\n");
		sb.append("emailNoErrorSubject=" + LogUtil.emailNoErrorSubject + "\n");
		
		sb.append("mailSmtpUser=" + LogUtil.mailSmtpUser + "\n");
		sb.append("mailSmtpPassword=" + LogUtil.mailSmtpPassword + "\n");
		sb.append("mailSmtpAuth=" + LogUtil.mailSmtpAuth + "\n");
		sb.append("mailSmtpHost=" + LogUtil.mailSmtpHost + "\n");
		sb.append("mailSmtpPort=" + LogUtil.mailSmtpPort + "\n");
		
		sb.append("isUserTrackOn=" + LogUtil.isUserTrackOn + "\n");
		StringBuffer userIdSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
            userIdSb.append(entry.getKey() + ", " );
	    }
		sb.append("trackedUserIds=" + userIdSb.toString() + "\n");

		return Response.status(200).entity(sb.toString()).build();
	}
		
	
	@PUT
	@Path(MWGApplicationConstants.Log.trackUserId)
	public Response updateTrackedUserIds(@PathParam("userIds") String userIds, 
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		StringBuffer sb = new StringBuffer();

		List<String> userIdList = Arrays.asList(userIds.split("\\s*,\\s*"));
		
		for (String userId : userIdList) {
			if (LogUtil.trackedUserIdsMap.containsKey(userId)) {
				// do nothing, it is already there
			} else {
				LogUtil.trackedUserIdsMap.put(userId.trim(), userId.trim());
			}
		}
		
		sb.append("The current tracked user ids listed below after the addition:\n");
		
		StringBuffer userIdSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
            userIdSb.append(entry.getKey() + ", " );
	    }
	    
		sb.append("trackedUserIds=" + userIdSb.toString() + "\n");

		return Response.status(200).entity(sb.toString()).build();
	}
			
	@DELETE
	@Path(MWGApplicationConstants.Log.trackUserId)
	public Response deleteTrackedUserIds(@PathParam("userIds") String userIds, 
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String authToken) {
		
		if (authToken == null || !MiscUtil.checkEnvToken(authToken)) {
			return Response.status(401).entity("Not authorized").build();
		}
		
		StringBuffer sb = new StringBuffer();

		List<String> userIdList = Arrays.asList(userIds.split("\\s*,\\s*"));
		
		for (String userId : userIdList) {
			if (LogUtil.trackedUserIdsMap.containsKey(userId.trim())) {
				LogUtil.trackedUserIdsMap.remove(userId.trim());
			} else {
				// do nothing since it is not in the first place
			}
		}
		
		sb.append("The current tracked user ids listed below after the deletion:\n");
		
		StringBuffer userIdSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
            userIdSb.append(entry.getKey() + ", " );
	    }
	    
		sb.append("trackedUserIds=" + userIdSb.toString() + "\n");

		return Response.status(200).entity(sb.toString()).build();
	}
}
