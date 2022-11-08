package com.wakefern.global;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ApplicationUtils {
	private final static Logger logger = LogManager.getLogger(ApplicationUtils.class);

	private static String getSystemPropertyValue(String key) {
		return java.lang.System.getenv(key.trim());
	}

	public static String getVcapValue(String vcapKeyName){
    	String vcapValue = "";
    	try {
    		vcapValue = getSystemPropertyValue(vcapKeyName).trim();
    	} catch(Exception e){
    		logger.error("[ApplicationUtils]::getVcapValue::Failed! Exception " + vcapKeyName + e.getMessage());
    	}
    	return vcapValue;
    }

	/**
	 * Method overload for getting environment variable values with a default.
	 * @param name
	 * @param defaultValue
	 * @return
	 */
    public static String getVcapValue(String name, String defaultValue) {
    	try {
    		return getSystemPropertyValue(name).trim();
		} catch (Exception e) {
			logger.error("[ApplicationUtils]::getVcapValue::Failed for key " + name + ". Exception " + e.getMessage());
			return defaultValue;
		}
	}
	
    /**
     * if no AppVersion or AppVersion in request is < majorVersion.minorVersion  (Important note: < is the key )
     * 	then applicable
     * 	else not 
     * @param appVerHeader
     * @return true if applicable, false otherwise.
     */
    public static boolean isReleaseApplicable(String appVerHeader, int majorVersion, int minorVersion) {
    	boolean isApplicable = true;
    	
    	if (appVerHeader != null && !appVerHeader.isEmpty()) {
    		String[] headerVerArr = appVerHeader.split("\\.");
    		int majorVerHeader = Integer.parseInt(headerVerArr[0]);
 
    		if (majorVerHeader > majorVersion) { // header major version
    			isApplicable = false;
    		} else if (majorVerHeader == majorVersion) { // check minor version
    			if (Integer.parseInt(headerVerArr[1]) >= minorVersion) {
    				isApplicable = false;
    			}
    		}
    	}
    	return isApplicable;
    }
}
