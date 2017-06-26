package com.wakefern.Wakefern;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernApplicationConstants {
	
	public static class Statica{
		public static final String userName = "statica3492";
		public static final String password = "efadafb2c0b0f730";
		public static final String host = "sl-ams-01-guido.statica.io";
		public static final String port = "9293";
	}

    public static class Requests {
        public static class Coupons {
            public static final String baseURL = "http://couponprodwest.azure-mobile.net/api";


            public static class Headers{
                public static final String CouponAuthenticationToken        = "noowhTBIYfzVrXOcFrNSwIFbkMoqRh19";
                public static final String CouponAuthenticationTokenHeader  = "X-ZUMO-APPLICATION";
            }

            public static class ListId{
                public static final String CouponId = "/getCouponIDListBySR";
                public static final String CouponByPPC = "/getCouponIDListByPPC";
                public static final String CouponAddPPC = "/addCouponToPPC";
            }

            public static class Metadata{
                public static final String Metadata = "/getCouponMetadata";
                public static final String PPC = "ppc_number";
                public static final String PPC_All = "all";
                public static final String PPCQuery = "?ppc_number=";
                public static final String CouponId = "coupon_id";
                public static final String CouponParam = "&coupon_id=";
                public static final String ClipSource = "&clip_source=";
                public static final String ClipSource_App_SR = "APP_SR";
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
