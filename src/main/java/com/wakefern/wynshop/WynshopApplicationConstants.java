package com.wakefern.wynshop;

import com.wakefern.global.ApplicationUtils;
import com.wakefern.wakefern.WakefernApplicationConstants;

import java.util.logging.Logger;

public class WynshopApplicationConstants {
	private static final Logger logger = Logger.getLogger(WynshopApplicationConstants.class.getName());
	/**
	 * returns either mi9v8 production or staging url endpoint
	 * @return
	 */
	private static String getMi9v8ServiceEndpoint() {
		String mi9v8Service = ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.MI9V8_SERVICE);
		String mi9v8Domain = mi9v8Service.equalsIgnoreCase(WakefernApplicationConstants.Mi9V8.mi9v8Staging) ?
				WakefernApplicationConstants.Mi9V8.baseURLStaging : WakefernApplicationConstants.Mi9V8.baseURL;
		logger.info("getMi9V8ServiceEndpoint::mi9v8Domain " + mi9v8Domain);
		return mi9v8Domain;
	}

	public static final String BaseURL = getMi9v8ServiceEndpoint();
	public static class Requests {
		public static class Routes {
			public static final String CartItemLocator = "/mi9/cart/{storeId}";
			public static final String ShoppingCartItemLocator = "/mi9/v8/lists/planning/{storeId}";
		}
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
