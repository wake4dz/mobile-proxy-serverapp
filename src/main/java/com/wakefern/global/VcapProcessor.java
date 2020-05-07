package com.wakefern.global;

import org.apache.log4j.Logger;

import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

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
	
	private static String walletService = null;
	private static String srWalletKeyProd = null;
	private static String srWalletKeyStaging = null;
	
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
		
		walletService = MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.WALLET_SERVICE);
		if ((walletService == null) || (walletService.trim().length() == 0)) {
			throw new RuntimeException("wallet_service must have a non-empty value");
		}
	
		srWalletKeyProd = MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.SR_WALLET_PROD_KEY);
		if ((srWalletKeyProd == null) || (srWalletKeyProd.trim().length() == 0)) {
			throw new RuntimeException("sr_wallet_prod_key must have a non-empty value");
		}

		srWalletKeyStaging = MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.SR_WALLET_STAGE_KEY);
		if ((srWalletKeyStaging == null) || (srWalletKeyStaging.trim().length() == 0)) {
			throw new RuntimeException("sr_wallet_stage_key must have a non-empty value");
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
		String highTimeObj = MWGApplicationConstants.getSystemProperytyValue(vcapKeyName);
		return highTimeObj !=null && !highTimeObj.trim().isEmpty() ? Integer.valueOf(highTimeObj.trim()) : 0;
	}

	public static String getWalletService() {
		return walletService;
	}

	public static String getSrWalletKeyProd() {
		return srWalletKeyProd;
	}

	public static String getSrWalletKeyStaging() {
		return srWalletKeyStaging;
	}
	

	public static String getTargetWalletServiceEndpoint() {
		if (walletService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.Wallet.Upstream.StageBaseURL;
		} else {
			return WakefernApplicationConstants.Wallet.Upstream.ProdBaseURL;
		}
	}
		
	public static String getTargetWalletAuthorizationKey() {
		if (walletService.trim().equalsIgnoreCase("staging")) {
			return srWalletKeyStaging;
		} else {
			return srWalletKeyProd;
		}
	}
	
}
