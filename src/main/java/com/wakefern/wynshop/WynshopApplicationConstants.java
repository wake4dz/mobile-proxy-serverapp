package com.wakefern.wynshop;

import com.wakefern.global.EnvManager;

import java.util.logging.Logger;

public class WynshopApplicationConstants {

	public static final String BaseURL = EnvManager.getTargetMi9v8ServiceEndpoint();
	
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
