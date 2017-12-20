package com.wakefern.mywebgrocer;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGApplicationConstants {
    public static final String fgProdBaseURL  = "https://api.thefreshgrocer.com/api";                 // Prod
    public static final String fgStageBaseURL = "https://api-fg75stg.staging.thefreshgrocer.com/api"; // Staging
    
    // TODO: CHANGE TO PROD BEFORE RELEASING!!!
    //
    public static final String baseURL = fgStageBaseURL;
    
    public static final String pathChainID     = "chainId";
    public static final String pathUserID      = "userId";
    public static final String pathStoreID     = "storeId";
    public static final String pathRegionID    = "region";
    public static final String pathZipCode     = "postalCode";
    public static final String pathParentCatID = "parentCategoryId";
    public static final String pathCategoryID  = "categoryId";
    public static final String pathProductSKU  = "sku";
    public static final String pathProductID   = "productId";
    
    public static final String querySkip          = "skip";
    public static final String queryTake          = "take";
    public static final String queryServices      = "mwgService";
    public static final String queryCity          = "city";
    public static final String queryCoords        = "coordinates";     // Geolocation coordinates.
    public static final String queryToZip         = "deliversTo";      // Is this ZIP within a given store's delivery area?
    public static final String queryFilters       = "fq";
    public static final String queryIpAddr        = "ip";
    public static final String queryZipCode       = "postalCode";
    public static final String querySearchTerm    = "q";
    public static final String queryRadius        = "radius";          // Store Search radius. In meters or kilometers.
    public static final String queryUnitOfMeasure = "unitOfMeasure";   // Unit of measure (m or km) for Store Search radius.
    public static final String queryRegionCode    = "region";
    public static final String queryStoreOwnerID  = "storeownerId";
    public static final String queryStoreStatus   = "storeStatus";
    public static final String queryIsMember      = "isMember";
    public static final String queryProdsPerCat	 = "productPerCategory";
    public static final String querySaleOnlyProds = "saleOnly";
    public static final String queryUserID        = "userId";
    public static final String queryStoreID       = "storeId";
    public static final String queryExcluded      = "exclude";        // Products to exclude from the result set
    public static final String querySortOrder     = "sort";           // Sort results by Brand, Price, or UnitPrice 
    public static final String querySeachBySound  = "sound";          // What does this do?  Maybe if you yell your request really loudly, MWG will hear it?
    
    public static final String baseURLv1	   = "https://api.shoprite.com/api/v1";
    public static final String serviceURLv1 = "https://service.shoprite.com";
    public static final String appToken     = "62081B21-0885-4544-8849-326195C8F9CD";

    private static final String chainsPath = "/chains/{" + pathChainID + "}";	
    private static final String usersPath  = "/users/{"  + pathUserID  + "}";	
    
    public static class Headers {
    		public static class Params {
    			public static final String auth = "Authorization";
    		}
    		
    		public static class Account {
    			public static final String login = "application/vnd.mywebgrocer.account-authentication+json";
    			
    			public static final String fullProfile  = "application/vnd.mywebgrocer.account-profile-full+json";
    			public static final String basicProfile = "application/vnd.mywebgrocer.account-profile-basic+json";
    		}
    		
    		public static class Stores {
    			public static final String details  = "application/vnd.mywebgrocer.store+json";
    			public static final String chains   = "application/vnd.mywebgrocer.chains+json";
    			public static final String regions  = "application/vnd.mywebgrocer.stores-regions+json";
    			public static final String cities   = "application/vnd.mywebgrocer.stores-region-cities+json";
    			public static final String stores   = "application/vnd.mywebgrocer.stores+json";
    			public static final String delivers = "application/vnd.mywebgrocer.store-delivers-to+json";
    		}
    		
    		public static class Products {
    			public static final String categories  = "application/vnd.mywebgrocer.category+json";
    			public static final String countries   = "application/vnd.mywebgrocer.product-country-of-origin+json";
    			public static final String productList = "application/vnd.mywebgrocer.product-list+json";
    			public static final String product     = "application/vnd.mywebgrocer.product+json";
    		}
    }
    
    public static class Requests {
    		public static class Account {
    			public static final String acctPath = "/account/v7";
    			
    			public static final String login   = chainsPath + "/authentication";  // Log in a registered user
    			public static final String profile = chainsPath + usersPath;
    		}
    	
        public static class Authentication {
            public static final String authenticate = "/authorization/v7/authorization";  // Retrieve Session Token & Guest User ID
        }
        
        public static class Stores {
        		public static final String storesPath = "/stores/v7";
        		
        		public static final String chains  = "/chains";
        		public static final String regions = chainsPath + "/regions";
        		public static final String stores  = chainsPath + "/stores";
        		
        		public static final String cities   = regions + "/{" + pathRegionID + "}/cities";
        		public static final String details  = stores  + "/{" + pathStoreID + "}";
        		public static final String delivers = details + "/delivers-to/{" + pathZipCode + "}";
        		
            public static final String StoreLocator     = "/storelocator/v1/chain";
            public static final String ShoppingListUser = "/shoppinglists/v1/user/";
        }
        
        public static class Products {
	    		private static final String prodsPath = "/products";
	    		private static final String prodPath  = "/product";
	    		private static final String catsPath  = "/categories";
	    		private static final String nutriPath = "/nutrition";
	    		private static final String salesPath = "/special";
	    		private static final String featPath  = "/featured";

    			private static final String storePath   = "/store/"    + "{" + pathStoreID     + "}";
        		private static final String catPath     = "/category/" + "{" + pathCategoryID  + "}";
        		private static final String catTreePath = "/category/" + "{" + pathParentCatID + "}";
        		private static final String prodIdPath  = "/product/"  + "{" + pathProductID   + "}";
        		private static final String skuPath     = "/sku/"      + "{" + pathProductSKU  + "}";
        		
        		public static final String productPath = "product/v7";
        		
        		public static final String categories       = catsPath      + storePath;
        		public static final String subCategories    = catTreePath   + storePath + catsPath;
        		public static final String catsWithSales    = categories    + salesPath;
        		public static final String subCatsWithSales = subCategories + salesPath;
        		
        		public static final String countries = chainsPath + "/countries";
        		
        		public static final String prodsByCat   = prodsPath  + catPath   + storePath;
        		public static final String prodBySKU    = prodPath   + storePath + skuPath;
        		public static final String prodByID     = prodIdPath + storePath;
        		public static final String prodVarsByID = prodIdPath + storePath + "/all/variations";
        		
        		public static final String nutritionBySKU = prodPath   + storePath + skuPath + nutriPath;
        		public static final String nutritionByID  = prodIdPath + storePath + nutriPath;
        		
        		public static final String featuredProdsByStore = prodsPath + storePath + featPath;
        		public static final String featuredProdsByCat   = prodsPath + catPath   + storePath + featPath;
        		public static final String saleItemsByCat       = prodsPath + catPath   + storePath + salesPath;
        }
        
        // ^^^ NEW STUFF ^^^ \\

        public static class Categories {
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
