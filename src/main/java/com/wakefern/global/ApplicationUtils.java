package com.wakefern.global;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ApplicationUtils {
    private final static Logger logger = LogManager.getLogger(ApplicationUtils.class);

    private static String getEnv(String key) {
        return java.lang.System.getenv(key.trim()).trim();
    }

    public static String getEnvValue(String key) {
        String value = "";
        try {
            value = getEnv(key);
        } catch (Exception e) {
            logger.error("[ApplicationUtils]::getEnvValue::Failed! Exception for key: " + key
                    + " message: " + e.getMessage());
        }
        return value;
    }

    /**
     * Method overload for getting environment variable values with a default.
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public static String getEnvValue(String key, String defaultValue) {
        try {
            return getEnv(key);
        } catch (Exception e) {
            logger.error("[ApplicationUtils]::getEnvValue::Failed for key " + key + ". Exception " + e.getMessage());
            return defaultValue;
        }
    }
}
