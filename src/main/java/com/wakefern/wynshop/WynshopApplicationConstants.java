package com.wakefern.wynshop;

import com.wakefern.global.ApplicationUtils;
import com.wakefern.wakefern.WakefernApplicationConstants;

public class WynshopApplicationConstants {
	private static final String fgStage = "FreshGrocerStage";
	private static final String fgProd = "FreshGrocerProd";
	private static final String srStage = "ShopRiteStage";
	private static final String srProd = "ShopRiteProd";
	// private static final String srDev = "ShopRiteDev";

	/**
	 * Return the appropriate Application Token.
	 *
	 * @return String
	 */
	public static String getAppToken() {
		String appToken;

		String targetAPI = getTargetAPI();

		switch (targetAPI) {
		case fgStage:
		case fgProd:
			appToken = ApplicationUtils
					.getVcapValue(WakefernApplicationConstants.VCAPKeys.TFG_MWG_PROD_KEY);
			break;
//		case srDev:
//			appToken = shopRiteDevToken;
//			break;
		case srStage:
			appToken = ApplicationUtils
					.getVcapValue(WakefernApplicationConstants.VCAPKeys.SR_MWG_STAGE_KEY);
			break;

			default:
			appToken = ApplicationUtils
					.getVcapValue(WakefernApplicationConstants.VCAPKeys.SR_MWG_PROD_KEY);
		}

		return appToken;
	}

	/**
	 * Return auth token for The Fresh Grocer or ShopRite
	 *
	 * @return
	 */
	public static String getProductRecmdAuthToken() {
		String appToken;
		String targetAPI = getTargetAPI();

		appToken = (targetAPI.equals(fgStage) || targetAPI.equals(fgProd))
				? ApplicationUtils
						.getVcapValue(WakefernApplicationConstants.VCAPKeys.TFG_PRODUCT_RECOMMENDATION_KEY)
				: ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.SR_PRODUCT_RECOMMENDATION_KEY);

		return appToken;
	}

	/**
	 * Check the Bluemix Environment Variable, indicating which Wynshop API this
	 * instance of the Wakefern Java API should be talking to.
	 *
	 * @return String
	 */
	public static String getTargetAPI() {
		String targetAPI = ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.CHAIN);

		// On a local Dev server, the environment variable won't exist unless you have
		// specifically set it up yourself.
		// In that case, default to one of the dev targets.
		//
		// ShopRite Dev : srDev
		// ShopRite Staging : srStage
		// FreshGrocer Staging : fgStage
		targetAPI = (targetAPI == null) ? srStage : targetAPI;

		return targetAPI;
	}

	public static class Requests {
		// Request Parameters
		public static class Params {

			// Request Parameters included within the URL's path.
			public static class Path {
				public static final String token = "token";
				public static final String storeID = "storeId";
			}

			// Request Parameters that are part of the URL's query string.
			public static class Query {
				public static final String userID = "userId";
				public static final String sku = "sku";
				public static final String startDate = "startDate";
				public static final String endDate = "endDate";
			}
		}
	}
}
