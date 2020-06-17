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
	
	private static String recipeService = null;
	private static String recipeClientId = null;
	private static String recipeApiKeyStaging = null;
	private static String recipeApiKeyProd = null;
	
	private static String walletService = null;
	private static String srWalletKeyProd = null;
	private static String srWalletKeyStaging = null;

	private static String userJwtSecret = null;
	
	//this static code is not run until the class is loaded into the memory for the first time
	//system settings are fetched once, store them in the heap memory for quick access
	static {  
		try {
			apiHighTimeout = getVcapValue(WakefernApplicationConstants.VCAPKeys.API_HIGH_TIMEOUT);

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("api_high_timeout must have an integer value in millisecond!");
		}

		try {
			apiMediumTimeout = getVcapValue(WakefernApplicationConstants.VCAPKeys.API_MEDIUM_TIMEOUT);
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("api_medium_timeout must have an integer value in millisecond!");
		}

		try {
			apiLowTimeout = getVcapValue(WakefernApplicationConstants.VCAPKeys.API_LOW_TIMEOUT);
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("api_low_timeout must have an integer value in milliseconds");
		}
	
		recipeService = MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.RECIPE_SERVICE);
		if ((recipeService == null) || (recipeService.trim().length() == 0)) {	
			throw new RuntimeException("recipe_service must have a non-empty value");
		}

		recipeClientId = MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.RECIPE_CLIENT_ID_KEY);
		if ((recipeClientId == null) || (recipeClientId.trim().length() == 0)) {
			throw new RuntimeException("recipe_client_id_key must have a non-empty value");
		}

		recipeApiKeyStaging = MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.RECIPE_API_STAGE_KEY);
		if (( recipeApiKeyStaging == null) || ( recipeApiKeyStaging.trim().length() == 0)) {
			throw new RuntimeException("recipe_api_stage_key must have a non-empty value");
		}

		recipeApiKeyProd = MWGApplicationConstants.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.RECIPE_API_PROD_KEY);
		if ((recipeApiKeyProd == null) || (recipeApiKeyProd.trim().length() == 0)) {
			throw new RuntimeException("recipe_api_prod_key must have a non-empty value");
		}

		walletService = MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.WALLET_SERVICE);
		if ((walletService == null) || (walletService.trim().length() == 0)) {
			throw new RuntimeException("wallet_service must have a non-empty value");
		}

		srWalletKeyProd = MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.SR_WALLET_PROD_KEY);
		if ((srWalletKeyProd == null) || (srWalletKeyProd.trim().length() == 0)) {
			throw new RuntimeException("sr_wallet_prod_key must have a non-empty value");
		}

		srWalletKeyStaging = MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.SR_WALLET_STAGE_KEY);
		if ((srWalletKeyStaging == null) || (srWalletKeyStaging.trim().length() == 0)) {
			throw new RuntimeException("sr_wallet_stage_key must have a non-empty value");
		}

		userJwtSecret = MWGApplicationConstants
				.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.USER_JWT_SECRET);
		if ((userJwtSecret == null) || (userJwtSecret.trim().isEmpty())) {
			throw new RuntimeException(
					WakefernApplicationConstants.VCAPKeys.USER_JWT_SECRET + " must have a non-empty value");
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

	public static String getRecipeService() {
		return recipeService;
	}

	public static String getRecipeClientId() {
		return recipeClientId;
	}

	public static String getRecipeApiKeyStaging() {
		return recipeApiKeyStaging;
	}

	public static void setRecipeApiKeyStaging(String recipeApiKeyStaging) {
		VcapProcessor.recipeApiKeyStaging = recipeApiKeyStaging;
	}

	public static String getRecipeApiKeyProd() {
		return recipeApiKeyProd;
	}

	/**
	 * get vcap value for api time out, return 0 if no vcap value found.
	 * 
	 * @param vcapKeyName
	 * @return
	 */
	private static int getVcapValue(String vcapKeyName) throws Exception {
		String highTimeObj = MWGApplicationConstants.getSystemPropertyValue(vcapKeyName);
		return highTimeObj != null && !highTimeObj.trim().isEmpty() ? Integer.valueOf(highTimeObj.trim()) : 0;
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

	public static String getUserJwtSecret() {
		return userJwtSecret;
	}

	public static String getTargetRecipeLocaiServiceEndpoint() {
		if (recipeService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.RecipeLocai.Upstream.stagingBaseURL;
		} else {
			return WakefernApplicationConstants.RecipeLocai.Upstream.prodBaseURL;
		}
	}
		
	
	public static String getTargetRecipeLocaiApiKey() {
		if (recipeService.trim().equalsIgnoreCase("staging")) {
			return recipeApiKeyStaging;
		} else {
			return recipeApiKeyProd;
		}
	}

}
