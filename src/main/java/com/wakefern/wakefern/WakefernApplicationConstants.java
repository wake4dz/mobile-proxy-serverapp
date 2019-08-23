package com.wakefern.wakefern;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernApplicationConstants {

	// Wakefern APIs
	private static final String wakefernApiStage = "https://wfcapi.staging.shoprite.com";
	private static final String wakefernApiProd = "https://wfcapi.shoprite.com";

	public static String getBaseWakefernApiUrl() {
		String targetAPI = MWGApplicationConstants.getTargetAPI();
		if (targetAPI.equals("ShopRiteStage") || targetAPI.equals("FreshGrocerStage")) {
			return wakefernApiStage;
		}

		return wakefernApiProd;
	}
	
	public static class VCAPKeys{
		public static final String cors = "cors";
		public static final String chain = "chain";
		public static final String url = "url";
		public static final String coupon_v2_key = "coupon_v2_key";
		public static final String sr_mwg_stage_key = "sr_mwg_stage_key";
		public static final String sr_mwg_prod_key = "sr_mwg_prod_key";
		public static final String tfg_mwg_prod_key = "tfg_mwg_prod_key";
		public static final String jwt_public_key = "jwt_public_key"; //use for digital receipt & item locator
		public static final String prod_not_found_login = "prod_not_found_login";
		public static final String sr_product_recommendation_key = "sr_product_recommendation_key";
		public static final String tfg_product_recommendation_key = "tfg_product_recommendation_key";
	}

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
            public static final String GetCouponIDByPromoCode = "/v2/getCouponIDByPromoCode";
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
        
        public static final String baseURL = "https://api.wakefern.com";
        public static final String locationPath = "/itemlocator/item/location";
        public static final String authPath = "/wfctoken/auth/gentoken";
        
    	public static final String prefix = "/iteminfo/v1/secure/itemData";
    	public static final String storeId = "storeId";
    	public static final String upcs = "upcs";
    	
    	public static final String itemInfo_appCode = "KPC1";
    	public static final String itemInfo_authPath = "/iteminfo/v1/tokens";
    	public static final String itemInfo_storePath = "/store/{storeId}";
    	public static final String itemInfo_upcsPath = "/upc/{upcs}";
    	public static final String itemLocatorArr = "itemLocator";
    	public static final String itemLocatorPath = itemInfo_storePath + itemInfo_upcsPath;
    }
    
	public static class Reports {

		public static class NotFound {
			public static final String contextPath = "/notfound/api/v1";
			public static final String authenticate = contextPath + "/authenticate/user";
			public static final String product = contextPath + "/product";
		}
	}

    public static class UserProfile {
    		public static final String Address = "Addresses";
    		public static final String IsDefaultBilling = "IsDefaultBilling";
    }
}
