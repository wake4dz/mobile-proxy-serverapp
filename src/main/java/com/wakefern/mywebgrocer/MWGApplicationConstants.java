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
        
    public static final String baseURLv1	   = "https://api.shoprite.com/api/v1";
    public static final String serviceURLv1 = "https://service.shoprite.com";
    public static final String appToken     = "62081B21-0885-4544-8849-326195C8F9CD";
    
    public static class Headers {
    		private static final String prefix = "application/vnd.mywebgrocer.";
    		
    		public static class Params {
    			public static final String auth = "Authorization";
    		}
    		
    		public static class Account {
    			public static final String login = prefix + "account-authentication+json";
    			
    			public static final String fullProfile  = prefix + "account-profile-full+json";
    			public static final String basicProfile = prefix + "account-profile-basic+json";
    		}
    		
    		public static class Stores {
    			public static final String details  = prefix + "store+json";
    			public static final String chains   = prefix + "chains+json";
    			public static final String regions  = prefix + "stores-regions+json";
    			public static final String cities   = prefix + "stores-region-cities+json";
    			public static final String stores   = prefix + "stores+json";
    			public static final String delivers = prefix + "store-delivers-to+json";
    		}
    		
    		public static class Products {
    			public static final String categories  = prefix + "category+json";
    			public static final String countries   = prefix + "product-country-of-origin+json";
    			public static final String productList = prefix + "product-list+json";
    			public static final String product     = prefix + "product+json";
    		}
    		
    		public static class Circulars {
    			public static final String circulars  = prefix + "circular+json";
    			public static final String categories = prefix + "circular-category+json";
    			public static final String details    = prefix + "circular-detail+json";
    			public static final String fullPages  = prefix + "circular-pages-full+json";
    			
    			public static final String page  = prefix + "circular-page+json";
    			public static final String items = prefix + "circular-items+json";
    			public static final String item  = prefix + "circular-item+json";
    		}
    		
    		public static class Recipes {
    			public static final String recipes = prefix + "recipes+json"; 
    			public static final String recipe  = prefix + "recipe+json";

    			public static final String categories   = prefix + "recipe-category+json";
    			public static final String instructions = prefix + "recipe-directions+json";
    			public static final String emailRequest = prefix + "recipe-email+json";
    			public static final String ingredients  = prefix + "recipe-ingredients+json";
    			public static final String nutritional  = prefix + "recipe-nutrients+json";
    		}
    }
    
    public static class Requests {
        private static final String chainID = "/chains/" + "{" + Params.Path.chainID + "}";	

        // Request Parameters
        public static class Params {
        		
        		// Request Parameters included within the URL's path.
        		public static class Path {
        		    public static final String itemID      = "itemId";
        		    public static final String chainID     = "chainId";
        		    public static final String userID      = "userId";
        		    public static final String storeID     = "storeId";
        		    public static final String regionID    = "region";
        		    public static final String zipCode     = "postalCode";
        		    public static final String parentCatID = "parentCategoryId";
        		    public static final String categoryID  = "categoryId";
        		    public static final String productSKU  = "sku";
        		    public static final String productID   = "productId";
        		    public static final String circularID  = "circularId";
        		    public static final String pageID      = "pageId";
        		    public static final String recipeID    = "recipeId";
        		}
        		
        		// Request Parameters that are part of the URL's query string.
        		public static class Query {
        		    public static final String skip          = "skip";
        		    public static final String take          = "take";
        		    public static final String services      = "mwgService";
        		    public static final String city          = "city";
        		    public static final String coords        = "coordinates";     // Geolocation coordinates.
        		    public static final String toZip         = "deliversTo";      // Is this ZIP within a given store's delivery area?
        		    public static final String filters       = "fq";
        		    public static final String ipAddr        = "ip";
        		    public static final String zipCode       = "postalCode";
        		    public static final String searchTerm    = "q";
        		    public static final String radius        = "radius";          // Store Search radius. In meters or kilometers.
        		    public static final String unitOfMeasure = "unitOfMeasure";   // Unit of measure (m or km) for Store Search radius.
        		    public static final String regionCode    = "region";
        		    public static final String storeOwnerID  = "storeownerId";
        		    public static final String storeStatus   = "storeStatus";
        		    public static final String isMember      = "isMember";
        		    public static final String prodsPerCat	 = "productPerCategory";
        		    public static final String saleOnlyProds = "saleOnly";
        		    public static final String userID        = "userId";
        		    public static final String storeID       = "storeId";
        		    public static final String excluded      = "exclude";        // Products to exclude from the result set
        		    public static final String sortOrder     = "sort";           // Sort results by Brand, Price, or UnitPrice 
        		    public static final String searchBySound = "sound";          // What does this do?  Maybe if you yell your request really loudly, MWG will hear it?
        		    public static final String runState      = "runState";
        		    public static final String promotion     = "promotion";
        		    public static final String recipeGroup   = "recipeGroup";        		    
        		}
        }
        
    		public static class Account {
    			public static final String acctPath = "/account/v7";
    			
    			// Log in a registered user
    			public static final String login   = chainID + "/authentication";
    			
    			// Get a user's full profile
    			public static final String profile = chainID + "/users/" + "{" + Params.Path.userID + "}";
    		}
    	
        public static class Authentication {
            public static final String authenticate = "/authorization/v7/authorization";  // Retrieve Session Token & Guest User ID
        }
        
        public static class Stores {
        		public static final String storesPath = "/stores/v7";
        		
        		public static final String chains  = "/chains";
        		public static final String regions = chainID + "/regions";
        		public static final String stores  = chainID + "/stores";
        		
        		public static final String cities   = regions + "/{" + Params.Path.regionID + "}/cities";
        		public static final String details  = stores  + "/{" + Params.Path.storeID + "}";
        		public static final String delivers = details + "/delivers-to/{" + Params.Path.zipCode + "}";
        		
            public static final String StoreLocator     = "/storelocator/v1/chain";
            public static final String ShoppingListUser = "/shoppinglists/v1/user/";
        }
        
        public static class Products {
	    		private static final String products  = "/products";
	    		private static final String product   = "/product";
	    		private static final String nutrition = "/nutrition";
	    		private static final String sales     = "/special";
	    		private static final String featured  = "/featured";

	    		private static final String categoryID  = "/category/" + "{" + Params.Path.categoryID  + "}";
    			private static final String storeID     = "/store/"    + "{" + Params.Path.storeID     + "}";
        		private static final String parentCatID = "/category/" + "{" + Params.Path.parentCatID + "}";
        		private static final String productID   = "/product/"  + "{" + Params.Path.productID   + "}";
        		private static final String productSKU  = "/sku/"      + "{" + Params.Path.productSKU  + "}";
        		
        		public static final String productPath = "/product/v7";
        		
        		public static final String categories       = "/categories" + storeID;
        		public static final String subCategories    = parentCatID   + storeID + "/categories";
        		public static final String catsWithSales    = categories    + sales;
        		public static final String subCatsWithSales = subCategories + sales;
        		
        		public static final String countries = chainID + "/countries";
        		
        		public static final String prodByID     = productID + storeID;
        		public static final String prodsByCat   = products  + categoryID + storeID;
        		public static final String prodBySKU    = product   + storeID    + productSKU;
        		public static final String prodVarsByID = productID + storeID    + "/all/variations";
        		public static final String prodSearch   = products  + storeID    + "/search";
        		
        		public static final String nutritionBySKU = product   + storeID + productSKU + nutrition;
        		public static final String nutritionByID  = productID + storeID + nutrition;
        		
        		public static final String suggestedProds       = products + storeID    + "/suggest";
        		public static final String featuredProdsByStore = products + storeID    + featured;
        		public static final String featuredProdsByCat   = products + categoryID + storeID + featured;
        		public static final String saleItemsByCat       = products + categoryID + storeID + sales;
        }
        
        public static class Circulars {
        		private static final String items   = "/items";
        		private static final String storeID = "/stores/" + "{" + Params.Path.storeID + "}";
        		        	
        		public static final String circularsPath = "/circulars/v7";
        		            
        		public static final String categories    = chainID       + storeID + "/categories";
            public static final String category      = categories    + "/{" + Params.Path.categoryID  + "}";
            public static final String categoryItems = category      + items;
            public static final String categoryItem  = categoryItems + "/{" + Params.Path.itemID + "}";
            
            public static final String search = chainID + storeID + items;
            
			public static final String circulars   = chainID     + storeID + "/circulars";
    			public static final String circular    = circulars   + "/{" + Params.Path.circularID + "}";
    			public static final String fullPages   = circular    + "/pages";
    			public static final String pageDetails = fullPages   + "/{" + Params.Path.pageID + "}";
    			public static final String pageItems   = pageDetails + items;
    			public static final String pageItem    = pageItems   + "/{" + Params.Path.itemID + "}";
        }
        
        public static class Recipes{
        		private static final String recipes = "/recipes";
        		
            public static final String recipesPath = "/recipes/v7";
            
            public static final String search   = chainID + recipes;
            public static final String featured = search  + "/features";
            public static final String details  = search  + "/{" + Params.Path.recipeID + "}";
            
            public static final String ingredients  = details + "/ingredients";
            public static final String emailRequest = details + "/emailRequest";
            public static final String instructions = details + "/directions";
            public static final String nutritional  = details + "/nutrients";
            
            public static final String categories  = chainID    + "/categories";
            public static final String searchByCat = categories + "/{" + Params.Path.categoryID + "}" + recipes;
        }
        
        // ^^^ NEW STUFF ^^^ \\

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
