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
		public static final String CORS = "cors";
		public static final String CHAIN = "chain";
		public static final String URL = "url";
		public static final String COUPON_SERVICE = "coupon_service";
		public static final String COUPON_V2_KEY = "coupon_v2_key";
		public static final String SR_MWG_STAGE_KEY = "sr_mwg_stage_key";
		public static final String SR_MWG_PROD_KEY = "sr_mwg_prod_key";
		public static final String TFG_MWG_PROD_KEY = "tfg_mwg_prod_key";
		public static final String JWT_PUBLIC_KEY = "jwt_public_key"; //use for digital receipt & item locator
		public static final String PROD_NOT_FOUND_LOGIN = "prod_not_found_login";
		public static final String SR_PRODUCT_RECOMMENDATION_KEY = "sr_product_recommendation_key";
		public static final String TFG_PRODUCT_RECOMMENDATION_KEY = "tfg_product_recommendation_key";
		public static final String APIM_PPC_EMAIL_KEY = "apim_ppc_email_key";
		public static final String ENABLE_CART_ITEM_LOCATOR = "enable_cart_item_locator";
		public static final String NEW_RELIC_KEY = "new_relic_key";
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
		public static final String coupon_staging = "Staging";
        public static final String baseURL = "https://couponapis.shoprite.com/api";
        public static final String baseURL_staging = "https://couponapis.staging.shoprite.com/api";
        public static final String context_root = "/api";

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

	public static class APIM {
		public static final String apimBaseURL = "https://apimprod.wakefern.com";
		public static final String ppcByEmail = "/WebAndClient/V1/LMWPPC_R_ByEmail";
		
		public static final String sub_key_header = "Ocp-Apim-Subscription-Key";
		public static final String email = "Email";

		public static final String resultSet_output = "ResultSet 1 Output";
		public static final String ppc_acct_id = "PPC_ACCT_ID";

		public static final String notFoundStatus="PPC number not found. Please contact our Customer Care Center at 1-800-ShopRite (1-800-746-7748).";
//				+ "Customer Care Associates are available to assist you Monday through Friday from 8 AM to 6 PM, and Saturday and Sunday from 9 AM to 5 PM.";
		public static final String foundStatus = "An email has been sent to you with your Price Plus card number. Please allow a few moments for the email to arrive.";
	
		public static final String mi9_fsn_key="Fsn";
		public static final String mi9_first_name="FirstName";
		public static final String mi9_last_name="LastName";
		
		public static final String mi9_fName_value="Valued";
		public static final String mi9_lName_value="Customer";
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
        
        public static final String prefix = "/iteminfo/v1";
        public static final String storeId = "storeId";
        public static final String upcs = "upcs";

        public static final String itemInfo_appCode = "KPC1";
        public static final String itemInfo_tokenPath = "/tokens";
        public static final String itemInfo_storePath = "/store/{storeId}";
        public static final String itemInfo_upcsPath = "/upc/{upcs}";
        public static final String itemLocatorArr = "itemLocator";
        public static final String itemLocatorPath = "/secure/itemData" + itemInfo_storePath + itemInfo_upcsPath;
    }
    
	public static class Receipt {

        public static class Proxy {
            public static final String Path = "users/{userId}/{ppc}/receipts";
            public static final String Detail = "/{receiptId}";
        }

        // Wakefern's APIs
		public static class Upstream {
            public static final String BaseURL = "https://api.wakefern.com";
            public static final String User = "/digitalreceipt/users";
        }
	}
	
	public static class JwtToken {
		// Wakefern's APIs
		public static final String JwtToken = "/jwtToken";
		public static final String TokenGen = "/generation";
		
		public static final String BaseURL = "https://api.wakefern.com";

		 public static final String authPath = "/wfctoken/auth/gentoken";

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
