package com.wakefern.wakefern;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernApplicationConstants {
	
    public static final String authToken = "7bd4a45d-4fef-4edf-a74d-c2214c0b7b54"; // Used by legacy Wakefern endpoints.
    public static final String srAuthToken = "e9d69feb-012b-45e1-bf10-eea13424495d"; // v2 product recommendation for ShopRite
    public static final String tfgAuthToken = "17a7139d-7268-4548-a441-29dbb380a592"; // v2 product recommendation for TheFreshGrocer

    public static class Chains {
		public static final String FreshGrocer = "FreshGrocer";
		public static final String ShopRite = "ShopRite";
    }

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
            public static final String CouponIDByPromoCode = "/getCouponIDByPromoCode";
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
            public static final String PromoteCode = "code";
            public static final String PromoteCodeParam = "&code=";
            public static final String PromoteCodeAdd = "add";
            public static final String PromoteCodeAddParam = "&add=";
            
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

	public static class CouponsV2 {
        public static final String baseURL = "https://couponapis.shoprite.com/api";
        public static final String baseURLAuth = "https://couponapis.shoprite.com";

        public static class Headers {
            public static final String CouponAuthenticationToken = "JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE2ODM1NjgzODAsImVtYWlsIjoiS0VWSU4uSkFOR0BXQUtFRkVSTi5DT00iLCJmdWxsTmFtZSI6ImNvdXBvbldlYlVzZXJzIiwiaWF0IjoxNTI1ODg4MzgwfQ.Numfi4df5nSvfR9Rt2POzA4Fki-iw7CAQT6rmjb32IY";
            public static final String clip_source = "clip_source";
            public static final String clip_token = "clip_token";
            public static final String coupon_id = "coupon_id";
            public static final String fsn = "fsn";
        }

        public static class PathInfo{
            public static final String CouponId     = "/getCouponIDListBySR";
            public static final String CouponByPPC  = "/getCouponIDListByPPC";
            public static final String CouponAddPPC = "/addCouponToPPC";
            public static final String UserLogin = "/auth/v2/userLogin";
            public static final String CouponMetadata = "/v2/getCouponMetadata";
            public static final String CouponIDListByPPC_SEC = "/v2/getCouponIDListByPPC_SEC";
            public static final String CouponIDListByPPC_SEC_FG = "/v2/getCouponIDListByPPC_SEC_FG";
            public static final String AddCouponToPPC_SEC = "/v2/addCouponToPPC_SEC";
            public static final String AddCouponToPPC_SEC_FG = "/v2/addCouponToPPC_SEC_FG";
            public static final String RemoveCouponFromPPC = "/v2/removeCouponFromPPC";
            public static final String RemoveCouponFromPPC_FG = "/v2/removeCouponFromPPC_FG";
            public static final String GetCouponByUPC = "/v2/getCouponByUPC";
            public static final String GetUPCListByCouponID = "/v2/getUPCListByCouponID";
            public static final String GetHistoryMetadata = "/v2/getHistoryMetadata";
            public static final String GetHistoryCouponByPPC = "/v2/getHistoryCouponByPPC";
            public static final String fsn = "fsn";
            public static final String upc = "upc";
            public static final String coupon_id = "coupon_id";
            public static final String clip_token = "clip_token";
        }

        public static class QueryParam{
            public static final String CouponParam = "&coupon_id=";
            public static final String UPCParam = "&upc=";
            public static final String ClipSource = "&clip_source=";
        }

        public static class ParamValues {
			public static final String ClipAppSource_FG = "APP_FG";
			public static final String ClipAppSource_SR = "APP_SR";
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
