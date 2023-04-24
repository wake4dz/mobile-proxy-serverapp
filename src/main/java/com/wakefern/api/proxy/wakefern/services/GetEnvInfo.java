package com.wakefern.api.proxy.wakefern.services;

import javax.ws.rs.GET;

import javax.ws.rs.Path;

import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.EnvManager;
import com.wakefern.global.annotations.ValidateAdminToken;
/**
 * 
 * @author Danny Zheng
 * @date   2023-03-20
 * 
 * Provide a safe way to see all the Envs key/value + its associated target info if any
 *
 */
@Path(ApplicationConstants.Requests.Proxy + ApplicationConstants.Env.Envs)
public class GetEnvInfo {
	final static Logger logger = LogManager.getLogger(GetEnvInfo.class);
	private static final int FORMAT_RIGHT_PADDING = 40;
	
	@GET
	@ValidateAdminToken
	public Response getEnvInfo() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("     ***  Proxy Env Variables Key/Value  ***\n");
		
		sb.append(getEnv("banner", EnvManager.getBanner()) +"\n");
		
		sb.append(getEnv("api_high_timeout", String.valueOf(EnvManager.getApiHighTimeout())));
		sb.append(getEnv("api_medium_timeout", String.valueOf(EnvManager.getApiMediumTimeout())));
		sb.append(getEnv("api_low_timeout", String.valueOf(EnvManager.getApiLowTimeout())) + "\n");
		
		sb.append(getEnv("http_default_connect_timeout_ms", String.valueOf(EnvManager.getHttpDefaultConnectTimeoutMs())));
		sb.append(getEnv("http_default_read_timeout_ms", String.valueOf(EnvManager.getHttpDefaultReadTimeoutMs())) + "\n");
		
		sb.append(getEnv("mute_error_log", String.valueOf(EnvManager.isMuteErrorLog())));
		sb.append(getEnv("mute_http_code", StringUtils.join(EnvManager.getMuteHttpCode(), ",")) + "\n");

		sb.append(getEnv("item_locator_cache_enabled", String.valueOf(EnvManager.isItemLocatorCacheEnabled())));
		sb.append(getEnv("item_locator_partition_size", String.valueOf(EnvManager.getItemLocatorPartitionSize())) + "\n");
		
		sb.append(getEnv("mobilepassthru_prd_api_key", hideKeyValue(EnvManager.getMobilePassThruApiKeyProd())) +"\n");
		
		sb.append(getEnv("proxy_admin_key", hideKeyValue(EnvManager.getProxyAdminKey())) +"\n");
		
		sb.append(getEnv("jwt_public_key", hideKeyValue(EnvManager.getJwtPublicKey())));
		sb.append(getEnv("user_jwt_secret", hideKeyValue(EnvManager.getUserJwtSecret())) +"\n");
		
		sb.append(getEnv("mi9v8_service", EnvManager.getMi9v8Service()));
		sb.append(getEnv("target mi9v8 service endpoint", EnvManager.getTargetMi9v8ServiceEndpoint()) +"\n");
		
		sb.append(getEnv("coupon_service", EnvManager.getCouponService()));
		sb.append(getEnv("coupon_v3_key", hideKeyValue(EnvManager.getCouponV3Key())));
		sb.append(getEnv("target coupon service endpoint", EnvManager.getTargetCouponV3ServiceEndpoint()) +"\n");
		
		sb.append(getEnv("recipe_service", EnvManager.getRecipeService()));
		sb.append(getEnv("recipe_shelf_thread_pool_size", String.valueOf(EnvManager.getRecipeShelfThreadPoolSize())));
		sb.append(getEnv("recipe_client_id_key", hideKeyValue(EnvManager.getRecipeClientId())));
		sb.append(getEnv("recipe_api_stage_key", hideKeyValue(EnvManager.getRecipeApiKeyStaging())));
		sb.append(getEnv("recipe_api_prod_key", hideKeyValue(EnvManager.getRecipeApiKeyProd())));
		sb.append(getEnv("target recipe API key", hideKeyValue(EnvManager.getTargetRecipeLocaiApiKey())));
		sb.append(getEnv("target recipe service endpoint", EnvManager.getTargetRecipeLocaiServiceEndpoint()) +"\n");
		
		sb.append(getEnv("push2device_service", EnvManager.getPush2DeviceService()));
		sb.append(getEnv("push2device_stg_api_key", hideKeyValue(EnvManager.getPush2deviceApiKeyStaging())));
		sb.append(getEnv("push2device_prod_api_key", hideKeyValue(EnvManager.getPush2deviceApiKeyProd())));
		sb.append(getEnv("push2device_stg_url", EnvManager.getPush2deviceStagingUrl()));
		sb.append(getEnv("push2device_prod_url", EnvManager.getPush2deviceProdUrl()));
		sb.append(getEnv("target push2device API key", hideKeyValue(EnvManager.getTargetPush2DeviceApiKey())));
		sb.append(getEnv("target push2device service endpoint", EnvManager.getTargetPush2DeviceUrl()) +"\n");
		
		sb.append(getEnv("prodx_service", EnvManager.getProdxService()));
		sb.append(getEnv("prodx_complements_stg_api_key", hideKeyValue(EnvManager.getProdxComplementsApiKeyStaging())));
		sb.append(getEnv("prodx_complements_prod_api_key", hideKeyValue(EnvManager.getProdxComplementsApiKeyProd())));
		sb.append(getEnv("prodx_aisle_id", hideKeyValue(EnvManager.getProdxComplementsAisleId())));
		sb.append(getEnv("prodx_variations_prod_api_key", hideKeyValue(EnvManager.getProdxVariationsApiKeyProd())));
		sb.append(getEnv("target prodx_complements API key", hideKeyValue(EnvManager.getTargetProdxComplementsApiKey())));
		sb.append(getEnv("target prodx service endpoint", EnvManager.getTargetProdxServiceEndpoint()) +"\n");
		
		sb.append(getEnv("srfh_orders_service", EnvManager.getSrfhOrdersService()));
		sb.append(getEnv("srfh_orders_stg_api_key", hideKeyValue(EnvManager.getSrfhOrdersApiKeyStaging())));
		sb.append(getEnv("srfh_orders_prd_api_key", hideKeyValue(EnvManager.getSrfhOrdersApiKeyProd())));
		sb.append(getEnv("target SRFH order API key", hideKeyValue(EnvManager.getTargetSRFHOrdersApiKey())));
		sb.append(getEnv("target SRFH order service endpoint", EnvManager.getTargetSRFHOrdersBaseUrl()) +"\n");
		
		sb.append(getEnv("srfh_curbside_service", EnvManager.getSrfhCurbsideService()));
		sb.append(getEnv("srfh_curbside_stg_api_key", hideKeyValue(EnvManager.getSrfhCurbsideApiKeyStaging())));
		sb.append(getEnv("srfh_curbside_prd_api_key", hideKeyValue(EnvManager.getSrfhCurbsideApiKeyProd())));
		sb.append(getEnv("target SRFH curbside API key", hideKeyValue(EnvManager.getTargetSRFHCurbsideApiKey())));
		sb.append(getEnv("target SRFH curbside service endpoint", EnvManager.getTargetSRFHCurbsideBaseUrl()) +"\n");
		
		sb.append(getEnv("wallet_service", EnvManager.getWalletService()));
		sb.append(getEnv("wallet_stage_key", hideKeyValue(EnvManager.getWalletKeyStaging())));
		sb.append(getEnv("wallet_prod_key", hideKeyValue(EnvManager.getWalletKeyProd())));
		sb.append(getEnv("target wallet API key", hideKeyValue(EnvManager.getTargetWalletAuthorizationKey())));
		sb.append(getEnv("target wallet service endpoint", EnvManager.getTargetWalletServiceEndpoint()) +"\n");
		
		sb.append(getEnv("reward_point_service", EnvManager.getRewardPointService()));
		sb.append(getEnv("reward_point_key", hideKeyValue(EnvManager.getRewardPointKey())));
		sb.append(getEnv("target reward service endpoint", EnvManager.getTargetRewardServiceEndpoint()) +"\n");
		
		// for both Staging and Prod
		sb.append(getEnv("apim_nutrition_key", hideKeyValue(EnvManager.getApimNutritionKey())));
		sb.append(getEnv("apim_sms_enrollments_key", hideKeyValue(EnvManager.getApimSmsEnrollmentKey())));
		
		return Response.ok(sb.toString()).build();
	}
	
	private String getEnv(String env, String envValue) {
		return pad("\n" + env + ":") + envValue;
	}
	
	private static String pad(String str) {
		return StringUtils.rightPad(str, FORMAT_RIGHT_PADDING);
	}
	
	private static String hideKeyValue(String value) {
		if ((value != null) && value.trim().length() > 20) {
			return StringUtils.left(value.trim(), 8) + "..." + StringUtils.right(value.trim(), 4);
		} else {
			return "key value is too short"; 
		}
	}
}
