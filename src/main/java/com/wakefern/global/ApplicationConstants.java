package com.wakefern.global;

import com.wakefern.wakefern.WakefernApplicationConstants;

public final class ApplicationConstants {

    public static final String xmlAcceptType  = "text/xml";
    public static final int xmlTabAmount      = 4;
    public static final String ErrorMessage   = "ErrorMessage";

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
            public static final String contentType  = "Content-Type";
            public static final String contentAccept = "Accept";
            public static final String contentAuthorization = "Authorization";
            public static final String userAgent = "User-Agent";
            public static final String appCode = "appCode";
            public static final String jwtToken = "JWT-Token";
            public static final String appVersion  = "AppVersion";
        }

        public static class Tokens {
            public static final String couponToken = "noowhTBIYfzVrXOcFrNSwIFbkMoqRh19";
            public static final String couponV2Token = "JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODM1NjgzODAsImVtYWlsIjoiS0VWSU4uSkFOR0BXQUtFRkVSTi5DT00iLCJmdWxsTmFtZSI6ImNvdXBvbldlYlVzZXJzIiwiaWF0IjoxNTI1ODg4MzgwfQ.Numfi4df5nSvfR9Rt2POzA4Fki-iw7CAQT6rmjb32IY";
            public static final String planningToken = "486806CF-CF9A-4087-8C05-ED1B0008AF03";
        }

        public static final class Coupons {
            public static final String GetPPCCoupons = WakefernApplicationConstants.Coupons.Metadata.PPCCoupons;
            public static final String GetCoupons = WakefernApplicationConstants.Coupons.Metadata.Metadata;
            public static final String GetCouponId = WakefernApplicationConstants.Coupons.ListId.CouponId;
            public static final String GetCouponIdByPPC = WakefernApplicationConstants.Coupons.ListId.CouponByPPC;
            public static final String CouponIDByPromoCode = WakefernApplicationConstants.Coupons.ListId.CouponIDByPromoCode;
            public static final String CouponAddPPC = WakefernApplicationConstants.Coupons.ListId.CouponAddPPC;
            public static final String BaseCouponURL = WakefernApplicationConstants.Coupons.baseURL;
            public static final String GetCouponsRecommendations = WakefernApplicationConstants.Coupons.Metadata.MetadataRecommendations;
        }
        
        public static final class CouponsV2 {
            public static final String BaseCouponURL = ApplicationUtils.getCouponServiceEndpoint(WakefernApplicationConstants.VCAPKeys.COUPON_SERVICE);
            public static final String BaseCouponURLAuth = BaseCouponURL.replace(WakefernApplicationConstants.CouponsV2.context_root, "");
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
        public static final String VerifyServices = "/wakefern/services/v7/verify";
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
