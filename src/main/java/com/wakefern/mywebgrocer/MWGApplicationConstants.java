package com.wakefern.mywebgrocer;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGApplicationConstants {
    public static final String fgProdBaseURL  = "https://api.thefreshgrocer.com/api";                 // Prod
    public static final String fgStageBaseURL = "https://api-fg75stg.staging.thefreshgrocer.com/api"; // Staging
    
    public static String baseURL = fgStageBaseURL;  // TODO: Change this so it defaults to the PROD URL before releasing!
    
    public static final String chainID = "chainId";
    public static final String userID  = "userId";
    
    public static final String baseURLv1		= "https://api.shoprite.com/api/v1";
    public static final String serviceURLv1	= "https://service.shoprite.com";
    public static final String authToken		= "FE8803F0-D4FA-4AFF-B688-1A3BD5915FAA";
    public static final String appToken		= "62081B21-0885-4544-8849-326195C8F9CD";
    public static final String storeGroupId	= "3601";
    public static final String storeId		= "C627119202";

    private static final String chainsPath = "/chains/{" + chainID + "}";	
    
    public static class Headers {
    		public static class Account {
    			public static final String authContentType = "application/vnd.mywebgrocer.account-authentication+json";
    		}
    }
    
    public static class Requests {
    		public static class Account {
    			public static final String acctPath = "/account/v7";
    			public static final String authPath = chainsPath + "/authentication";
    		}
    	
        public static class Authentication {
            public static final String authenticate   = "/authorization/v7/authorization";        		
        }

        public static class Categories {
            public static final String CategoriesFromStoreId = "/product/v5/categories/store";
            public static final String SubCategories = "/product/v5/category";
            public static final String ProductId = "/product/v5/product";
            public static final String ProductStore = "/product/v5/product/store";
            public static final String ProductsStore = "/product/v5/products/store";
            public static final String ProductCategory = "/product/v5/products/category";
        }

        public static class Circular{
            public static final String Categories = "/circulars/v5/chains";
        }

        public static class Checkout {
            public static final String Checkout = "/checkout/v5/fulfillments/store";
            public static final String Payments = "/checkout/v5/payments/store";
            public static final String UserCheckout = "/checkout/v5/user";
            public static final String Users = "/checkout/v5/users";
            public static final String User = "/checkout/v5/user";
            public static final String UserOrder = "/checkout/v5/orders/user";
            public static final String Order = "/checkout/v5/order";
        }

        public static class Cart {
            public static final String CartAuth = "/cart/v5/user/authenticated";
            public static final String CartUser = "/cart/v5/user";
        }

        public static class Shop{
            public static final String ShopStore = "/shop/v5/shop/store";
            public static final String ShopUser = "/shop/v5/shop/user";
        }

        public static class ShoppingLists{
            public static final String slChains = "/shoppinglist/v5/chains";
            public static final String slUser = "/shoppinglist/v5/user";
            public static final String slItemsUser = "/shoppinglist/v5/items/user";
            public static final String slGeneric = "/shoppinglist/v5/generic";
        }

        public static class Recipes{
            public static final String RecipeChain = "/recipes/v1/chain";
            public static final String UpdateProfile = "/user/v1/id";
        }

        public static class Registration{
            public static final String UserRegistration = "/user/v1/registration";
        }

        public static class Planning{
            public static final String StoreLocator = "/storelocator/v1/chain";
            public static final String ShoppingListUser = "/shoppinglists/v1/user/";
        }

        public static class Wakefern{
            public static final String ItemLocator = "/itemlocator/item/location";
            public static final String ItemLocatorAuth = "/wfctoken/auth/gentoken";
            public static final String ItemLocatorJson = "/itemlocator/item/location/json";
        }
        
        public static class Recommendations{
        	public static final String ProductRecommendations = "/recommend/api/v1/products/user";
        	public static final String baseURL = "https://wfcapi.shoprite.com";
        	public static final String UPCRecommendations = "/api/wfc/store/";
        	public static final String baseUPC_URL = "https://vp.shoprite.com";
        }
        
        public static class Rewards{
        	public static final String Points = "/rewards/api/v1/points";
        	public static final String baseURL = "https://wfcapi.shoprite.com";
        }
    }
}
