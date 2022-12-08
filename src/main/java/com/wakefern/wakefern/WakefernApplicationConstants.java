package com.wakefern.wakefern;

import com.wakefern.global.ApplicationUtils;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernApplicationConstants {

    // Wakefern APIs
    private static final String wakefernApiStage = "https://wfcapi.staging.shoprite.com";
    private static final String wakefernApiProd = "https://wfcapi.shoprite.com";

    public static String getBaseWakefernApiUrl() {
        String targetAPI = ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.CHAIN);
        if (targetAPI.equalsIgnoreCase("ShopRiteStage")) {
            return wakefernApiStage;
        }

        return wakefernApiProd;
    }

    public static class VCAPKeys {
        public static final String CHAIN = "chain";
        public static final String URL = "url";
        public static final String COUPON_SERVICE = "coupon_service";
        public static final String COUPON_V3_KEY = "coupon_v3_key";

        public static final String PROXY_ADMIN_KEY = "proxy_admin_key";

        public static final String JWT_PUBLIC_KEY = "jwt_public_key"; // use for digital receipt & item locator
        public static final String PROD_NOT_FOUND_LOGIN = "prod_not_found_login";
        public static final String SR_PRODUCT_RECOMMENDATION_KEY = "sr_product_recommendation_key";


        public static final String APIM_PPC_EMAIL_KEY = "apim_ppc_email_key";
        public static final String APIM_NUTRITION_KEY = "apim_nutrition_key";

        // subscription key to APIM SMS Enrollments API
        public static final String APIM_SMS_ENROLLMENTS_KEY = "apim_sms_enrollments_key";

        public static final String API_HIGH_TIMEOUT = "api_high_timeout";
        public static final String API_MEDIUM_TIMEOUT = "api_medium_timeout";
        public static final String API_LOW_TIMEOUT = "api_low_timeout";

        // Locai's Recipe Shop
        public static final String RECIPE_SERVICE = "recipe_service";
        public static final String RECIPE_CLIENT_ID_KEY = "recipe_client_id_key";
        public static final String RECIPE_API_STAGE_KEY = "recipe_api_stage_key";
        public static final String RECIPE_API_PROD_KEY = "recipe_api_prod_key";
        public static final String RECIPE_SHELF_THREAD_POOL_SIZE = "recipe_shelf_thread_pool_size";

        // Wakefern's internal Wallet API
        public static final String WALLET_SERVICE = "wallet_service";
        public static final String SR_WALLET_PROD_KEY = "sr_wallet_prod_key";
        public static final String SR_WALLET_STAGE_KEY = "sr_wallet_stage_key";

        // Secret for signing JWT used to secure PPC endpoints
        public static final String USER_JWT_SECRET = "user_jwt_secret";

        // SRFH Orders API
        public static final String SRFH_ORDERS_STG_API_KEY = "srfh_orders_stg_api_key";
        public static final String SRFH_ORDERS_PROD_API_KEY = "srfh_orders_prd_api_key";
        public static final String SRFH_ORDERS_SERVICE = "srfh_orders_service";

        // SRFH curbside API
        public static final String SRFH_CURBSIDE_STG_API_KEY = "srfh_curbside_stg_api_key";
        public static final String SRFH_CURBSIDE_PROD_API_KEY = "srfh_curbside_prd_api_key";
        public static final String SRFH_CURBSIDE_SERVICE = "srfh_curbside_service";

        // Prodx
        public static final String PRODX_SERVICE = "prodx_service";
        public static final String PRODX_COMPLEMENTS_STG_API_KEY = "prodx_complements_stg_api_key";
        public static final String PRODX_COMPLEMENTS_PROD_API_KEY = "prodx_complements_prod_api_key";
        public static final String PRODX_AISLE_ID = "prodx_aisle_id";
        public static final String PRODX_VARIATIONS_PROD_API_KEY = "prodx_variations_prod_api_key";
        
        public static final String MI9V8_SERVICE = "mi9v8_service";

        public static final String REWARD_POINT_SERVICE = "reward_point_service";
        
        public static final String MUTE_ERROR_LOG = "mute_error_log";
        public static final String MUTE_HTTP_CODE = "mute_http_code";

        public static final String ITEM_LOCATOR_CACHE_ENABLED = "item_locator_cache_enabled";
        
    	public static final String HTTP_DEFAULT_CONNECT_TIMEOUT = "http_default_connect_timeout_ms";
    	public static final String HTTP_DEFAULT_READ_TIMEOUT = "http_default_read_timeout_ms";
    }

    public static class CouponsV3 {
        public static final String coupon_staging = "Staging";
        public static final String baseURL = "https://couponapis.brands.wakefern.com/api";
        public static final String baseURL_staging = "https://couponapis.staging.brands.wakefern.com/api";

        public static class PathInfo {
            public static final String UserLogin = "/v3/auth/login";
            public static final String AvailableCoupons = "/v3/{banner}/coupons/available";
            public static final String CouponsList = "/v3/{banner}/coupons/list";
            public static final String AddCouponToPPC = "/v3/{banner}/coupons/clip";
            public static final String RemoveCouponFromPPC = "/v3/{banner}/coupons/unclip";
            public static final String ExpiredCoupons = "/v3/{banner}/coupons/expired";
            public static final String ClippedCoupons = "/v3/{banner}/coupons/clipped";
            public static final String RedeemedCoupons = "/v3/{banner}/coupons/redeemed";
            public static final String GetCouponByPromoCode = "/v3/{banner}/coupons/promocode/{promoCode}";
            public static final String GetCouponsByUPC = "/v3/{banner}/coupons/upc/{upcCode}";
            public static final String GetUPCListByCouponId = "/v3/{banner}/coupons/upcList/{couponId}";
            public static final String GetCouponByCouponId = "/v3/{banner}/coupons/couponIds/{couponIds}";
            public static final String GetCouponListByStoreId = "/v3/{banner}/coupons/storeId/{storeId}";
            public static final String GetCouponListByQuery = "/v3/{banner}/coupons/socialoffers/{query}";

            public static final String upc = "upc";
            public static final String banner = "banner";
            public static final String couponId = "couponId";
            public static final String couponIds = "couponIds";
        }
    }

    public static class Mi9V8 {
        public static final String mi9v8Staging = "Staging";
        public static final String baseURL = "https://mobile-gateway.brands.wakefern.com/api";
        public static final String baseURLStaging = "https://mobile-gateway.staging.brands.wakefern.com/api";

        public static class PathInfo {
            public static final String GetPlanning = "/lists/planning/{storeId}";

        }
    }

    public static class APIM {
        public static final String apimBaseURL = "https://apimprod.wakefern.com";
        public static final String apimDevBaseURL = "https://apimdev.wakefern.com";
        public static final String ppcByEmail = "/WebAndClient/V1/LMWPPC_R_ByEmail";
        public static final String nutritionBySkuStoreId = "/WebAndClient/V1/mwgmenulabeling";

        public static final String apimWakefernProduct = "/wakefern/product";
        public static final String apimNutritionUrl = "/nutrition/{skuStoreId}";
        public static final String sub_key_header = "Ocp-Apim-Subscription-Key";
        public static final String email = "Email";

        public static final String resultSet_output = "ResultSet 1 Output";
        public static final String ppc_acct_id = "PPC_ACCT_ID";

        public static final String notFoundStatus = "PPC number not found. Please contact our Customer Care Center at 1-800-ShopRite (1-800-746-7748).";
        //				+ "Customer Care Associates are available to assist you Monday through Friday from 8 AM to 6 PM, and Saturday and Sunday from 9 AM to 5 PM.";
        public static final String foundStatus = "An email has been sent to you with your Price Plus card number. Please allow a few moments for the email to arrive.";

        public static final String mi9_fsn_key = "Fsn";
        public static final String mi9_first_name = "FirstName";
        public static final String mi9_last_name = "LastName";

        public static final String mi9_fName_value = "Valued";
        public static final String mi9_lName_value = "Customer";

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
        public static final String wf_sect_desc = "wf_sect_desc";
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


    public static class Mi9V8ItemLocator {
        //Wakefern's ItemLocator API can only take a certain number of UPC at a time
        //test the partition algorithm by using a small ITEM_PARTITION_SIZE + set log4j level to TRACE
        //the default ITEM_PARTITION_SIZE is 100
        public static final int ITEM_PARTITION_SIZE = 100;

        public static final String Aisle = "aisle";
        public static final String AisleStore = "aisleStore";
        public static final String AreaDesc = "wf_area_desc";
        public static final String Items = "items";
        public static final String LineItems = "lineItems";
        public static final String Other = "OTHER";
        public static final String Sku = "sku";
        public static final String AisleSectionDesc = "aisleSectionDesc";
        public static final String AisleAreaSeqNum = "aisleAreaSeqNum";
        public static final String AisleSectionShelfNum = "aisleSectionShelfNum";
        public static final String AisleShelfPositionNum = "aisleShelfPositionNum";
        public static final String ItemLocator = "itemLocator";
        public static final String Upc13Num = "upc_13_num";
        public static final String ItemLocations = "item_locations";
        public static final String AreaSeqNum = "area_seq_num";
        public static final String WfAreaCode = "wf_area_code";
        public static final String WfSectDesc = "wf_sect_desc";
        public static final String StoreAreaDesc = "area_desc";
        public static final String SectionShelfNum = "sect_shlf_num";
        public static final String ShelfPositionNum = "shlf_pstn_num";


        public static final String baseURL = "https://api.wakefern.com";
        public static final String locationPath = "/itemlocator/item/location";
        public static final String authPath = "/wfctoken/auth/gentoken";

    }

    public static class SmsEnrollment {
        public static class Proxy {
            public static final String Path = "users/{userId}/{ppc}/enrollments/sms/";
        }

        public static class ProxyV8 {
            public static final String Path = "users/{ppc}/enrollments/sms/";
        }

        public static class Upstream {
            public static final String BaseURL = APIM.apimBaseURL;
            public static final String MimeType = "application/vnd.wakefern.selection.api.v3+json";
            public static final String ApiVersion = "v1";
            public static final String ApiVersionHeaderKey = "Api-Version";

            public static final String enrollmentPath = "/selection-ecom/rest/ecom/sms/enrollment";
            public static final String orderSubscriptionPath = "/selection-ecom/rest/ecom/sms/registerOrder";
        }
    }

    public static class UpcomingOrders {
        public static class Proxy {
            public static final String Path = "users/{ppc}/pickup/upcoming-orders";
        }

        public static class Upstream {
            public static final String MimeType = "application/vnd.wakefern.selection.api.v3+json";
            public static final String upcomingOrdersPath = "/selection-ecom/rest/ecom/customer/upcomingOrders/";
        }

        public static class RequestParamsQuery {
            public static final String frequentShopperNumber = "frequentShopperNumber";
            public static final String fulfillmentDate = "fulfillmentDate";
        }
    }

    public static class CurbsideSession {
        public static class Proxy {
            public static final String Path = "users/{ppc}/pickup/curbside/session";
        }

        public static class Upstream {
            public static final String Path = "/selection-external/rest/ext/curbside/session";
        }

        public static class RequestParamsQuery {
            public static final String key = "key";
            public static final String orderNumber = "orderNumber";
            public static final String storeNumber = "storeNumber";
            public static final String frequentShopperNumber = "frequentShopperNumber";
        }
    }

    public static class JwtToken {
        // Wakefern's APIs
        public static final String JwtToken = "/jwtToken";
        public static final String TokenGen = "/generation";

        public static final String BaseURL = "https://api.wakefern.com";

        public static final String authPath = "/wfctoken/auth/gentoken";
    }

    public static class RecipeLocai {
        public static class Proxy {
            public static final String path = "/recipes/locai/";

            public static final String recipeTags = "recipeTags";
            public static final String lookupRecipes = "lookupRecipes";
            public static final String searchRecipes = "searchRecipes";
            public static final String completeRecipes = "completeRecipes";
            public static final String productLookup = "productLookup";
            public static final String productLookupBySkus = "productLookupBySkus";
            public static final String productDetails = "productDetails";
            public static final String getUserInfo = "getUserInfo";
            public static final String getUserProfile = "getUserProfile";
            public static final String setUserProfile = "setUserProfile";
            public static final String getUserFavorites = "getUserFavorites/users/{userId}";
            public static final String saveDishFavorite = "saveDishFavorite/users/{userId}/dishes/{dishId}";
            public static final String getRecipeCategories = "getRecipeCategories";
            public static final String getShelves = "shelves";
            public static final String recipeTracking = "recipeTracking";
            public static final String productSearchandizing = "productSearchandizing";
            public static final String homepageConfig = "homepageConfig";
            public static final String searchBySkus = "searchBySkus";
        }

        public static class RequestParamsQuery {
            public static final String sessionToken = "sessionToken";
            public static final String accountId = "accountId";
            public static final String clientBrand = "clientBrand";
        }

        public static class HeadersParams {
            public static final String auth = "Authorization";
            public static final String contentType = "Content-Type";

        }

        public static class RequestsParamsPath {
            public static final String userId = "userId";
            public static final String dishId = "dishId";
        }

        public static class Upstream {
            public static final String version_1 = "/v1";

            public static final String prodBaseURL = "https://cookit-api.locai.io" + version_1;
            public static final String stagingBaseURL = "https://cookit-api-stg.locai.io" + version_1;
        }
    }

    public static class Wallet {
        public static class Proxy {
            public static final String Path = "/wallet";
            public static final String GetWallet = "/userpass/{device}/{accountId}/{sessionToken}";
        }

        public static class Upstream {
            public static final String ProdBaseURL = "https://wfcapi.shoprite.com/passbook/api/v1/userpass";
            public static final String StageBaseURL = "https://wfcapi.staging.shoprite.com/passbook/api/v1/userpass";
        }

        public static class HeadersParams {
            public static final String Auth = "Authorization";
            public static final String ContentType = "Content-Type";
        }

        public static class RequestParamsPath {
            public static final String Device = "device";
            public static final String AccountId = "accountId";
            public static final String SessionToken = "sessionToken";
			public static final String CustomerId = "customerId";
        }
    }

    public static class HealthCheck {
        public static final String Path = "ping";
    }

    public static class Prodx {
        public static class Upstream {
            public static final String prodBaseUrl = "https://apis-main-two.prodx.com/";
            public static final String stagingBaseUrl = "https://apis-dev.prodx.com/";

        }

        public static class ProductsComplements {
            public static final String AISLE_ID = "aisleId";

            public static final String GetComplementsPath = "complements/v1.0/catalogs/WAKEFERN/products/{productId}/complements";

            public static class Proxy {
                public static final String GetProductComplements = "/products/{productId}/complements";
            }
        }
        public static class ProductsVariations {
        	public static final String ProductId = "productId";
        	
        	public static final String StoreId = "storeId";
        	public static final String PrimaryOnly = "primaryOnly";
        	public static final String Test = "test";
        	
        	public static final String GetVariationsPath = "variations/v1.0/catalogs/WAKEFERN/products/{productId}/variations";
        	
            public static class Proxy {
                public static final String GetProductVariations = "/products/{productId}/variations";
            }
        }
    }

    public static class RewardPoint {
        public static class Proxy {
            public static final String rewardPath = "/rewards/points";
        }

        // Wakefern home-grown API for reward point program
        public static class Upstream {
            public static final String Points = "/rewards/api/v1/points";
            public static final String baseURL = "https://wfcapi.shoprite.com";
            public static final String baseStagingURL = "https://wfcapi.staging.shoprite.com";
        }
    }


    public static class Reports {

        public static class Upstream {
            public static final String contextPath = "/notfound/api/v1";
            public static final String authenticate = contextPath + "/authenticate/user";
            public static final String product = contextPath + "/product";
        }

        public static class Proxy {
            public static final String NotFoundTokenURL = "/reports/notFound/token";
            public static final String NotFoundProductURL = "/reports/notFound/product";
        }
    }


    /*
        2022-08-02
        TODO: Once Sherry's Recommendation API with Mi9 V8 integration is ready, we would retest this Proxy API.
    */
    public static class Recommendations {
        public static final String ProductRecommendations = "/recommend/api/v1/products/user";

        public static final String ProductRecommendationsv2 = "/recommedation/api/v2/store";
        public static final String BaseRecommendationsURL = "https://wfcapi.shoprite.com";
//            public static final String UPCRecommendations = "/api/wfc/store/";
//            public static final String BaseUPCRecommendationsURL = "https://vp.shoprite.com";

    }

}