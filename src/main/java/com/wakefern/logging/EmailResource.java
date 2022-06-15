package com.wakefern.logging;

import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.sun.jersey.core.impl.provider.entity.XMLRootObjectProvider;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.annotations.ValidateAdminToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.wynshop.WynshopApplicationConstants;

/* 
 *  author: Danny zheng
 *  date:   7/23/2018
 *  
 *  To provide REST end points to update the properties configuration
 */
@Path(ApplicationConstants.Log.email)
public class EmailResource {
	final static Logger logger = LogManager.getLogger(EmailResource.class);

	@GET
	@Path(ApplicationConstants.Log.status)
	@ValidateAdminToken
	public Response getEmailReportStatus(@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		StringBuilder sb = new StringBuilder();
		
		sb.append(" --- Current Property Settings ---\n");
		sb.append("isMailOn=").append(LogUtil.isEmailOn).append("\n");
		sb.append("isErrorMapUpdatable=").append(LogUtil.isErrorMapUpdatable).append("\n");
		
		sb.append("fromEmailAddress=").append(LogUtil.fromEmailAddress).append("\n");
		StringBuilder emailSb = new StringBuilder();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey()).append(", ");
	    }
		sb.append("toEmailAddresses=").append(emailSb).append("\n");
		
		sb.append("emailWithErrorSubject=").append(LogUtil.emailWithErrorSubject).append("\n");
		sb.append("emailNoErrorSubject=").append(LogUtil.emailNoErrorSubject).append("\n");
		sb.append("mailSmtpUser=").append(LogUtil.mailSmtpUser).append("\n");
		sb.append("mailSmtpAuth=").append(LogUtil.mailSmtpAuth).append("\n");
		sb.append("mailSmtpHost=").append(LogUtil.mailSmtpHost).append("\n");
		sb.append("mailSmtpPort=").append(LogUtil.mailSmtpPort).append("\n");
		sb.append("isUserTrackOn=").append(LogUtil.isUserTrackOn).append("\n");
		StringBuffer userSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
	    	userSb.append(entry.getKey()).append(", ");
	    }
		sb.append("trackedUserIds=").append(userSb).append("\n");
		return Response.status(200).entity(sb.toString()).build();
	}
	
	@PUT
	@Path(ApplicationConstants.Log.address)
	@ValidateAdminToken
	public Response updateEmailAddress(@PathParam("addresses") String addresses, 
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		StringBuilder sb = new StringBuilder();

		String[] emailList = addresses.split("\\s*,\\s*");
		
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
		
		StringBuilder emailSb = new StringBuilder();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey()).append(", ");
	    }
	    
		sb.append("emailAddresses=").append(emailSb).append("\n");

		return Response.status(200).entity(sb.toString()).build();
	}
			
	@DELETE
	@Path(ApplicationConstants.Log.address)
	@ValidateAdminToken
	public Response deleteEmailAddress(@PathParam("addresses") String addresses, 
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		StringBuilder sb = new StringBuilder();

		String[] emailList = addresses.split("\\s*,\\s*");
		
		for (String address : emailList) {
			if (LogUtil.toEmailAddressesMap.containsKey(address)) {
				LogUtil.toEmailAddressesMap.remove(address);
			}
		}
		
		sb.append("The current email addresses listed below after the deletion:\n");
		
		StringBuilder emailSb = new StringBuilder();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey()).append(", ");
	    }
	    
		sb.append("toEmailAddresses=").append(emailSb).append("\n");

		return Response.status(200).entity(sb.toString()).build();
	}
	
	
	@PUT
	@Path(ApplicationConstants.Log.updateSetting)
	@ValidateAdminToken
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
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
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
		

		sb.append("isEmailOn=").append(LogUtil.isEmailOn).append("\n");
		sb.append("isErrorMapUpdatable=").append(LogUtil.isErrorMapUpdatable).append("\n");
		
		sb.append("fromEmailAddress=").append(LogUtil.fromEmailAddress).append("\n");
		sb.append("toEmailAddressMaxSize=").append(LogUtil.toEmailAddressMaxSize).append("\n");
		StringBuffer emailSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.toEmailAddressesMap.entrySet()) {
            emailSb.append(entry.getKey()).append(", ");
	    }
		sb.append("toEmailAddresses=").append(emailSb).append("\n");
		sb.append("emailWithErrorSubject=").append(LogUtil.emailWithErrorSubject).append("\n");
		sb.append("emailNoErrorSubject=").append(LogUtil.emailNoErrorSubject).append("\n");
		
		sb.append("mailSmtpUser=").append(LogUtil.mailSmtpUser).append("\n");
		sb.append("mailSmtpPassword=").append(LogUtil.mailSmtpPassword).append("\n");
		sb.append("mailSmtpAuth=").append(LogUtil.mailSmtpAuth).append("\n");
		sb.append("mailSmtpHost=").append(LogUtil.mailSmtpHost).append("\n");
		sb.append("mailSmtpPort=").append(LogUtil.mailSmtpPort).append("\n");
		
		sb.append("isUserTrackOn=").append(LogUtil.isUserTrackOn).append("\n");
		StringBuffer userIdSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
            userIdSb.append(entry.getKey()).append(", ");
	    }
		sb.append("trackedUserIds=").append(userIdSb).append("\n");

		return Response.status(200).entity(sb.toString()).build();
	}
		
	
	@PUT
	@Path(ApplicationConstants.Log.trackUserId)
	@ValidateAdminToken
	public Response updateTrackedUserIds(@PathParam("userIds") String userIds, 
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {
		StringBuffer sb = new StringBuffer();

		String[] userIdList = userIds.split("\\s*,\\s*");
		
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
            userIdSb.append(entry.getKey()).append(", ");
	    }
	    
		sb.append("trackedUserIds=").append(userIdSb).append("\n");

		return Response.status(200).entity(sb.toString()).build();
	}
			
	@DELETE
	@Path(ApplicationConstants.Log.trackUserId)
	@ValidateAdminToken
	public Response deleteTrackedUserIds(@PathParam("userIds") String userIds, 
			@HeaderParam(ApplicationConstants.Requests.Headers.Authorization) String authToken) {

		StringBuilder sb = new StringBuilder();

		String[] userIdList = userIds.split("\\s*,\\s*");
		
		for (String userId : userIdList) {
			if (LogUtil.trackedUserIdsMap.containsKey(userId.trim())) {
				LogUtil.trackedUserIdsMap.remove(userId.trim());
			}
		}
		
		sb.append("The current tracked user ids listed below after the deletion:\n");
		
		StringBuffer userIdSb = new StringBuffer();
	    for (Map.Entry<String, String> entry : LogUtil.trackedUserIdsMap.entrySet()) {
            userIdSb.append(entry.getKey()).append(", ");
	    }
	    
		sb.append("trackedUserIds=").append(userIdSb).append("\n");

		return Response.status(200).entity(sb.toString()).build();
	}
}
