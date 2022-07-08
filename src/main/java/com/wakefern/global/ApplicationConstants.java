package com.wakefern.global;

import com.wakefern.wakefern.WakefernApplicationConstants;

public final class ApplicationConstants {
	public static final String ErrorMessage = "ErrorMessage";

	public static class StringConstants {
		public static final String store = "/store";
		public static final String wakefernApplication = "wakefern application";
	}

	public static class Requests {
		public static final String unauthorizedError = "Session is not valid";

		public static class Headers {
			public static final String contentType = "Content-Type";
			public static final String Accept = "Accept";
			public static final String Authorization = "Authorization";
			public static final String userAgent = "User-Agent";
			public static final String appCode = "appCode";
			public static final String jwtToken = "JWT-Token";
			public static final String appVersion = "AppVersion";
			public static final String xSiteHost = "x-site-host";
			public static final String reservedTimeslot = "X-Reserved-Timeslot";
			public static final String xForwardedFor = "X-Forwarded-For";

			public static class MIMETypes {
				public static final String generic = "application/*";
				public static final String json = "application/json";
			}
		}

		public static final class CouponsV3 {
			public static final String BaseCouponURL = ApplicationUtils
					.getCouponV3ServiceEndpoint(WakefernApplicationConstants.VCAPKeys.COUPON_SERVICE);
			public static final String UserLogin = "/coupons/v3/login";
			public static final String CouponList = "/coupons/v3/list";
			public static final String AvailableCoupons = "/coupons/v3/available";
			public static final String ExpiredCoupons = "/coupons/v3/expired";
			public static final String ClippedCoupons = "/coupons/v3/clipped";
			public static final String RedeemedCoupons = "/coupons/v3/redeemed";
			public static final String GetCouponByPromoCode = "/coupons/v3/promocode/{promoCode}";
			public static final String AddCouponToPPC = "/coupons/v3/clip";
			public static final String RemoveCouponFromPPC = "/coupons/v3/unclip";
			public static final String GetCouponByUPC = "/coupons/v3/upc/{upc}";
			public static final String GetUPCsByCouponId = "/coupons/v3/upcList/{couponId}";
			public static final String GetCouponByExternalCouponIds = "/coupons/v3/couponIds/{externalCouponIds}";
			public static final String GetCouponsByStoreId = "/coupons/v3/storeId/{storeId}";
			public static final String GetSocialOffersByQuery = "/coupons/v3/socialoffers/{query}";

			public static final String banner = "shoprite";
			public static final String couponId = WakefernApplicationConstants.CouponsV3.PathInfo.couponId;
			public static final String couponIds = WakefernApplicationConstants.CouponsV3.PathInfo.couponIds;
			public static final String externalCouponIds = "externalCouponIds";
			public static final String upc = WakefernApplicationConstants.CouponsV3.PathInfo.upc;
			public static final String storeId = "storeId";
		}

		public static class Recommendations {
			public static final String ProductRecommendations = "/recommend/api/v1/products/user";

			public static final String ProductRecommendationsv2 = "/recommedation/api/v2/store";
			public static final String BaseRecommendationsURL = "https://wfcapi.shoprite.com";
//            public static final String UPCRecommendations = "/api/wfc/store/";
//            public static final String BaseUPCRecommendationsURL = "https://vp.shoprite.com";

		}

		public static class Reports {
			public static final String NotFoundTokenURL = "/reports/notFound/token";
			public static final String NotFoundProductURL = "/reports/notFound/product";
		}

		
		public static final class Mi9V8 {
			public static final String BaseURL = ApplicationUtils
					.getMi9v8ServiceEndpoint(WakefernApplicationConstants.VCAPKeys.MI9V8_SERVICE);
			
			public static final String ShoppingCartItemLocator = "/mi9/v8/lists/planning/{storeId}";
		}
		
		public static final String VerifyServices = "/wakefern/services/v7/verify";
		
		public static final String Proxy = "/proxy";
	}

	public static class Logging {
		public static final String Logging = "/logging";
		public static final String log = "log";
		public static final String LoggingAuth = "wakefern-shoprite-auth";
	}

	public static class Log {
		public static final String log = "/log";

		public static final String email = log + "/email";
		public static final String status = "/status";
		public static final String address = "/address" + "/{addresses}";
		public static final String updateSetting = "/updateSettings";
		public static final String trackUserId = "/trackUserId/{userIds}";

		public static final String logger = log + "/logger";
		public static final String changeLevel = "/level/{logLevel}";
		public static final String getLevel = "/level";
		public static final String appenderList = "/appender/list";

		public static final String release = log + "/release";
		public static final String releaseLevel = "/level";

	}


	public static class NewRelic {
		public static final String NewRelicURL = "https://api.newrelic.com/v2";
		public static final String NewRelicDeployments = "/deployments.json";
		public static final String Applications = "/applications/";
		public static final String Deployments = "{appId}/deployments";

		public static final String NewRelicHeaderKey = "X-Api-Key";

		public static final String Deployment = "deployment";
		public static final String Revision = "revision";
		public static final String Changelog = "changelog";
		public static final String Description = "description";
		public static final String User = "user";
		public static final String UserWFCAdmin = "WFCAdmin";
		public static final String Timestamp = "timestamp";
	}
}
