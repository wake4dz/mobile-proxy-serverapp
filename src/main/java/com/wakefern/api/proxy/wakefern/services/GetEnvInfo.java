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

		sb.append(getEnv("item_locator_cache_enabled", String.valueOf(EnvManager.isItemLocatorCacheEnabled())) + "\n");
		
		sb.append(getEnv("proxy_admin_key", hideKeyValue(EnvManager.getProxyAdminKey())) +"\n");
		
		sb.append(getEnv("jwt_public_key", hideKeyValue(EnvManager.getJwtPublicKey())));
		sb.append(getEnv("user_jwt_secret", hideKeyValue(EnvManager.getUserJwtSecret())) +"\n");
		
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
		
		return Response.ok(sb.toString()).build();
	}
	
	private String getEnv(String env, String envValue) {
		return pad("\n" + env + ":") + envValue;
	}
	
	private static String pad(String str) {
		return StringUtils.rightPad(str, FORMAT_RIGHT_PADDING);
	}
	
	private static String hideKeyValue(String value) {
		if ((value != null) && value.trim().length() > 10) {
			return StringUtils.left(value.trim(), 4) + "..." + StringUtils.right(value.trim(), 4);
		} else {
			return "key value is too short"; 
		}
	}
}
