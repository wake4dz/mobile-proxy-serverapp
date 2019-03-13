package com.wakefern.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.TreeMap;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

/*
 *  author: Danny Zheng
 *  date:   7/8/2018
 *  The main log utility class to provide all essential utility methods for the logging functionalities
 */
public class LogUtil {
	private final static Logger logger = Logger.getLogger(LogUtil.class);
	
	public static boolean isEmailOn = false;
	
	public static int toEmailAddressMaxSize = 5;
	
	public static boolean isUserTrackOn = false;

	public static Map<String, String> trackedUserIdsMap = new ConcurrentHashMap<String, String>();
	
	public static String mailSmtpUser = null;
	public static String mailSmtpPassword = null;
	public static boolean mailSmtpAuth = true;
	public static String mailSmtpHost = null;
	public static int mailSmtpPort = 587;
	
	public static String fromEmailAddress = null;
	
	public static String emailWithErrorSubject = null;
	public static String emailNoErrorSubject = null;
	
	
	public static long currentDateTime;
	// for regular exception types
	public static Map<String, Integer> errorMap = new ConcurrentHashMap<String, Integer>();
	// for MWG's backend api calling errors for 401, 404, etc
	public static Map<String, Integer> error4xxMap = new ConcurrentHashMap<String, Integer>();
	
	public static boolean isErrorMapUpdatable = false;
	public static Map<String, String> toEmailAddressesMap = new ConcurrentHashMap<String, String>();
	
	public static JobDetail emailJob;
	public static JobDetail emailJob2;
	
	public static JobKey jobKey;
	public static JobKey jobKey2;
	
	public static Trigger emailTrigger;
	public static Scheduler emailScheduler;

	public static Trigger emailTrigger2;
	public static Scheduler emailScheduler2;
	
	public static InetAddress ipAddress = null;;
	
	//this static code is not run until the class is loaded into the memory for the first time
	//AppStartup is a listener which will be run after the WAS start loading.
	static {  
		try {
			
	        try {
	            ipAddress = InetAddress.getLocalHost(); // might not be accurate in the Bluemix cloud env
	            //hostName = ip.getHostName();
	        } catch (UnknownHostException e) {
	            // ignore it, nothing we can do about it.
	        }
	        
	        
			Date date = new Date();
			currentDateTime = date.getTime();
			
			isEmailOn = Boolean.parseBoolean(LogUtil.getPropertyValue("isEmailOn").trim());
				
			toEmailAddressMaxSize = Integer.parseInt(LogUtil.getPropertyValue("toEmailAddressMaxSize").trim());
			 
			mailSmtpPort = Integer.parseInt(LogUtil.getPropertyValue("mailSmtpPort").trim());
			
			mailSmtpUser = LogUtil.getPropertyValue("mailSmtpUser").trim();
			mailSmtpPassword = LogUtil.getPropertyValue("mailSmtpPassword").trim();
			mailSmtpAuth = Boolean.parseBoolean(LogUtil.getPropertyValue("mailSmtpAuth").trim());
			mailSmtpHost = LogUtil.getPropertyValue("mailSmtpHost").trim();
			mailSmtpPort = Integer.parseInt(LogUtil.getPropertyValue("mailSmtpPort").trim());
			
			fromEmailAddress = LogUtil.getPropertyValue("fromEmailAddress").trim();
			
			emailNoErrorSubject = LogUtil.getPropertyValue("emailNoErrorSubject").trim();
			emailWithErrorSubject = LogUtil.getPropertyValue("emailWithErrorSubject").trim();
			
			List<String> emailList = Arrays.asList(LogUtil.getPropertyValue("toEmailAddresses").trim().split("\\s*,\\s*"));
			int index = 0;
			for (String address : emailList) {
				if (index < toEmailAddressMaxSize) {
					toEmailAddressesMap.put(address, address);
				} else {
					logger.warn("toEmailAddressMaxSize of " + toEmailAddressMaxSize + " is reached, the " + address + " is omitted.");
				}
				index++;
			}
			
			
			isUserTrackOn = Boolean.parseBoolean(LogUtil.getPropertyValue("isUserTrackOn").trim());
			List<String> trackedUserIdList = Arrays.asList(LogUtil.getPropertyValue("trackedUserIds").trim().split("\\s*,\\s*"));
			if (trackedUserIdList.size() > 0) {
				for (String userId : trackedUserIdList) {
					trackedUserIdsMap.put(userId.trim(), userId.trim());
				}
			}

			isErrorMapUpdatable = Boolean.parseBoolean(LogUtil.getPropertyValue("isErrorMapUpdatable").trim());
	    	
		    	emailJob = JobBuilder.newJob(EmailReportScheduleJob.class)
						.withIdentity("emailJob", "emailGroup").build();
		    	
		    	emailJob2 = JobBuilder.newJob(EmailReportScheduleJob.class)
						.withIdentity("emailJob", "emailGroup").build();
	
				jobKey = new JobKey("emailJob", "emailGroup");
		    	emailJob = JobBuilder.newJob(EmailReportScheduleJob.class)
				.withIdentity(jobKey).build();
	
		    	jobKey2 = new JobKey("emailJob2", "emailGroup");
		    	emailJob2 = JobBuilder.newJob(EmailReportScheduleJob.class)
				.withIdentity(jobKey2).build();
	    	
			// for repeating every 5 seconds
			//.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
			
			// for repeating every 45 seconds with CronExpression object
			//.withSchedule(CronScheduleBuilder.cronSchedule(new CronExpression("0/45 * * * * ?")))
			
	    	// for repeating at 3:45pm every day
			//.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(15, 45))
	    	
	    	// for repeating every Monday at 03:00pm. Note: Sunday=1, Saturday=7
			//.withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(2, 03, 00))
	    	
	    	// for repeating on 28th of the month at mid night
			//.withSchedule(CronScheduleBuilder.monthlyOnDayAndHourAndMinute(28, 00, 00))
	    	
	    	
	    	//DZ's suggestion: Send email error report on every Monday and Thursday at 3:00am 
	    	//                 to prepare for busy weekends if there are many errors.
	    
	    	// #1 of 2: for Monday's 3:00am
	    	// 7:00am in Bluemix's South region = 2:00am or 3:00am EST depending on the DTS
//			emailTrigger = TriggerBuilder.newTrigger()
//					.withIdentity("emailCronTrigger", "emailGroup")
//					//.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(11, 3))
//					.withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(2, 7, 0))
//					.build();

			// #2 of 2: for Thursday's 3:00am
//			emailTrigger2 = TriggerBuilder.newTrigger()
//					.withIdentity("emailCronTrigger2", "emailGroup")
//					//.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(15, 30))
//					.withSchedule(CronScheduleBuilder.weeklyOnDayAndHourAndMinute(5, 7, 0))
//					.build();
//		
//			emailScheduler = new StdSchedulerFactory().getScheduler();
//			emailScheduler.start();
//			emailScheduler.scheduleJob(emailJob, emailTrigger);
//			emailScheduler.scheduleJob(emailJob2, emailTrigger2);

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));	
			logger.error("ATTENTION: there is an error in initiating the app logging process, check the logConfig.properties file in the classpath");
		}
		
    
	}
	
	/*
	 * concatenate all relevant URL request data 
	 */
	public static String getRequestData(Object... arguments) {
		
		StringBuffer sb = new StringBuffer();
	
		sb.append("[");
		for (int i = 0; i < arguments.length; i=i+2) {
			
			if (arguments[i] != null) {
				sb.append(arguments[i].toString());
			} else {
				sb.append("null");
			}
			
			sb.append(":");
			
			if (arguments[i+1] != null) {
				sb.append(arguments[i+1].toString());
			} else {
				sb.append("null");
			}			

			if (i+2 < arguments.length ) {
				sb.append(", ");
			}
		}
		sb.append("]");
		
		return sb.toString();
	}
	
	
	/* 
	 * only display com.wakefern related stack trace info
	 */
	public static String getRelevantStackTrace(Exception e) {
		StringBuffer sb = new StringBuffer();
		StackTraceElement[] elements = e.getStackTrace();
	
		int i = 0;
		while (i < elements.length) {
			if (elements[i].toString().contains("com.wakefern")) {
				sb.append(elements[i] + " -> ");
			} else {
				sb.append("truncated other high-level stack traces...");
				
				break; // not interested other nested JDK/library stack trace
			}
			
			i++;
		}
		
		return sb.toString();
		
	}
	
	
	/*
	 * generate the error type summary report from the maps object
	 */
	public static String generateErrorReport() {
        
		StringBuffer sb = new StringBuffer();
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Map<String, Integer> treeMap = new TreeMap<String, Integer>(LogUtil.errorMap);
		Map<String, Integer> tree4xxMap = new TreeMap<String, Integer>(LogUtil.error4xxMap);
		
		sb.append("\n\n\t\t *** Error Type Count Report ***\n");
		sb.append("\t" + sdf.format(LogUtil.currentDateTime) + " to " + sdf.format(new Date()) + "\n" );
		
		if (ipAddress == null) {
			sb.append("\tCurrent IP Address: " + "null\n\n");			
		} else {
			sb.append("\tCurrent IP Address: " + ipAddress.toString() + "\n\n");
		}
		
		if (treeMap.isEmpty() && tree4xxMap.isEmpty()) {
			sb.append("Great news: there is no reported error for this instance of the server from the last email or reset at " + sdf.format(LogUtil.currentDateTime) + "\n\n");
		} else {
			
			if (!treeMap.isEmpty()) {
				sb.append("Regular error type:\n");
			    for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
			            sb.append(StringUtils.rightPad(entry.getKey() + ":", 50) + entry.getValue() + "\n");
			     }
			    sb.append("\n");
			}
			
			if (!tree4xxMap.isEmpty()) {
				sb.append("HTTP 4xx error type:\n");
			    for (Map.Entry<String, Integer> entry : tree4xxMap.entrySet()) {
			            sb.append(StringUtils.rightPad(entry.getKey() + ":", 50) + entry.getValue() + "\n");
			     }
			}
			
		}
		
		return sb.toString();
	}
	
	
	/*
	 * get the app property value from the specified property file
	 */
	public static String getPropertyValue(String key) {

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = LogUtil.class.getClassLoader().getResourceAsStream("logConfig.properties");

			// load a properties file
			prop.load(input);
			
			return prop.getProperty(key).trim();

		} catch (IOException ex) {
			logger.error(LogUtil.getRelevantStackTrace(ex) + ",  error message: " + ex.getMessage());
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// ignore it
				}
			}
		}

	}
	
	/*
	 * null handler
	 */
	public static String getExceptionMessage(Exception e) {
		return ((e instanceof NullPointerException) ? "NullPointerException" : e.getMessage());
	}
	
	
	/*
	 * get the welcome message for the the console
	 */
	public static List<String> getWelcomeMessages() {
		List<String> messages = new ArrayList<String>();
		
		messages.add("");
		
		messages.add(StringUtils.rightPad("The 'chain' system property:", 40) + 
				MWGApplicationConstants.getSystemProperytyValue("chain"));
		messages.add(StringUtils.rightPad("The 'url' system property:", 40) + 
				MWGApplicationConstants.getSystemProperytyValue("url"));
		messages.add(StringUtils.rightPad("The 'cors' system property:", 40) + 
				MWGApplicationConstants.getSystemProperytyValue("cors"));
		messages.add(StringUtils.rightPad("The 'checkoutv3' system property:", 40) + 
				MWGApplicationConstants.getSystemProperytyValue("checkoutv3"));
		messages.add(StringUtils.rightPad("The 'plastic_bag_fee' system property:", 40) + 
				MWGApplicationConstants.getSystemProperytyValue("plastic_bag_fee"));
		messages.add("");
		
		messages.add(StringUtils.rightPad("The current IP address:", 25) + ipAddress);
		
		messages.add("");
		messages.add("The mobile app's back-end is ready to serve...");
		
		return messages;
		
	}
	
	
	/*
	 * get the welcome message for the index.jsp
	 */
	public static List<String> getWelcomeHtmlMessages() {
		List<String> messages = new ArrayList<String>();
		
		messages.add("</br> <table id=\"envVariable\">");
		messages.add("<tr> <th>System Property (VCAP) Name</th> <th>System Property (VCAP) Value</th></tr>");
		
		messages.add("<tr><td>chain</td>" + "<td>" +
				MWGApplicationConstants.getSystemProperytyValue("chain") + "</td> </tr>");

		messages.add("<tr><td>url</td>" + "<td>" +
				MWGApplicationConstants.getSystemProperytyValue("url") + "</td> </tr>");
		
		messages.add("<tr><td>cors</td>" + "<td>" +
				MWGApplicationConstants.getSystemProperytyValue("cors") + "</td> </tr>");
		
		messages.add("<tr><td>checkoutv3</td>" + "<td>" +
				MWGApplicationConstants.getSystemProperytyValue("checkoutv3") + "</td> </tr>");
		
		messages.add("<tr><td>plastic_bag_fee</td>" + "<td>" +
				MWGApplicationConstants.getSystemProperytyValue("plastic_bag_fee") + "</td> </tr>");
		
		messages.add("</table> </br> </br>");
		
		messages.add("<h2>The current IP address: " + ipAddress + "</h2>");
		
		messages.add("<h1>The mobile app's back-end is ready to serve...</h1>");
		
		return messages;
		
	}
	
	/*
	 * to determine if we need to log any errors from MWG's backend services with the HTTP codes of
	 *     401 - not authorized
	 *     404 - not found
	 *     
	 *  
	 *  These are some common 4xx:
	 *  	400 - bad request
	 *  	403 - Forbidden
	 *  	405 - method not allowed
	 *  	408 - request timeout
	 */
	public static boolean is4xxError(String exceptionMsg) {
		
		if (exceptionMsg.contains("401") || (exceptionMsg.contains("404"))) {
			return true;
		} else {
			return false;
		}
		
	}
	
	/*
	 * Add different exception type into 2 different kinds of error maps for the reporting
	 */
	public static void addErrorMaps(Exception e, MwgErrorType mwgErrorType) {

		// set it to be "false" in the logConfig.proproties for the memory issue troubleshooting
		if (isErrorMapUpdatable) {
		    	if (LogUtil.is4xxError(LogUtil.getExceptionMessage(e))) {
		        	if (!LogUtil.error4xxMap.containsKey(mwgErrorType.toString())) {
		        		LogUtil.error4xxMap.put(mwgErrorType.toString(), 1); 
		        	} else {
		        		LogUtil.error4xxMap.put(mwgErrorType.toString(), LogUtil.error4xxMap.get(mwgErrorType.toString()) + 1);
		        	}
		    	} else {
		        	if (!LogUtil.errorMap.containsKey(mwgErrorType.toString())) {
		        		LogUtil.errorMap.put(mwgErrorType.toString(), 1); 
		        	} else {
		        		LogUtil.errorMap.put(mwgErrorType.toString(), LogUtil.errorMap.get(mwgErrorType.toString()) + 1);
		        	}        		
		    	}
		}
	}
	
	/*
	 * To extract userId's in the request URL if isUserTrackOn is on. By default, isUserTrackOn is false.
	 */
	public static String getUserId(String url) {	
		try {
	        Map<String, String> urlMap=new HashMap<String, String>();
	        String queryString=StringUtils.substringAfter(url,"?");
	        for(String param : queryString.split("&")){
	            urlMap.put(StringUtils.substringBefore(param, "="),StringUtils.substringAfter(param, "="));
	        }
	        
	        return urlMap.get(MWGApplicationConstants.Requests.Params.Query.userID);
		} catch (Exception e) {
		    	logger.error(e.getMessage());
		    	return null;
	    }
	}
	
	
	/*
	 * If MWG returns an error HTML content for any REST API call, it would be transformed into a generic JSON error message
	 */
	public static String transformHtmlResponse(String response) {
		if ((response != null) && (response.indexOf("<html") > 0)) {					
			return "{ \"errorMessage\" : \"An HTML content is returned, something is not right\"} ";
		} else {
			return response;
		}
	}
}
