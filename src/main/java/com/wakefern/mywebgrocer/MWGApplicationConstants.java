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
    		
    		public static final String generic = "application/*";
    		public static final String json    = "application/json";

    		public static class Params {
    			public static final String auth = "Authorization";
    		}
    		
    		public static class Account {
    			public static final String login = prefix + "account-authentication+json";
    			
    			public static final String profile   = prefix + "account-profile-full+json";
    			public static final String register  = prefix + "account-profile-register+json";
    			public static final String prefStore = prefix + "account-online-shopping-store+json";
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
    			public static final String prepTimes    = prefix + "recipe-times+json";
    		}
    		
    		public static class Shop {
    			public static final String contact   = prefix + "customer-service-contact+json";
    			public static final String message   = prefix + "contact-message+json";
    			public static final String settings  = prefix + "shop-store-settings+json";
    			public static final String dashboard = prefix + "dashboard-entry+json";
    			
    			public static final String links = prefix + "links+json";
    			public static final String entry = prefix + "shop-entry+json";
    		}
    		
    		public static class ShoppingList {
    			public static final String list  = prefix + "list+json";
    			public static final String email = prefix + "list-email+json";
    			public static final String item  = prefix + "list-item+json";
    			public static final String items = prefix + "list-items+json";
    		}
    		
    		public static class Cart {
    			public static final String mergeGuest = prefix + "user-authenticated+json";
    			public static final String email      = prefix + "cart-email+json";
    			public static final String contents   = prefix + "grocery-list+json";
    			public static final String simpleItem = prefix + "simple-cart-item+json";
    			public static final String itemsV2    = prefix + "cart-items-v2+json";
    			public static final String item       = prefix + "cart-item+json";
    			public static final String items      = prefix + "cart-items+json";
    			public static final String review     = prefix + "cart-review+json";
    		}
    
    		public static class Checkout {
    			public static final String promoCodes     = prefix + "promo-code+json";
    			public static final String fulfillments   = prefix + "fulfillments+json";
    			public static final String fulfillOptsV2  = prefix + "fulfillment-options-v2+json";
    			public static final String fulfillDates   = prefix + "fulfillment-dates+json";  // Available fulfillment dates.
    			public static final String fulfillTimes   = prefix + "fulfillment-times+json";  // Available fulfillment times.
    			public static final String fulfillSlot    = prefix + "fulfillment-slot+json";   // User-specific fulfillment time slots.
    			public static final String deliveryInfoV2 = prefix + "delivery-info-v2+json";
    			
    			public static final String comments   = prefix + "comments+json";
    			public static final String links      = prefix + "links+json";
    			public static final String orders     = prefix + "orders+json";
    			public static final String orderV3    = prefix + "order-detail-v3+json";
    			public static final String addressV2  = prefix + "address-v2+json";
    			public static final String checkoutV2 = prefix + "checkout-v2+json";
    			public static final String paymentsV3 = prefix + "payments-v3+json";
    			
    			public static final String orderResults    = prefix + "order-results+json";
    			public static final String checkoutResults = prefix + "checkout-results+json";
    		}
    }
    
    public static class Requests {
        private static final String chainsID = "/chains/" + "{" + Params.Path.chainID + "}";
        private static final String usersID  = "/users/"  + "{" + Params.Path.userID  + "}";
        private static final String userID   = "/user/"   + "{" + Params.Path.userID  + "}";
		private static final String storeID  = "/store/"  + "{" + Params.Path.storeID + "}";
		private static final String storesID = "/stores/" + "{" + Params.Path.storeID + "}";

        // Request Parameters
        public static class Params {
        		
        		// Request Parameters included within the URL's path.
        		// As new path params are added here, corresponding code will need to be added to ServiceMappings.buildURL()
        		public static class Path {
        			public static final String token       = "token";
        		    public static final String itemID      = "itemId";
        		    public static final String chainID     = "chainId";
        		    public static final String userID      = "userId";
        		    public static final String storeID     = "storeId";
        		    public static final String orderID     = "orderId";
        		    public static final String regionID    = "region";
        		    public static final String zipCode     = "postalCode";
        		    public static final String parentCatID = "parentCategoryId";
        		    public static final String categoryID  = "categoryId";
        		    public static final String productSKU  = "sku";
        		    public static final String productID   = "productId";
        		    public static final String circularID  = "circularId";
        		    public static final String pageID      = "pageId";
        		    public static final String recipeID    = "recipeId";
        		    public static final String listID      = "listId";
        		    public static final String listItemID  = "listItemId";
        		    public static final String srcListID   = "sourceListId";
        		    public static final String mwgStoreID  = "pseudoStoreId";
        		    public static final String districtID  = "districtId";
        		    public static final String fulfillType = "fulfillment";  // As in:  'delivery' or 'pickup'
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
        		    public static final String prodsPerCat	= "productPerCategory";
        		    public static final String saleOnlyProds = "saleOnly";
        		    public static final String userID        = "userId";
        		    public static final String storeID       = "storeId";
        		    public static final String excluded      = "exclude";        // Products to exclude from the result set
        		    public static final String sortOrder     = "sort";           // Sort results by Brand, Price, or UnitPrice 
        		    public static final String searchBySound = "sound";          // What does this do?  Maybe if you yell your request really loudly, MWG will hear it?
        		    public static final String runState      = "runState";
        		    public static final String promotion     = "promotion";
        		    public static final String recipeGroup   = "recipeGroup";   
        		    public static final String categoryMap   = "categoryMap";
        		    public static final String evtParams     = "eventParameters";
        		}
        }
        
    		public static class Account {
    			public static final String prefix = "/account/v7";
    			
    			// Log in a registered user
    			public static final String login   = chainsID + "/authentication";
    			
    			// Get a user's full profile
    			public static final String profile = chainsID + usersID;
    			
    			// Register a new user
    			public static final String register = chainsID + "/users";
    		}
    	
        public static class Authentication {
        		public static final String prefix = "/authorization/v7";
        		
        		// Retrieve Session Token & Guest User ID
            public static final String authorize = "/authorization";
            
            // The "token" path param here is the "secret" provided by MWG to the Mobile App.
            public static final String checkout  = authorize + "/{" + Params.Path.token + "}/authenticate"; 
            public static final String delete    = authorize + "/{" + Params.Path.token + "}";
        }
        
        public static class Stores {
        		public static final String prefix = "/stores/v7";
        		
        		public static final String chains  = "/chains";
        		public static final String regions = chainsID + "/regions";
        		public static final String stores  = chainsID + "/stores";
        		
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
        		private static final String parentCatID = "/category/" + "{" + Params.Path.parentCatID + "}";
        		private static final String productID   = "/product/"  + "{" + Params.Path.productID   + "}";
        		private static final String productSKU  = "/sku/"      + "{" + Params.Path.productSKU  + "}";
        		
        		public static final String prefix = "/product/v7";
        		
        		public static final String categories       = "/categories" + storeID;
        		public static final String subCategories    = parentCatID   + storeID + "/categories";
        		public static final String catsWithSales    = categories    + sales;
        		public static final String subCatsWithSales = subCategories + sales;
        		
        		public static final String countries = chainsID + "/countries";
        		
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
        		        	
        		public static final String prefix = "/circulars/v7";
        		            
        		public static final String categories    = chainsID       + storeID + "/categories";
            public static final String category      = categories    + "/{" + Params.Path.categoryID  + "}";
            public static final String categoryItems = category      + items;
            public static final String categoryItem  = categoryItems + "/{" + Params.Path.itemID + "}";
            
            public static final String search = chainsID + storeID + items;
            
			public static final String circulars   = chainsID     + storeID + "/circulars";
    			public static final String circular    = circulars   + "/{" + Params.Path.circularID + "}";
    			public static final String fullPages   = circular    + "/pages";
    			public static final String pageDetails = fullPages   + "/{" + Params.Path.pageID + "}";
    			public static final String pageItems   = pageDetails + items;
    			public static final String pageItem    = pageItems   + "/{" + Params.Path.itemID + "}";
        }
        
        public static class Recipes {
        		private static final String recipes = "/recipes";
        		
        		private static final String id = "/{" + Params.Path.recipeID + "}";
        		
            public static final String prefix = "/recipes/v7";
            
            public static final String search   = chainsID + recipes;
            public static final String featured = search  + "/features";
            public static final String details  = search  + id;
            
            public static final String ingredients  = details + "/ingredients";
            public static final String emailRequest = details + "/emailRequest";
            public static final String instructions = details + "/directions";
            public static final String nutritional  = details + "/nutrients";
            public static final String prepTimes    = details + "/times";
            
            public static final String categories  = chainsID    + "/categories";
            public static final String searchByCat = categories + "/{" + Params.Path.categoryID + "}" + recipes;
            
            public static final String userRecipes  = chainsID + usersID + recipes;
            public static final String updateRecipe = userRecipes + id;
        }
        
        public static class Shop {
        		private static final String shop    = "/shop";
        		private static final String storeID = "/store" + "/{" + Params.Path.storeID + "}";
        	
            public static final String prefix = "/shop/v7";
            
            public static final String contact  = shop + storeID + "/contact";
            public static final String settings = shop + storeID + "/settings";
            
            public static final String entryByChain = shop + usersID + chainsID + "/entry";
            public static final String entryByStore = shop + userID  + storeID;

            public static final String message = contact + "/messages";            

            public static final String dashboard = shop + userID + storeID + "/dashboard";
        }
        
        public static class ShoppingList {
        		public static final String prefix = "/shoppinglist/v7";
        		
        		public static final String lists = chainsID + usersID + "/lists";
        		
        		public static final String list  = lists + "/{" + Params.Path.listID + "}";
        		public static final String items = list  + "/items";
        		public static final String item  = items + "/{" + Params.Path.listItemID + "}";
        		public static final String copy  = list  + "/duplicate";
        }
        
        public static class Cart {
        		private static final String itemID = "/item" + "/{" + Params.Path.itemID + "}";
        		
        		public static final String prefix = "/cart/v7";
    			
        		// Merge the cart of Guest user, into the now-authenticated user's cart.
    			public static final String mergeGuest = "/user/authenticated";
    			
    			public static final String cart = userID + storeID;
    			
    			public static final String item   = cart + itemID;
    			public static final String items  = cart + "/items"; 
    			public static final String review = cart + "/review";
    			
    			public static final String mergeList = cart + "/merge-from/list" + "/{" + Params.Path.srcListID + "}";
        }
        
        public static class Checkout {
        		private static final String fflmnts = "/fulfillments";
        		private static final String fflmnt  = "/fulfillment";
        		private static final String dates   = "/dates";
        		private static final String times   = "/times";
        		private static final String addr    ="/address";
        		
        		private static final String mwgStoreID  = "/store/"    + "{" + Params.Path.mwgStoreID + "}";
        		private static final String mwgStoresID = "/stores/"   + "{" + Params.Path.mwgStoreID + "}";
        		private static final String deliveryZip = "/delivery/" + "{" + Params.Path.zipCode    + "}";
        		private static final String districtID  = "/district/" + "{" + Params.Path.districtID + "}";
        		private static final String orderID     = "/order/"    + "{" + Params.Path.orderID    + "}";
        		
        		public static final String prefix ="/checkout/v7";
        		
        		public static final String promoCodes = chainsID + storesID + usersID + "/promocodes";
        		
        		public static final String fulfillOpts   = fflmnts      + mwgStoreID;
        		public static final String deliveryInfo  = fulfillOpts  + deliveryZip;
        		public static final String deliveryDates = deliveryInfo + dates;
        		public static final String deliveryTimes	= deliveryInfo + times;
        		public static final String pickupInfo    = fflmnts      + mwgStoreID + "/pickup";
        		public static final String pickupDates   = pickupInfo   + dates;
        		public static final String pickupTimes   = pickupInfo   + times;
        		
        		public static final String deliveryDistrictDates = deliveryInfo + districtID + dates;
        		public static final String deliveryDistrictTimes = deliveryInfo + districtID + times;
        		
        		public static final String userCheckoutState = userID  + mwgStoreID;
        		public static final String reservedTimeslot  = usersID + mwgStoresID + "/reserved-timeslot";
        		
        		public static final String deliveryAddr    = userCheckoutState + addr + "/delivery";
        		public static final String billingAddr     = userCheckoutState + addr + "/billing";
        		public static final String comments        = userCheckoutState + "/comments";
        		public static final String timeslot        = userCheckoutState + fflmnt;
        		public static final String userFulfillOpts = userCheckoutState + fflmnts;
        		
        		public static final String orders = "/orders" + userID + mwgStoreID;
        		public static final String order  = orderID   + userID;
        		
        		public static final String payments = "/payments" + mwgStoreID + "/{" + Params.Path.fulfillType + "}";
        		
        		public static final String paymentOpts = userID + mwgStoreID + "/payment";
        		
        		// Legacy.  Used by com.wakefern.payment.GetToken
            public static final String UserCheckout = "/checkout/v5/user";
        }

        public static class Rewards {
        	
        		// Legacy endpoints, still in use.
        		public static final String Points = "/rewards/api/v1/points";
        		public static final String baseURL = "https://wfcapi.shoprite.com";
        }
        
        public static class Wakefern {
            public static final String ItemLocator = "/itemlocator/item/location";
            public static final String ItemLocatorAuth = "/wfctoken/auth/gentoken";
            public static final String ItemLocatorJson = "/itemlocator/item/location/json";
        }
        
        public static class Recommendations {
	        	public static final String ProductRecommendations = "/recommend/api/v1/products/user";
	        	public static final String baseURL = "https://wfcapi.shoprite.com";
	        	public static final String UPCRecommendations = "/api/wfc/store/";
	        	public static final String baseUPC_URL = "https://vp.shoprite.com";
        }
    }
}
