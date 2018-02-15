package com.wakefern.global;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
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
        public static String buildErrorJsonOpen = "{\"ErrorMessage\":\"";
        public static String buildErrorJsonClose = "\"}";
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
            public static final String GetPPCCoupons = WakefernApplicationConstants.Requests.Coupons.Metadata.PPCCoupons;
            public static final String GetCoupons = WakefernApplicationConstants.Requests.Coupons.Metadata.Metadata;
            public static final String GetCouponId = WakefernApplicationConstants.Requests.Coupons.ListId.CouponId;
            public static final String GetCouponIdByPPC = WakefernApplicationConstants.Requests.Coupons.ListId.CouponByPPC;
            public static final String CouponAddPPC = WakefernApplicationConstants.Requests.Coupons.ListId.CouponAddPPC;
            public static final String BaseCouponURL = WakefernApplicationConstants.Requests.Coupons.baseURL;
            public static final String GetCouponsRecommendations = WakefernApplicationConstants.Requests.Coupons.Metadata.MetadataRecommendations;
        }

        public static final class Wakefern {
            public static final String ItemLocator = MWGApplicationConstants.Requests.Wakefern.ItemLocator;
            public static final String ItemLocatorAuth = MWGApplicationConstants.Requests.Wakefern.ItemLocatorAuth;
            public static final String ItemLocatorJson = MWGApplicationConstants.Requests.Wakefern.ItemLocatorJson;
        }

        public static class Recommendations {
            public static final String ProductRecommendations = MWGApplicationConstants.Requests.Recommendations.ProductRecommendations;
            public static final String BaseRecommendationsURL = MWGApplicationConstants.Requests.Recommendations.baseURL; 
            public static final String UPCRecommendations = MWGApplicationConstants.Requests.Recommendations.UPCRecommendations;
            public static final String BaseUPCRecommendationsURL = MWGApplicationConstants.Requests.Recommendations.baseUPC_URL;
        }
    }
}
