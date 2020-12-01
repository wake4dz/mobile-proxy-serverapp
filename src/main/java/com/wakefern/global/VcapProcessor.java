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

	private static String citrusService = null;
	private static String citrusCatalogIdStaging = null;
	private static String citrusContentStandardIdStaging = null;
	private static String citrusApiKeyStaging = null;

	private static int timeslotSearchRadiusInMile = 0;

	// this static code is not run until the class is loaded into the memory for the
	// first time system settings are fetched once, store them in the heap memory
	// for quick access
	static {
		try {
			apiHighTimeout = getVcapValueInt(WakefernApplicationConstants.VCAPKeys.API_HIGH_TIMEOUT);

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("api_high_timeout must have an integer value in millisecond!");
		}

		try {
			apiMediumTimeout = getVcapValueInt(WakefernApplicationConstants.VCAPKeys.API_MEDIUM_TIMEOUT);
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("api_medium_timeout must have an integer value in millisecond!");
		}

		try {
			apiLowTimeout = getVcapValueInt(WakefernApplicationConstants.VCAPKeys.API_LOW_TIMEOUT);
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("api_low_timeout must have an integer value in milliseconds");
		}

		try {
			timeslotSearchRadiusInMile = getVcapValueInt(
					WakefernApplicationConstants.VCAPKeys.TIMEOUT_SEARCH_RADIUS_IN_MILE);

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("timeslotSearchRadiusInMile must have an integer value in mile!");
		}

		recipeService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.RECIPE_SERVICE);
		recipeClientId = getVcapValueString(WakefernApplicationConstants.VCAPKeys.RECIPE_CLIENT_ID_KEY);
		recipeApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.RECIPE_API_STAGE_KEY);
		recipeApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.RECIPE_API_PROD_KEY);

		walletService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.WALLET_SERVICE);
		srWalletKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SR_WALLET_PROD_KEY);
		srWalletKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SR_WALLET_STAGE_KEY);

		userJwtSecret = getVcapValueString(WakefernApplicationConstants.VCAPKeys.USER_JWT_SECRET);

		citrusService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_SERVICE);
		citrusCatalogIdStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_STG_CATALOG_ID);
		citrusContentStandardIdStaging = getVcapValueString(
				WakefernApplicationConstants.VCAPKeys.CITRUS_STG_CONTENT_STANDARD_ID);
		citrusApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_STG_KEY);
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
	 * Get vcap value as an integer. Returns 0 if no vcap value is found.
	 * 
	 * @param vcapKeyName the key of the vcap value
	 * @return the vcap value
	 */
	private static int getVcapValueInt(String vcapKeyName) throws Exception {
		String highTimeObj = MWGApplicationConstants.getSystemPropertyValue(vcapKeyName);
		return highTimeObj != null && !highTimeObj.trim().isEmpty() ? Integer.valueOf(highTimeObj.trim()) : 0;
	}

	/**
	 * Get vcap value as a string if it exists. Throws a RuntimeException otherwise.
	 * 
	 * @param key the key of the vcap value
	 * @return the vcap value
	 * @throws RuntimeException
	 */
	private static String getVcapValueString(String key) throws RuntimeException {
		final String value = MWGApplicationConstants.getSystemPropertyValue(key);

		if ((value == null) || (value.trim().length() == 0)) {
			throw new RuntimeException(key + " must have a non-empty value");
		}

		return value;
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

	public static int getTimeslotSearchRadiusInMile() {
		return timeslotSearchRadiusInMile;
	}
	
	public static String getCitrusService() {
		return citrusService;
	}

	public static String getCitrusApiKey() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return citrusApiKeyStaging;
		} else {
			return citrusApiKeyStaging; // TODO: prod value
		}
	}

	public static String getCitrusCatalogId() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return citrusCatalogIdStaging;
		} else {
			return citrusCatalogIdStaging; // TODO: prod value
		}
	}

	public static String getCitrusContentStandardId() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return citrusContentStandardIdStaging;
		} else {
			return citrusContentStandardIdStaging; // TODO: prod value
		}
	}
}
