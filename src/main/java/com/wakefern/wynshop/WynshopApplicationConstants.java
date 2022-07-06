package com.wakefern.wynshop;

public class WynshopApplicationConstants {

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
