package com.wakefern.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.logging.LogUtil;
import com.wakefern.wakefern.WakefernApplicationConstants;

/*
 *  author:     Danny Zheng
 *  date:       10/10/2019
 *  purpose:    to retrieve every VCAP property/value from the IBM Cloud VCAP settings
 */
public class VcapProcessor {
	private final static Logger logger = LogManager.getLogger(VcapProcessor.class);

	private final static String ENV_STAGING = "staging";

	private static int apiHighTimeout = 0;
	private static int apiMediumTimeout = 0;
	private static int apiLowTimeout = 0;

	private static String recipeService = null;
	private static String recipeClientId = null;
	private static String recipeApiKeyStaging = null;
	private static String recipeApiKeyProd = null;
	private static int recipeShelfThreadPoolSize = 0;

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
	
	private static String srMobilePassThruApiKeyProd = null;

	private static String prodxService = null;
	private static String prodxComplementsApiKeyStaging = null;
	private static String prodxComplementsApiKeyProd = null;
	private static String prodxComplementsAisleId = null;

	private static String prodxVariationsApiKeyProd = null;
	
	private static String mi9v8Service = null;
	
	private static String rewardPointService = null;
	
	private static boolean isMuteErrorLog = false;
	private static List<String> muteHttpCode = null;

	private static boolean isItemLocatorCacheEnabled = false;
	
	private static int httpDefaultConnectTimeoutMs = 0;
	private static int httpDefaultReadTimeoutMs = 0;
	
	private static String jwtPublicKey = null;
	

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
			recipeShelfThreadPoolSize = getVcapValueInt(WakefernApplicationConstants.VCAPKeys.RECIPE_SHELF_THREAD_POOL_SIZE);
			if (recipeShelfThreadPoolSize <= 0) {
				throw new IllegalArgumentException(WakefernApplicationConstants.VCAPKeys.RECIPE_SHELF_THREAD_POOL_SIZE + " needs to be a positive integer");
			}
		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("recipe_shelf_thread_pool_size must have a positive integer value");
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

		srMobilePassThruApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.APIM_SRMOBILE_PASSTHRU_PROD_API_KEY);
		
		prodxService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_SERVICE);
		
		prodxComplementsApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_COMPLEMENTS_PROD_API_KEY);
		prodxComplementsApiKeyStaging = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_COMPLEMENTS_STG_API_KEY);
		prodxComplementsAisleId = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_AISLE_ID);
	
		prodxVariationsApiKeyProd = getVcapValueString(WakefernApplicationConstants.VCAPKeys.PRODX_VARIATIONS_PROD_API_KEY);
		
		mi9v8Service = getVcapValueString(WakefernApplicationConstants.VCAPKeys.MI9V8_SERVICE);
		
		rewardPointService = getVcapValueString(WakefernApplicationConstants.VCAPKeys.REWARD_POINT_SERVICE);
		
		if (getVcapValueString(WakefernApplicationConstants.VCAPKeys.MUTE_ERROR_LOG).trim().equalsIgnoreCase("true")) {
			isMuteErrorLog = true;
		}
		
		String codes= getVcapValueString(WakefernApplicationConstants.VCAPKeys.MUTE_HTTP_CODE).trim();
		muteHttpCode = new ArrayList<>(Arrays.asList(codes.split(",")));

		isItemLocatorCacheEnabled = getVcapValueString(WakefernApplicationConstants.VCAPKeys.ITEM_LOCATOR_CACHE_ENABLED).trim().equalsIgnoreCase("true");
		
		try {
			httpDefaultConnectTimeoutMs = getVcapValueInt(WakefernApplicationConstants.VCAPKeys.HTTP_DEFAULT_CONNECT_TIMEOUT);

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("http_default_connect_timeout_ms must have an integer value in millisecond!");
		}
		
		try {
			httpDefaultReadTimeoutMs = getVcapValueInt(WakefernApplicationConstants.VCAPKeys.HTTP_DEFAULT_READ_TIMEOUT);

		} catch (Exception e) {
			logger.error(LogUtil.getRelevantStackTrace(e) + ", the error message: " + LogUtil.getExceptionMessage(e));
			throw new RuntimeException("http_default_read_timeout_ms must have an integer value in millisecond!");
		}
		
		jwtPublicKey = getVcapValueString(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY);
		
		
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

	public static int getRecipeShelfThreadPoolSize() {
		return recipeShelfThreadPoolSize;
	}

	/**
	 * Get vcap value as an integer. Returns 0 if no vcap value is found.
	 * 
	 * @param vcapKeyName the key of the vcap value
	 * @return the vcap value
	 */
	private static int getVcapValueInt(String vcapKeyName) {
		String highTimeObj = ApplicationUtils.getVcapValue(vcapKeyName);
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
		final String value = ApplicationUtils.getVcapValue(key);

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

	/**
	 * Verify if a service is a staging environment or not.
	 * @param service String
	 * @return true if the service is a staging environment, false otherwise
	 */
	public static boolean isServiceStaging(String service) {
		return service != null && service.trim().equalsIgnoreCase(ENV_STAGING);
	}

	public static String getTargetWalletServiceEndpoint() {
		return isServiceStaging(walletService) ?
				WakefernApplicationConstants.Wallet.Upstream.StageBaseURL
				: WakefernApplicationConstants.Wallet.Upstream.ProdBaseURL;
	}

	public static String getTargetWalletAuthorizationKey() {
		return isServiceStaging(walletService) ? srWalletKeyStaging : srWalletKeyProd;
	}

	public static String getUserJwtSecret() {
		return userJwtSecret;
	}

	public static String getTargetRecipeLocaiServiceEndpoint() {
		return isServiceStaging(recipeService) ?
				WakefernApplicationConstants.RecipeLocai.Upstream.stagingBaseURL
				: WakefernApplicationConstants.RecipeLocai.Upstream.prodBaseURL;
	}

	public static String getTargetRecipeLocaiApiKey() {
		return isServiceStaging(recipeService) ? recipeApiKeyStaging : recipeApiKeyProd;
	}

	public static String getTargetSRFHOrdersBaseUrl() {
		return isServiceStaging(srfhOrdersService) ?
				WakefernApplicationConstants.APIM.apimDevBaseURL
				: WakefernApplicationConstants.APIM.apimBaseURL;
	}

	public static String getTargetSRFHOrdersApiKey() {
		return isServiceStaging(srfhOrdersService) ? srfhOrdersApiKeyStaging : srfhOrdersApiKeyProd;
	}

	public static String getTargetSRFHCurbsideBaseUrl() {
		return isServiceStaging(srfhCurbsideService) ?
				WakefernApplicationConstants.APIM.apimDevBaseURL
				: WakefernApplicationConstants.APIM.apimBaseURL;
	}

	public static String getTargetSRFHCurbsideApiKey() {
		return isServiceStaging(srfhCurbsideService) ? srfhCurbsideApiKeyStaging : srfhCurbsideApiKeyProd;
	}

	public static String getSrfhOrdersService() {
		return srfhOrdersService;
	}

	public static String getSrfhCurbsideService() {
		return srfhCurbsideService;
  }

	public static String getProdxService() {
		return prodxService;
	}

	public static String getProdxServiceEndpoint() {
		return isServiceStaging(prodxService) ?
				WakefernApplicationConstants.Prodx.Upstream.stagingBaseUrl
				: WakefernApplicationConstants.Prodx.Upstream.prodBaseUrl;
	}

	public static String getProdxComplementsApiKey() {
		return isServiceStaging(prodxService) ? prodxComplementsApiKeyStaging : prodxComplementsApiKeyProd;
	}

	public static String getMi9v8Service() {
		return mi9v8Service;
	}

	public static String getProdxComplementsAisleId() { return prodxComplementsAisleId; }

	public static String getRewardPointService() {
		return rewardPointService;
	}

	public static List<String> getMuteHttpCode() {
		return muteHttpCode;
	}

	public static boolean isMuteErrorLog() {
		return isMuteErrorLog;
	}

	public static void setMuteErrorLog(boolean isMuteErrorLog) {
		VcapProcessor.isMuteErrorLog = isMuteErrorLog;
	}

	public static void setMuteHttpCode(List<String> muteHttpCode) {
		VcapProcessor.muteHttpCode = muteHttpCode;
	}

	public static boolean isItemLocatorCacheEnabled() { return isItemLocatorCacheEnabled; }

	public static int getHttpDefaultConnectTimeoutMs() {
		return httpDefaultConnectTimeoutMs;
	}

	public static int getHttpDefaultReadTimeoutMs() {
		return httpDefaultReadTimeoutMs;
	}

	public static String getJwtPublicKey() {
		return jwtPublicKey;
	}

	public static String getProdxVariationsApiKeyProd() {
		return prodxVariationsApiKeyProd;
	}

	public static String getSrMobilePassThruApiKeyProd() {
		return srMobilePassThruApiKeyProd;
	}
	
}
