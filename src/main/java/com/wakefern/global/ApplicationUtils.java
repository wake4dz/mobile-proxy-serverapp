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
}
