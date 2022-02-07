package com.wakefern.global;

import com.wakefern.wakefern.WakefernApplicationConstants;

public final class ApplicationConstants {

	public static final String xmlAcceptType = "text/xml";
	public static final int xmlTabAmount = 4;
	public static final String ErrorMessage = "ErrorMessage";

	public static class StringConstants {
		public static final String fq = "&fq=";
		public static final String isMember = "?isMember";
		public static final String isMemberAmp = "&isMember";
		public static final String payment = "/payment";
		public static final String skip = "&skip=";
		public static final String sort = "&sort=";
		public static final String store = "/store";
		public static final String takeAmp = "&take=";
		public static final String wakefernApplication = "wakefern application";
			
	}

	public static class Requests {
		public static String unauthorizedError = "Session is not valid";

		public static class Header {
			public static final String contentType = "Content-Type";
			public static final String contentAccept = "Accept";
			public static final String contentAuthorization = "Authorization";
			public static final String userAgent = "User-Agent";
			public static final String appCode = "appCode";
			public static final String jwtToken = "JWT-Token";
			public static final String appVersion = "AppVersion";
			public static final String xSiteHost = "x-site-host";
		}

		public static final class CouponsV2 {
			public static final String BaseCouponURL = ApplicationUtils
					.getCouponServiceEndpoint(WakefernApplicationConstants.VCAPKeys.COUPON_SERVICE);
			public static final String BaseCouponURLAuth = BaseCouponURL
					.replace(WakefernApplicationConstants.CouponsV2.context_root, "");
			public static final String UserLogin = WakefernApplicationConstants.CouponsV2.PathInfo.UserLogin;
			public static final String CouponMetadata = WakefernApplicationConstants.CouponsV2.PathInfo.CouponMetadata;
			public static final String fsn = WakefernApplicationConstants.CouponsV2.PathInfo.fsn;
			public static final String rule = WakefernApplicationConstants.CouponsV2.PathInfo.rule;
			public static final String upc = WakefernApplicationConstants.CouponsV2.PathInfo.upc;
			public static final String coupon_id = WakefernApplicationConstants.CouponsV2.PathInfo.coupon_id;
			public static final String clip_token = WakefernApplicationConstants.CouponsV2.PathInfo.clip_token;
			public static final String CouponIDListByPPC_SEC = WakefernApplicationConstants.CouponsV2.PathInfo.CouponIDListByPPC_SEC;
			public static final String CouponIDListByPPC_SEC_FG = WakefernApplicationConstants.CouponsV2.PathInfo.CouponIDListByPPC_SEC_FG;
			public static final String AddCouponToPPC_SEC = WakefernApplicationConstants.CouponsV2.PathInfo.AddCouponToPPC_SEC;
			public static final String AddCouponToPPC_SEC_FG = WakefernApplicationConstants.CouponsV2.PathInfo.AddCouponToPPC_SEC_FG;
			public static final String RemoveCouponFromPPC = WakefernApplicationConstants.CouponsV2.PathInfo.RemoveCouponFromPPC;
			public static final String RemoveCouponFromPPC_FG = WakefernApplicationConstants.CouponsV2.PathInfo.RemoveCouponFromPPC_FG;
			public static final String GetCouponByUPC = WakefernApplicationConstants.CouponsV2.PathInfo.GetCouponByUPC;
			public static final String GetCouponIDByPromoCode = WakefernApplicationConstants.CouponsV2.PathInfo.GetCouponIDByPromoCode;
			public static final String GetUPCListByCouponID = WakefernApplicationConstants.CouponsV2.PathInfo.GetUPCListByCouponID;
			public static final String GetHistoryMetadata = WakefernApplicationConstants.CouponsV2.PathInfo.GetHistoryMetadata;
			public static final String GetHistoryCouponByPPC = WakefernApplicationConstants.CouponsV2.PathInfo.GetHistoryCouponByPPC;
			public static final String GetSpecialOfferByRule = WakefernApplicationConstants.CouponsV2.PathInfo.GetSpecialOfferByRule;
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

	public static class CheckoutNotify {
		public static final String CheckoutNotifyURL = "/checkout/notify";
		public static final String SuccessPathURL = "/success/{query}";
		public static final String FailurePathURL = "/failure/{query}";
		public static final String SuccessCallbackURL = "wakefernshopriteapp://checkoutSuccess";
		public static final String FailureCallbackURL = "wakefernshopriteapp://checkoutFailure";
	}
}
