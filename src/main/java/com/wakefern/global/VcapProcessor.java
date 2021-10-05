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

	private static String srfhOrdersService = null;
	private static String srfhCurbsideService = null;
	private static String srfhOrdersApiKeyStaging = null;
	private static String srfhOrdersApiKeyProd = null;
	private static String srfhCurbsideApiKeyStaging = null;
	private static String srfhCurbsideApiKeyProd = null;
	
	private static String citrusService = null;
	private static String citrusCatalogIdStaging = null;
	private static String citrusContentStandardIdStaging = null;
	private static String citrusApiKeyStaging = null;
	private static String citrusCatalogIdProd = null;
	private static String citrusContentStandardIdProd = null;
	private static String citrusApiKeyProd = null;

	private static String push2DeviceService = null;
	private static String push2DeviceApiKeyStaging = null;
	private static String push2DeviceApiKeyProd = null;

	private static String prodxService = null;
	private static String prodxApiKeyStaging = null;
	private static String prodxApiKeyProd = null;
	private static String prodxAisleId = null;

	private static String mi9v8Service = null;
	
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

		srfhOrdersService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SRFH_ORDERS_SERVICE);
		srfhOrdersApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SRFH_ORDERS_STG_API_KEY);
		srfhOrdersApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SRFH_ORDERS_PROD_API_KEY);

		srfhCurbsideService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SRFH_CURBSIDE_SERVICE);
		srfhCurbsideApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SRFH_CURBSIDE_STG_API_KEY);
		srfhCurbsideApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.SRFH_CURBSIDE_PROD_API_KEY);

		citrusService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_SERVICE);
		citrusCatalogIdStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_STG_CATALOG_ID);
		citrusContentStandardIdStaging = getVcapValueString(
				WakefernApplicationConstants.VCAPKeys.CITRUS_STG_CONTENT_STANDARD_ID);
		citrusApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_STG_KEY);
		citrusCatalogIdProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_PROD_CATALOG_ID);
		citrusContentStandardIdProd = getVcapValueString(
				WakefernApplicationConstants.VCAPKeys.CITRUS_PROD_CONTENT_STANDARD_ID);
		citrusApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.CITRUS_PROD_KEY);

		push2DeviceService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PUSH2DEVICE_SERVICE);
		push2DeviceApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PUSH2DEVICE_STG_API_KEY);
		push2DeviceApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PUSH2DEVICE_PROD_API_KEY);

		prodxService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_SERVICE);
		prodxApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_COMPLEMENTS_PROD_API_KEY);
		prodxApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_COMPLEMENTS_STG_API_KEY);
		prodxAisleId = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_AISLE_ID);
	
		mi9v8Service = getVcapValueString(WakefernApplicationConstants.VCAPKeys.MI9V8_SERVICE);

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
		return highTimeObj != null && !highTimeObj.trim().isEmpty() ? Integer.parseInt(highTimeObj.trim()) : 0;
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

	public static String getTargetSRFHOrdersBaseUrl() {
		if (srfhOrdersService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.APIM.apimDevBaseURL;
		} else {
			return WakefernApplicationConstants.APIM.apimBaseURL;
		}
	}

	public static String getTargetSRFHOrdersApiKey() {
		if (srfhOrdersService.trim().equalsIgnoreCase("staging")) {
			return srfhOrdersApiKeyStaging;
		} else {
			return srfhOrdersApiKeyProd;
		}
	}

	public static String getTargetSRFHCurbsideBaseUrl() {
		if (srfhCurbsideService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.APIM.apimDevBaseURL;
		} else {
			return WakefernApplicationConstants.APIM.apimBaseURL;
		}
	}

	public static String getTargetSRFHCurbsideApiKey() {
		if (srfhCurbsideService.trim().equalsIgnoreCase("staging")) {
			return srfhCurbsideApiKeyStaging;
		} else {
			return srfhCurbsideApiKeyProd;
		}
	}

	public static String getSrfhOrdersService() {
		return srfhOrdersService;
	}

	public static String getSrfhCurbsideService() {
		return srfhCurbsideService;
  }
  
	public static int getTimeslotSearchRadiusInMile() {
		return timeslotSearchRadiusInMile;
	}

	public static String getCitrusService() {
		return citrusService;
	}

	public static String getCitrusServiceEndpoint() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.CitrusAds.Upstream.stagingBaseUrl;
		} else {
			return WakefernApplicationConstants.CitrusAds.Upstream.prodBaseUrl;
		}
	}

	public static String getCitrusApiKey() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return citrusApiKeyStaging;
		} else {
			return citrusApiKeyProd;
		}
	}

	public static String getCitrusCatalogId() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return citrusCatalogIdStaging;
		} else {
			return citrusCatalogIdProd;
		}
	}

	public static String getCitrusContentStandardId() {
		if (citrusService.trim().equalsIgnoreCase("staging")) {
			return citrusContentStandardIdStaging;
		} else {
			return citrusContentStandardIdProd;
		}
	}

	/**
	 * Get the version of the push2device service (staging, production)
	 * @return String
	 */
	public static String getPush2DeviceService() {
		return push2DeviceService;
	}

	/**
	 * Fetch the client secret for Push2Device service
	 * @return String
	 */
	public static String getPush2DeviceApiKey() {
		if (push2DeviceService.trim().equalsIgnoreCase("staging")) {
			return push2DeviceApiKeyStaging;
		} else {
			return push2DeviceApiKeyProd;
		}
	}

	/**
	 * Get the base url for the push2device service.
	 * @return
	 */
	public static String getPush2DeviceEndpoint() {
		if (push2DeviceService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.Push2Device.Upstream.stagingBaseUrl;
		} else {
			return WakefernApplicationConstants.Push2Device.Upstream.prodBaseUrl;
		}
	}

	public static String getProdxService() {
		return prodxService;
	}

	public static String getProdxServiceEndpoint() {
		if (prodxService.trim().equalsIgnoreCase("staging")) {
			return WakefernApplicationConstants.Prodx.Complements.Upstream.stagingBaseUrl;
		}
		return WakefernApplicationConstants.Prodx.Complements.Upstream.prodBaseUrl;
	}

	public static String getProdxApiKey() {
		if (prodxService.trim().equalsIgnoreCase("staging")) {
			return prodxApiKeyStaging;
		}
		return prodxApiKeyProd;
	}

	public static String getMi9v8Service() {
		return mi9v8Service;
	}

	public static String getProdxAisleId() { return prodxAisleId; }

}
