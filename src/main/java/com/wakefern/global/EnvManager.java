package com.wakefern.global;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.wakefern.WakefernApplicationConstants;

/*
 *  author:     Danny Zheng
 *  date:       10/10/2019
 *  purpose:    To retrieve and manage environment configuration
 */
public class EnvManager {
	private final static Logger logger = LogManager.getLogger(EnvManager.class);

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
	private static String walletKeyProd = null;
	private static String walletKeyStaging = null;

	private static String userJwtSecret = null;

	private static String srfhOrdersService = null;
	private static String srfhCurbsideService = null;
	private static String srfhOrdersApiKeyStaging = null;
	private static String srfhOrdersApiKeyProd = null;
	private static String srfhCurbsideApiKeyStaging = null;
	private static String srfhCurbsideApiKeyProd = null;
	
	private static String mobilePassThruApiKeyProd = null;

	private static String prodxService = null;
	private static String prodxComplementsApiKeyStaging = null;
	private static String prodxComplementsApiKeyProd = null;
	private static String prodxComplementsAisleId = null;

	private static String prodxVariationsApiKeyProd = null;

	private static String push2deviceApiKeyStaging = null;
	private static String push2deviceApiKeyProd = null;
	private static String push2deviceService = null;
	private static String push2deviceStagingUrl = null;
	private static String push2deviceProdUrl = null;
	
	private static String mi9v8Service = null;
	
	private static String rewardPointService = null;
	
	private static boolean isMuteErrorLog = false;
	private static List<String> muteHttpCode = null;

	private static boolean isItemLocatorCacheEnabled = false;
	
	private static int httpDefaultConnectTimeoutMs = 0;
	private static int httpDefaultReadTimeoutMs = 0;
	
	private static String jwtPublicKey = null;
	
	private static String banner = null;
	
	private static String rewardPointKey = null;
	private static String apimNutritionKey = null;
	
	private static String apimSmsEnrollmentKey = null;
	
	private static String proxyAdminKey = null;
	
	private static String couponService = null;
	private static String couponV3Key = null;

	/**
	 * Get an environment variable value
	 * @param key
	 * @return
	 * @throws RuntimeException
	 */
    private static String getEnvValue(String key) throws RuntimeException {
        String value = null;
        try {
        	value = java.lang.System.getenv(key.trim()).trim();
        } catch (Exception e) {
            logger.error("[EnvManager]::getEnvValue::Failed! Exception for key: " + key
                    + " message: " + e.getMessage());
            throw new RuntimeException("Exception for key: " + key  + " message: " + e.getMessage());
        }
        return value;
    }
    
	/**
	 * Get env value as an integer. 
	 *
	 * @param key the key of the environment variable
	 * @return the value, if found, for the environment variable
	 */
	public static int getEnvValueInt(String key) throws RuntimeException {
		String intObj = getEnvValue(key);
		
		if ((intObj == null) || (intObj.trim().length() == 0) || (Integer.parseInt(intObj.trim()) <= 0)) {
			throw new RuntimeException(key + " must have a positive integer");
		}
		
		return Integer.parseInt(intObj.trim());
	}

	/**
	 * Get env value as a string if it exists. Throws a RuntimeException otherwise.
	 * 
	 * @param key the key of the environment variable
	 * @return the value, if found, for the environment variable
	 * @throws RuntimeException
	 */
	public static String getEnvValueString(String key) throws RuntimeException {
		final String value = getEnvValue(key);

		if ((value == null) || (value.trim().length() == 0)) {
			throw new RuntimeException(key + " must have a non-empty value");
		}

		return value;
	}

	
	// this static code is not run until the class is loaded into the memory for the
	// first time system settings are fetched once, store them in the heap memory
	// for quick access
	static {
	
		apiHighTimeout = getEnvValueInt(WakefernApplicationConstants.EnvVarsKeys.API_HIGH_TIMEOUT);
		apiMediumTimeout = getEnvValueInt(WakefernApplicationConstants.EnvVarsKeys.API_MEDIUM_TIMEOUT);
		apiLowTimeout = getEnvValueInt(WakefernApplicationConstants.EnvVarsKeys.API_LOW_TIMEOUT);

		recipeShelfThreadPoolSize = getEnvValueInt(
				WakefernApplicationConstants.EnvVarsKeys.RECIPE_SHELF_THREAD_POOL_SIZE);

		httpDefaultConnectTimeoutMs = getEnvValueInt(
				WakefernApplicationConstants.EnvVarsKeys.HTTP_DEFAULT_CONNECT_TIMEOUT);
		httpDefaultReadTimeoutMs = getEnvValueInt(WakefernApplicationConstants.EnvVarsKeys.HTTP_DEFAULT_READ_TIMEOUT);

		if (getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.MUTE_ERROR_LOG).trim()
				.equalsIgnoreCase("true")) {
			isMuteErrorLog = true;
		}

		String codes = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.MUTE_HTTP_CODE).trim();
		muteHttpCode = new ArrayList<>(Arrays.asList(codes.split(",")));

		isItemLocatorCacheEnabled = getEnvValueString(
				WakefernApplicationConstants.EnvVarsKeys.ITEM_LOCATOR_CACHE_ENABLED).trim().equalsIgnoreCase("true");

		recipeService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.RECIPE_SERVICE);
		recipeClientId = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.RECIPE_CLIENT_ID_KEY);
		recipeApiKeyStaging = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.RECIPE_API_STAGE_KEY);
		recipeApiKeyProd = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.RECIPE_API_PROD_KEY);

		walletService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.WALLET_SERVICE);
		walletKeyProd = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.WALLET_PROD_KEY);
		walletKeyStaging = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.WALLET_STAGE_KEY);

		userJwtSecret = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.USER_JWT_SECRET);

		srfhOrdersService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.SRFH_ORDERS_SERVICE);
		srfhOrdersApiKeyStaging = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.SRFH_ORDERS_STG_API_KEY);
		srfhOrdersApiKeyProd = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.SRFH_ORDERS_PROD_API_KEY);

		srfhCurbsideService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.SRFH_CURBSIDE_SERVICE);
		srfhCurbsideApiKeyStaging = getEnvValueString(
				WakefernApplicationConstants.EnvVarsKeys.SRFH_CURBSIDE_STG_API_KEY);
		srfhCurbsideApiKeyProd = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.SRFH_CURBSIDE_PROD_API_KEY);

		mobilePassThruApiKeyProd = getEnvValueString(
				WakefernApplicationConstants.EnvVarsKeys.APIM_SRMOBILE_PASSTHRU_PROD_API_KEY);

		prodxService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PRODX_SERVICE);

		prodxComplementsApiKeyProd = getEnvValueString(
				WakefernApplicationConstants.EnvVarsKeys.PRODX_COMPLEMENTS_PROD_API_KEY);
		prodxComplementsApiKeyStaging = getEnvValueString(
				WakefernApplicationConstants.EnvVarsKeys.PRODX_COMPLEMENTS_STG_API_KEY);
		prodxComplementsAisleId = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PRODX_AISLE_ID);

		prodxVariationsApiKeyProd = getEnvValueString(
				WakefernApplicationConstants.EnvVarsKeys.PRODX_VARIATIONS_PROD_API_KEY);

		push2deviceService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PUSH2DEVICE_SERVICE);
		push2deviceApiKeyProd = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PUSH2DEVICE_PROD_API_KEY);
		push2deviceApiKeyStaging = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PUSH2DEVICE_STG_API_KEY);
		push2deviceProdUrl = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PUSH2DEVICE_PROD_URL);
		push2deviceStagingUrl = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PUSH2DEVICE_STG_URL);

		mi9v8Service = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.MI9V8_SERVICE);

		rewardPointService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.REWARD_POINT_SERVICE);

		jwtPublicKey = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.JWT_PUBLIC_KEY);

		banner = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.BANNER);

		rewardPointKey = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.REWARD_POINT_KEY);

		apimNutritionKey = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.APIM_NUTRITION_KEY);

		apimSmsEnrollmentKey = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.APIM_SMS_ENROLLMENTS_KEY);

		proxyAdminKey = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.PROXY_ADMIN_KEY);

		couponService = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.COUPON_SERVICE);

		couponV3Key = getEnvValueString(WakefernApplicationConstants.EnvVarsKeys.COUPON_V3_KEY);
			
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
		EnvManager.recipeApiKeyStaging = recipeApiKeyStaging;
	}

	public static String getRecipeApiKeyProd() {
		return recipeApiKeyProd;
	}

	public static int getRecipeShelfThreadPoolSize() {
		return recipeShelfThreadPoolSize;
	}

	public static String getWalletService() {
		return walletService;
	}

	public static String getWalletKeyProd() {
		return walletKeyProd;
	}

	public static String getWalletKeyStaging() {
		return walletKeyStaging;
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
		return isServiceStaging(walletService) ? walletKeyStaging : walletKeyProd;
	}

	public static String getUserJwtSecret() {
		return userJwtSecret;
	}

    /**
     * returns either coupon production or staging url endpoint
     * @return
     */
    public static String getTargetCouponV3ServiceEndpoint() {
        return isServiceStaging(couponService) ?
                WakefernApplicationConstants.CouponsV3.baseURL_staging 
                : WakefernApplicationConstants.CouponsV3.baseURL;

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

	public static String getTargetProdxServiceEndpoint() {
		return isServiceStaging(prodxService) ?
				WakefernApplicationConstants.Prodx.Upstream.stagingBaseUrl
				: WakefernApplicationConstants.Prodx.Upstream.prodBaseUrl;
	}

	public static String getTargetProdxComplementsApiKey() {
		return isServiceStaging(prodxService) ? prodxComplementsApiKeyStaging : prodxComplementsApiKeyProd;
	}

	public static String getMi9v8Service() {
		return mi9v8Service;
	}

	public static String getProdxComplementsAisleId() { return prodxComplementsAisleId; }

	public static String getRewardPointService() {
		return rewardPointService;
	}
	
	public static String getTargetRewardServiceEndpoint() {
		return isServiceStaging(rewardPointService) ?
			   WakefernApplicationConstants.RewardPoint.Upstream.baseStagingURL
				: WakefernApplicationConstants.RewardPoint.Upstream.baseURL;
	}

	public static List<String> getMuteHttpCode() {
		return muteHttpCode;
	}

	public static boolean isMuteErrorLog() {
		return isMuteErrorLog;
	}

	public static void setMuteErrorLog(boolean isMuteErrorLog) {
		EnvManager.isMuteErrorLog = isMuteErrorLog;
	}

	public static void setMuteHttpCode(List<String> muteHttpCode) {
		EnvManager.muteHttpCode = muteHttpCode;
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

	public static String getMobilePassThruApiKeyProd() {
		return mobilePassThruApiKeyProd;
	}

	public static String getTargetPush2DeviceApiKey() {
		return isServiceStaging(push2deviceService) ? push2deviceApiKeyStaging : push2deviceApiKeyProd;
	}

	public static String getTargetPush2DeviceUrl() {
		return isServiceStaging(push2deviceService)
				? push2deviceStagingUrl
				: push2deviceProdUrl;
	}

	public static String getPush2deviceApiKeyStaging() {
		return push2deviceApiKeyStaging;
	}

	public static String getPush2deviceApiKeyProd() {
		return push2deviceApiKeyProd;
	}

	public static String getPush2DeviceService() {
		return push2deviceService;
	}

	
	public static String getPush2deviceStagingUrl() {
		return push2deviceStagingUrl;
	}

	public static String getPush2deviceProdUrl() {
		return push2deviceProdUrl;
	}

	public static String getBanner() {
		return banner;
	}

	public static String getRewardPointKey() {
		return rewardPointKey;
	}

	public static String getApimNutritionKey() {
		return apimNutritionKey;
	}

	public static String getApimSmsEnrollmentKey() {
		return apimSmsEnrollmentKey;
	}

	public static String getProxyAdminKey() {
		return proxyAdminKey;
	}

	public static String getCouponService() {
		return couponService;
	}

	public static String getCouponV3Key() {
		return couponV3Key;
	}

	public static String getProdxComplementsApiKeyStaging() {
		return prodxComplementsApiKeyStaging;
	}

	public static String getProdxComplementsApiKeyProd() {
		return prodxComplementsApiKeyProd;
	}

	public static String getSrfhOrdersApiKeyStaging() {
		return srfhOrdersApiKeyStaging;
	}

	public static String getSrfhOrdersApiKeyProd() {
		return srfhOrdersApiKeyProd;
	}

	public static String getSrfhCurbsideApiKeyStaging() {
		return srfhCurbsideApiKeyStaging;
	}

	public static String getSrfhCurbsideApiKeyProd() {
		return srfhCurbsideApiKeyProd;
	}

}
