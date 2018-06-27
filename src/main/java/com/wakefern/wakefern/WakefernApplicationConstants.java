package com.wakefern.wakefern;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernApplicationConstants {
	
    public static final String authToken = "7bd4a45d-4fef-4edf-a74d-c2214c0b7b54"; // Used by legacy Wakefern endpoints.
    public static final String srAuthToken = "e9d69feb-012b-45e1-bf10-eea13424495d"; // v2 product recommendation for ShopRite
    public static final String tfgAuthToken = "17a7139d-7268-4548-a441-29dbb380a592"; // v2 product recommendation for TheFreshGrocer

	public static class Coupons {
        public static final String baseURL = "http://couponprodwest.azure-mobile.net/api";

        public static class Headers{
            public static final String CouponAuthenticationToken        = "noowhTBIYfzVrXOcFrNSwIFbkMoqRh19";
            public static final String CouponAuthenticationTokenHeader  = "X-ZUMO-APPLICATION";
        }

        public static class ListId{
            public static final String CouponId     = "/getCouponIDListBySR";
            public static final String CouponByPPC  = "/getCouponIDListByPPC";
            public static final String CouponAddPPC = "/addCouponToPPC";
        }

        public static class Metadata{
            public static final String PPCCoupons = "/getPPCCoupons";
            public static final String Metadata = "/getCouponMetadata";
            public static final String MetadataRecommendations = "/getCouponsRecommendations";
            public static final String PPC = "ppc_number";
            public static final String PPC_All = "all";
            public static final String PPCQuery = "?ppc_number=";
            public static final String CouponId = "coupon_id";
            public static final String CouponParam = "&coupon_id=";
            public static final String ClipSource = "&clip_source=";
            public static final String ClipSource_App_SR = "APP_SR";
            
            //For Coupon and recommendation
            public static final String store  = "?storeId=";
            public static final String pseudo = "&pseudo=";
            public static final String email  = "&email=";
            public static final String ppcNo  = "&ppc_number=";
        }

        public static class Search{
            public static final String brandName = "brand_name";
            public static final String category = "Category";
            public static final String longDescription = "long_description";
            public static final String shortDescription = "short_description";
            public static final String requirementDescription = "requirement_description";
        }
    }
    
    public static class ItemLocator {
        public static final String Aisle = "Aisle";
        public static final String area_desc = "wf_area_desc";
        public static final String Items = "Items";
        public static final String Other = "OTHER";
        public static final String Sku = "Sku";
        public static final String upc_13_num = "upc_13_num";
        public static final String item_locations = "item_locations";
        public static final String area_seq_num = "area_seq_num";
        public static final String wf_area_code = "wf_area_code";
        public static final String TotalPrice = "TotalPrice";
        public static final String RegularPrice = "RegularPrice";
        public static final String Quantity = "Quantity";
        public static final String CurrentPrice = "CurrentPrice";
        public static final String Size = "Size";
        public static final String Sale = "Sale";
        public static final String LimitText = "LimitText";
        
        public static final String WakefernAuth    = "eyJleHAiOjE0NzYxMDQyMTM1NDYsInN1YiI6InNmamMxcGFzc3dkIiwiaXNzIjoiaHR0cDovL3dha2VmZXJuLmNvbSJ9";
        public static final String baseURL = "https://api.wakefern.com";
        public static final String locationPath = "/itemlocator/item/location";
        public static final String authPath = "/wfctoken/auth/gentoken";
    }
    
    public static class UserProfile {
    		public static final String Address = "Addresses";
    		public static final String IsDefaultBilling = "IsDefaultBilling";
    }
}
