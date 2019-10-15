package com.wakefern.global;

import org.apache.log4j.Logger;

import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

/*
 *  author:     Danny Zheng
 *  date:       10/10/2019
 *  purpose:    to retrieve every VCAP property/value from the IBM Cloud VCAP settings
 */
public class VcapProcessor {
	private final static Logger logger = Logger.getLogger(VcapProcessor.class);

	private static int apiHighTimeout = 0;
	private static int apiMediumTimeout = 0;
	private static int apiLowTimeout = 0;
	
	//this static code is not run until the class is loaded into the memory for the first time
	static {  
		try {
			apiHighTimeout = getVcapValue("api_high_timeout");
			
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));	
			throw new RuntimeException("api_high_timeout must have an integer value in millisecond!");
		}
		
		
		try {
			apiMediumTimeout = getVcapValue("api_medium_timeout");

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));	
			throw new RuntimeException("api_medium_timeout must have an integer value in millisecond!");
		}
		
		try {
			apiLowTimeout = getVcapValue("api_low_timeout");
			
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));	
			throw new RuntimeException("api_low_timeout must have an integer value in milliseconds");
		}
		
	}
	
	public static int getApiHighTimeout() {
		return apiHighTimeout;
	}
	
	public static int getApiMediumTimeout() {
		return apiMediumTimeout;
	}
	
	public static int getApiLowTimeout() {
		return apiLowTimeout;
	}
	
	/**
	 * get vcap value for api time out, return 0 if no vcap value found.
	 * @param vcapKeyName
	 * @return
	 */
	private static int getVcapValue(String vcapKeyName) throws Exception{
		Object highTimeObj = MWGApplicationConstants.getSystemProperytyValue(vcapKeyName);
		return highTimeObj !=null && !((String)highTimeObj).isEmpty() ? Integer.valueOf(MWGApplicationConstants.getSystemProperytyValue(vcapKeyName).trim()) : 0;
	}
	
	
}
