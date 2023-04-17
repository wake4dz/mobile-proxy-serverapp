package com.wakefern.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.EnvManager;

/*
 *  author: Danny Zheng
 *  date:   7/8/2018
 *  The main log utility class to provide all essential utility methods for the logging functionalities
 */
public class LogUtil {
	private final static Logger logger = LogManager.getLogger(LogUtil.class);

	public static InetAddress ipAddress = null;

    // this static code is not run until the class is loaded into the memory for the first time
	// AppStartup is a listener which will be run after the WAS start loading.
	static {

		try {
			ipAddress = InetAddress.getLocalHost(); // might not be accurate in the Bluemix cloud env
		} catch (UnknownHostException e) {
				// ignore it, nothing we can do about it.
		}


	}

	/**
	 * Concatenate all relevant URL request data
	 * 
	 * @param arguments
	 * @return
	 */
	public static String getRequestData(Object... arguments) {

		StringBuffer sb = new StringBuffer();

		sb.append("[");
		for (int i = 0; i < arguments.length; i = i + 2) {

			if (arguments[i] != null) {
				sb.append(arguments[i].toString());
			} else {
				sb.append("null");
			}

			sb.append(":");

			if (arguments[i + 1] != null) {
				sb.append(arguments[i + 1].toString());
			} else {
				sb.append("null");
			}

			if (i + 2 < arguments.length) {
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
				sb.append(elements[i]).append(" -> ");
			} else {
				sb.append("truncated other high-level stack traces...");

				break; // not interested other nested JDK/library stack trace
			}

			i++;
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

	private static final int FORMAT_RIGHT_PADDING = 60;

	private static String pad(String str) {
		return StringUtils.rightPad(str, FORMAT_RIGHT_PADDING);
	}

	/*
	 * get the welcome message for the console
	 */
	public static List<String> getWelcomeMessages() {
		List<String> messages = new ArrayList<>();

		messages.add("");

		messages.add("" + ReleaseUtil.getReleaseLines().get(0));
		messages.add("" + ReleaseUtil.getReleaseLines().get(1));
		messages.add("" + ReleaseUtil.getReleaseLines().get(2));

		messages.add("");

		messages.add(pad("The 'banner' env variable:") + EnvManager.getBanner());
		messages.add(pad("The 'mi9v8_service' env variable:") + EnvManager.getMi9v8Service());
		messages.add(pad("The 'coupon_service' env variable:") + EnvManager.getCouponService());
		messages.add(pad("The 'recipe_service' env variable:") + EnvManager.getRecipeService());
		messages.add(pad("The 'wallet_service' env variable:") + EnvManager.getWalletService());
		messages.add(pad("The 'srfh_orders_service' env variable:") + EnvManager.getSrfhOrdersService());
		messages.add(pad("The 'srfh_curbside_service' env variable:") + EnvManager.getSrfhCurbsideService());
		messages.add(pad("The 'prodx_service' env variable:") + EnvManager.getProdxService());
		messages.add(pad("The 'reward_point_service' env variable:") + EnvManager.getRewardPointService());
		messages.add(pad("The 'push2device_service' env variable:") + EnvManager.getPush2DeviceService());

		messages.add(pad("The 'mute_error_log' env variable:") + EnvManager.isMuteErrorLog());
		messages.add(pad("The 'mute_http_code' env variable:") + EnvManager.getMuteHttpCode());
		
		messages.add(pad("The 'api_high_timeout' env variable:") + EnvManager.getApiHighTimeout());
		messages.add(pad("The 'api_medium_timeout' env variable:") + EnvManager.getApiMediumTimeout());
		messages.add(pad("The 'api_low_timeout' env variable:") + EnvManager.getApiLowTimeout());
		messages.add(pad("The 'http_default_connect_timeout_ms' env variable:")
				+ EnvManager.getHttpDefaultConnectTimeoutMs());
		messages.add(pad("The 'http_default_read_timeout_ms' env variable:")
				+ EnvManager.getHttpDefaultReadTimeoutMs());

		messages.add(pad("The 'recipe_shelf_thread_pool_size' env variable:")
				+ EnvManager.getRecipeShelfThreadPoolSize());

		messages.add(pad("The 'item_locator_cache_enabled' env variable:")
				+ EnvManager.isItemLocatorCacheEnabled());

		messages.add(pad("The 'item_locator_partition_size' env variable:")
				+ EnvManager.getItemLocatorPartitionSize());

		messages.add("");

		messages.add(pad("The server IP address:") + ipAddress);

		messages.add("");
		messages.add("The mobile app's back-end is ready to serve...");

		return messages;
	}

	/*
	 * get the welcome message for the index.jsp
	 */
	public static List<String> getWelcomeHtmlMessages(String clientIp) {
		List<String> messages = new ArrayList<>();

		if (isAuthorized(clientIp)) {
			messages.add("<h1> Server Status </h1> <br />");
			messages.add("<table id=\"envVariable\">");
			messages.add("<tr> <th>Release Property Name</th> <th>Release Property Value</th></tr>");

			messages.add(
					"<tr><td>version</td>" + "<td>"
							+ ReleaseUtil.getReleaseLines().get(0)
									.substring(ReleaseUtil.getReleaseLines().get(0).indexOf("=") + 1).trim()
							+ "</td> </tr>");

			messages.add(
					"<tr><td>date</td>" + "<td>"
							+ ReleaseUtil.getReleaseLines().get(1)
									.substring(ReleaseUtil.getReleaseLines().get(1).indexOf("=") + 1).trim()
							+ "</td> </tr>");

			messages.add(
					"<tr><td>branch</td>" + "<td>"
							+ ReleaseUtil.getReleaseLines().get(2)
									.substring(ReleaseUtil.getReleaseLines().get(2).indexOf("=") + 1).trim()
							+ "</td> </tr>");

			messages.add("</table> <br /> <br />");

			messages.add("<table id=\"envVariable\">");
			messages.add("<tr> <th>Environment Variable Name</th> <th>Environment Variable Value</th></tr>");

			messages.add("<tr><td>banner</td>" + "<td>" + EnvManager.getBanner() + "</td> </tr>");

			messages.add("<tr><td>mi9v8_service</td>" + "<td>" + EnvManager.getMi9v8Service() + "</td> </tr>");
			
			messages.add("<tr><td>coupon_service</td>" + "<td>" + EnvManager.getCouponService() + "</td> </tr>");

			messages.add("<tr><td>wallet_service</td>" + "<td>" + EnvManager.getWalletService() + "</td> </tr>");

			messages.add("<tr><td>recipe_service</td>" + "<td>" + EnvManager.getRecipeService() + "</td> </tr>");

			messages.add("<tr><td>srfh_orders_service</td><td>" + EnvManager.getSrfhOrdersService() + "</td></tr>");

			messages.add("<tr><td>srfh_curbside_service</td><td>" + EnvManager.getSrfhCurbsideService() + "</td></tr>");

			messages.add("<tr><td>prodx_service</td><td>" + EnvManager.getProdxService() + "</td></tr>");

			messages.add("<tr><td>reward_point_service</td><td>" + EnvManager.getRewardPointService() + "</td></tr>");

			messages.add("<tr><td>push2device_service</td><td>" + EnvManager.getPush2DeviceService() + "</td></tr>");

			messages.add("<tr><td>mute_error_log</td><td>" + EnvManager.isMuteErrorLog() + "</td></tr>");
			messages.add("<tr><td>mute_http_code</td><td>" + EnvManager.getMuteHttpCode() + "</td></tr>");

			messages.add("<tr><td>api_high_timeout</td>" + "<td>" + EnvManager.getApiHighTimeout() + "</td> </tr>");

			messages.add(
					"<tr><td>api_medium_timeout</td>" + "<td>" + EnvManager.getApiMediumTimeout() + "</td> </tr>");

			messages.add("<tr><td>api_low_timeout</td>" + "<td>" + EnvManager.getApiLowTimeout() + "</td> </tr>");

			messages.add("<tr><td>http_default_connect_timeout_ms</td><td>"
					+ EnvManager.getHttpDefaultConnectTimeoutMs() + "</td></tr>");

			messages.add("<tr><td>http_default_read_timeout_ms</td><td>"
					+ EnvManager.getHttpDefaultReadTimeoutMs() + "</td></tr>");

			messages.add("<tr><td>recipe_shelf_thread_pool_size</td><td>" + EnvManager.getRecipeShelfThreadPoolSize() + "</td></tr>");
			messages.add("<tr><td>item_locator_cache_enabled</td><td>" + EnvManager.isItemLocatorCacheEnabled() + "</td></tr>");
			messages.add("<tr><td>item_locator_partition_size</td><td>" + EnvManager.getItemLocatorPartitionSize() + "</td></tr>");

			messages.add("</table> <br /> <br />");

			messages.add("<h2>The client IP address: " + clientIp + "</h2>");
			messages.add("<h2>The server IP address: " + ipAddress + "</h2>");
			
			messages.add("<h1>The mobile app's back-end is ready to serve...</h1>");

		} else {
			// nothing displayed as Mark wants this way
		}

		return messages;
	}

	/*
	 * to determine if we need to log any errors from MWG's backend services with
	 * the HTTP codes of 401 - not authorized 404 - not found
	 * 
	 * 
	 * These are some common 4xx: 400 - bad request 403 - Forbidden 405 - method not
	 * allowed 408 - request timeout
	 */
	public static boolean is4xxError(String exceptionMsg) {
		return exceptionMsg.contains("401") || (exceptionMsg.contains("404"));
	}




	/*
	 * If MWG returns an error HTML content for any REST API call, it would be
	 * transformed into a generic JSON error message
	 */
	public static String transformHtmlResponse(String response) {
		if ((response != null) && (response.indexOf("<html") > 0)) {
			return "{ \"errorMessage\" : \"An HTML content is returned, something is not right\"} ";
		} else {
			return response;
		}
	}


	// to check if a client IP address is either from a local Dev env or Wakefern's
	// Intranet/VPN
	// Mark Covello on 5/27/2020 states the Wakefern IP range is from 65.51.66.0 to
	// 65.51.66.255 as externally facing
	

	public static boolean isAuthorized(String clientIp) {
		boolean isAuthorized = false;

		// determine ipv4 or ipv6
		int dotCount = 0;
		for (int i = 0; i < clientIp.length(); i++) {
			if (clientIp.charAt(i) == '.') {
				dotCount++;
			}
		}

		// 127.0.0.1 for eclipse 
		// 172.17.0.1 for local Docker
		if (clientIp.equalsIgnoreCase("127.0.0.1") || clientIp.equalsIgnoreCase("172.17.0.1")) {
			isAuthorized = true;
		} else if (dotCount == 3) { // ipv4 format
			String[] ipRanges = clientIp.split("\\.");

			/* 
			 * Mark/Joe got these CIDR ranges from the SOC team for the Zscaler implementation
			 * 104.129.192.0/20 
			 * 165.225.0.0/16 
			 * 136.226.0.0/16 
			 * 137.83.128.0/18 
			 * 128.177.125.0/24 
			 * 
			 * These are CIDR (Classless Inter-Domain Routing) at https://www.ipaddressguide.com/cidr
			 */
			
				//165.225.0.0/16
			if ((Integer.valueOf(ipRanges[0]) == 165 && Integer.valueOf(ipRanges[1]) == 225 )
				||
				//136.226.0.0/16 
				((Integer.valueOf(ipRanges[0]) == 136 && Integer.valueOf(ipRanges[1]) == 226 ))
				||
				//128.177.125.0/24
				((Integer.valueOf(ipRanges[0]) == 128 && Integer.valueOf(ipRanges[1]) == 177 && Integer.valueOf(ipRanges[2]) == 125
				&& (Integer.valueOf(ipRanges[3]) >= 0 || (Integer.valueOf(ipRanges[3]) <= 16))))
				||
				//104.129.192.0/20 
				((Integer.valueOf(ipRanges[0]) == 104 && Integer.valueOf(ipRanges[1]) == 129 
				&& (Integer.valueOf(ipRanges[2]) >= 192 || (Integer.valueOf(ipRanges[2]) <= 207))))
				||
				//137.83.128.0/18
				((Integer.valueOf(ipRanges[0]) == 137 && Integer.valueOf(ipRanges[1]) == 83
				&& (Integer.valueOf(ipRanges[2]) >= 128 || (Integer.valueOf(ipRanges[2]) <= 191)))) ) {
					
					isAuthorized = true;
			}
				
		} else { // ipv6 format
			isAuthorized = false;
		}

		logger.info("The client IP of " + clientIp + "'s authorization is: " + isAuthorized);

		return isAuthorized;
	}
	
	/*
	 * A logging utility to determine whether the log4j should log an error exception 
	 * based on the system variables of MUTE_ERROR_LOG and MUTE_HTTP_CODE
	 * 
	 * 
	 */
	public static boolean isLoggable(Exception e) {
		boolean isLoggable = true;
		
		if (EnvManager.isMuteErrorLog()) {
			
			for (String code: EnvManager.getMuteHttpCode()) {
				if (code.trim().equalsIgnoreCase("401")) {
					logger.debug("Mute_http_code 401: " + e.getLocalizedMessage());
					if (e.getLocalizedMessage().contains("401,Unauthorized")) {
						isLoggable = false;
						break;
					}
					
				} else if (code.trim().equalsIgnoreCase("409")) {
					logger.debug("Mute_http_code 409: " + e.getLocalizedMessage());
					if (e.getLocalizedMessage().contains("409,Conflict")) {
						isLoggable = false;
						break;
					}
				}
			}
		}
		
		return isLoggable;
	}
	
}
