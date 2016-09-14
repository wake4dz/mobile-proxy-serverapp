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
            }

        }
    }
}
