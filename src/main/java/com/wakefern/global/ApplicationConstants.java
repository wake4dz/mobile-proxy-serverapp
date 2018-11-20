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
    }

    public static class Requests {
        public static String forbiddenError = "Session is not valid";

        public static class Header {
            public static final String contentType  = "Content-Type";
            public static final String contentAccept = "Accept";
            public static final String contentAuthorization = "Authorization";
        }

        public static class Tokens {
            public static final String couponToken = "noowhTBIYfzVrXOcFrNSwIFbkMoqRh19";
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

        public static class Recommendations {
            public static final String ProductRecommendations = "/recommend/api/v1/products/user";

            public static final String ProductRecommendationsv2 = "/recommedation/api/v2/store";
            public static final String BaseRecommendationsURL = "https://wfcapi.shoprite.com"; 
//            public static final String UPCRecommendations = "/api/wfc/store/";
//            public static final String BaseUPCRecommendationsURL = "https://vp.shoprite.com";

        }
    }
    
    public static class Logging {
		public static final String Logging = "/logging";
		public static final String log = "log";
		public static final String LoggingAuth = "wakefern-shoprite-auth";
    }
}
