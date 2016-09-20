package com.wakefern.Wakefern;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernApplicationConstants {

    public static class Requests {
        public static class Coupons {
            public static final String baseURL = "http://couponprodwest.azure-mobile.net/api";


            public static class Headers{
                public static final String CouponAuthenticationToken        = "noowhTBIYfzVrXOcFrNSwIFbkMoqRh19";
                public static final String CouponAuthenticationTokenHeader  = "X-ZUMO-APPLICATION";
            }

            public static class Metadata{
                public static final String Metadata = "/getCouponMetadata";
                public static final String PPC = "ppc_number";
                public static final String PPC_All = "all";
                public static final String PPCQuery = "?ppc_number=";
            }

            public static class Search{
                public static final String brandName = "brand_name";
                public static final String category = "Category";
                public static final String longDescription = "long_description";
                public static final String shortDescription = "short_description";
                public static final String requirementDescription = "requirement_description";
            }

        }
    }
}
