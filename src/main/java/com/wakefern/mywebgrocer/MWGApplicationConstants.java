package com.wakefern.mywebgrocer;

import com.wakefern.wakefern.WakefernApplicationConstants;

public class MWGApplicationConstants {
	private static final String shopRiteProd = "https://mobileapi.shoprite.com/api"; // ShopRite Production
	private static final String shopRiteProdWeb = "https://api.shoprite.com/api"; // ShopRite Production
	
	//TODO 
	//    There is no reliable ShopRiteDev environment from MWG, so we disabled all dev related configurations.
	//    Note: if chain=ShopRiteStage, it is pointed to the mobile version regardless what "url" is set
	//    This Java file needs to be re-factored in the future to streamline the process
	
	//private static final String shopRiteStageWeb = "https://api-sr75stg.staging.shoprite.com/api";//"https://api-sr75stg.staging.shoprite.com/api"; // ShopRite Staging
	private static final String shopRiteStage = "https://mobileapi-sr75stg.staging.shoprite.com/api";//"https://api-sr75stg.staging.shoprite.com/api"; // ShopRite Staging
	
	//private static final String shopRiteDev = "https://api.dev.shoprite.com/api"; // ShopRite Development
	
	private static final String freshGrocerProd = "https://api.thefreshgrocer.com/api"; // FreshGrocer Production
	private static final String freshGrocerStage = "https://api-fg75stg.staging.thefreshgrocer.com/api"; // FreshGrocer
																											// Staging

	private static final String freshGrocerStageToken = "62081B21-0885-4544-8849-326195C8F9CD";
	//private static final String shopRiteDevToken = "56BF02A1-074E-4767-9801-73447DC64928";
	private static final String shopRiteStageToken = "13B4312E-D431-4C53-A16C-5998B2F53482";
	private static final String shopRiteProdToken = "b5e4d368-d5ee-47ce-9c7d-423de1c879e6";

	private static final String fgStage = "FreshGrocerStage";
	private static final String fgProd = "FreshGrocerProd";
	private static final String srStage = "ShopRiteStage";
	private static final String srProd = "ShopRiteProd";
	//private static final String srDev = "ShopRiteDev";
	
	//By default, mobile is calling mobileapi.shoprite.com, when value 'web' is specified
	//	in vcap env var 'url', mobile will call api.shoprite.com
	private static final String mwgWeb = "web";

	/**
	 * Return the appropriate Base URL.
	 * 
	 * @return String
	 */
	public static String getBaseURL() {
		String baseURL;

		String targetAPI = getTargetAPI();

		switch (targetAPI) {
		case fgStage:
			baseURL = freshGrocerStage;
			break;
		case fgProd:
			baseURL = freshGrocerProd;
			break;
		case srStage:
			baseURL = shopRiteStage;
			break;
		case srProd:
			baseURL = getTargetProdURL();
			break;
//		case srDev:
//			baseURL = shopRiteDev;
//			break;

		default:
			baseURL = shopRiteProd;
		}

		return baseURL;
	}
	
	public static String getSRWebURL(){
		return shopRiteProdWeb;
	}

	/**
	 * Return the appropriate Application Token.
	 * 
	 * @return String
	 */
	public static String getAppToken() {
		String appToken;

		String targetAPI = getTargetAPI();

		switch (targetAPI) {
		case fgStage:
		case fgProd:
			appToken = freshGrocerStageToken;
			break;

//		case srDev:
//			appToken = shopRiteDevToken;
//			break;
		case srStage:
			appToken = shopRiteStageToken;
			break;
		case srProd:
			appToken = shopRiteProdToken;
			break;

		default:
			appToken = shopRiteProdToken;
		}

		return appToken;
	}

	/**
	 * Return auth token for The Fresh Grocer or ShopRite
	 * 
	 * @return
	 */
	public static String getProductRecmdAuthToken() {
		String appToken;
		String targetAPI = getTargetAPI();
		
		appToken = (targetAPI.equals(fgStage) || targetAPI.equals(fgProd)) 
				? WakefernApplicationConstants.tfgAuthToken
				: WakefernApplicationConstants.srAuthToken;

		return appToken;
	}

	/**
	 * Check the Bluemix Environment Variable, indicating which MWG API this
	 * instance of the Wakefern Java API should be talking to.
	 * 
	 * @return String
	 */
	public static String getTargetAPI() {
		String targetAPI = java.lang.System.getenv("chain");

		// On a local Dev server, the environment variable won't exist unless you have
		// specifically set it up yourself.
		// In that case, default to one of the dev targets.
		//
		// ShopRite Dev : srDev
		// ShopRite Staging : srStage
		// FreshGrocer Staging : fgStage
		targetAPI = (targetAPI == null) ? srStage : targetAPI;

		 return targetAPI;
	}

	/**
	 * Retrieve the current brand mode based on the current MWG target API.
	 *
	 * @return
	 */
	public static String getApplicationMode() {
		String targetAPI = getTargetAPI();
		return targetAPI.equals(fgStage) || targetAPI.equals(fgProd)
			? WakefernApplicationConstants.Chains.FreshGrocer
			: WakefernApplicationConstants.Chains.ShopRite;
	}
	
	
	/**
	 * The generic way to get the system property value
	 */
	public static String getSystemProperytyValue(String key) {
		return  java.lang.System.getenv(key.trim());
	
	}
	
	/**
	 * Check the Bluemix Environment Variable, indicating which MWG API this
	 * instance of the Wakefern Java API should be talking to.
	 * 
	 * @return String
	 */
	private static String getTargetProdURL() {
		String targetProdURL = java.lang.System.getenv("url");

		// Assigning mwg production api for cloud server to call..
		//
		// ShopRite Prod Mobile or other keywords: https://mobileapi.shoprite.com/api
		// ShopRite Prod Web : https://api.shoprite.com/api
		targetProdURL = (targetProdURL != null && targetProdURL.equalsIgnoreCase(mwgWeb)) ? shopRiteProdWeb : shopRiteProd;

		return targetProdURL;
	}

	public static class Headers {
		private static final String prefix = "application/vnd.mywebgrocer.";

		public static final String generic = "application/*";
		public static final String json = "application/json";

		public static class Params {
			public static final String auth = "Authorization";
			public static final String accept = "Accept";
      
			public static final String contentType = "Content-Type";
		}

		public static class Account {
			public static final String login = prefix + "account-authentication+json";

			public static final String profile = prefix + "account-profile-full+json";
			public static final String register = prefix + "account-profile-register+json";
			public static final String prefStore = prefix + "account-online-shopping-store+json";
		}

		public static class Stores {
			public static final String details = prefix + "store+json";
			public static final String chains = prefix + "chains+json";
			public static final String regions = prefix + "stores-regions+json";
			public static final String cities = prefix + "stores-region-cities+json";
			public static final String stores = prefix + "stores+json";
			public static final String delivers = prefix + "store-delivers-to+json";
		}

		public static class Products {
			public static final String categories = prefix + "category+json";
			public static final String categoriesPlugin = prefix + "category-plugin+json";
			public static final String countries = prefix + "product-country-of-origin+json";
			public static final String productList = prefix + "product-list+json";
			public static final String product = prefix + "product+json";
			public static final String accept = prefix + "grocery-list+json";

			// This is a Products header, but it's used by the Circular functionality.
			public static final String circItemProds = prefix + "grocery-list-plugin+json";
		}

		public static class Circulars {
			public static final String circulars = prefix + "circular+json";
			public static final String categories = prefix + "circular-category+json";
			public static final String details = prefix + "circular-detail+json";
			public static final String fullPages = prefix + "circular-pages-full+json";

			public static final String page = prefix + "circular-page+json";
			public static final String items = prefix + "circular-items+json";
			public static final String item = prefix + "circular-item+json";
		}

		public static class Recipes {
			public static final String recipes = prefix + "recipes+json";
			public static final String recipe = prefix + "recipe+json";

			public static final String categories = prefix + "recipe-category+json";
			public static final String instructions = prefix + "recipe-directions+json";
			public static final String emailRequest = prefix + "recipe-email+json";
			public static final String ingredients = prefix + "recipe-ingredients+json";
			public static final String nutritional = prefix + "recipe-nutrients+json";
			public static final String prepTimes = prefix + "recipe-times+json";
		}

		public static class Shop {
			public static final String contact = prefix + "customer-service-contact+json";
			public static final String message = prefix + "contact-message+json";
			public static final String settings = prefix + "shop-store-settings+json";
			public static final String dashboard = prefix + "dashboard-entry+json";

			public static final String links = prefix + "links+json";
			public static final String entry = prefix + "shop-entry+json";
		}

		public static class ShoppingList {
			public static final String list = prefix + "list+json";
			public static final String email = prefix + "list-email+json";
			public static final String item = prefix + "list-item+json";
			public static final String items = prefix + "list-items+json";
			public static final String wakefernItems = prefix + "wakefern-list-items+json";
		}

		public static class Cart {
			public static final String mergeGuest = prefix + "user-authenticated+json";
			public static final String email = prefix + "cart-email+json";
			public static final String contents = prefix + "grocery-list+json";
			public static final String simpleItem = prefix + "simple-cart-item+json";
			public static final String itemsV2 = prefix + "cart-items-v2+json";
			public static final String item = prefix + "cart-item+json";
			public static final String items = prefix + "cart-items+json";
			public static final String review = prefix + "cart-review+json";
		}

		public static class Checkout {
			public static final String promoCodes = prefix + "promo-code+json";
			public static final String substitutions = prefix +"substitutions+json";
			public static final String userFSN = prefix + "frequent-shopper-number+json";
			public static final String comments = prefix + "comments+json";
			public static final String links = prefix + "links+json";

			public static final String fulfillments = prefix + "fulfillments+json";
			public static final String fulfillOptsV2 = prefix + "fulfillment-options-v2+json";
			public static final String fulfillOpts = prefix + "fulfillment-options+json";
			public static final String fulfillDates = prefix + "fulfillment-dates+json"; // Available fulfillment dates.
			public static final String fulfillTimes = prefix + "fulfillment-times+json"; // Available fulfillment times.
			public static final String fulfillSlot = prefix + "fulfillment-slot+json"; // User-specific fulfillment time slots.
			
			public static final String deliveryInfoV2 = prefix + "delivery-info-v2+json";
			//public static final String checkoutV2 = prefix + "checkout-v2+json";
			public static final String checkoutV3 = prefix + "checkout-v3+json";
			public static final String paymentsV3 = prefix + "payments-v3+json";
			public static final String checkoutResults = prefix + "checkout-results+json";
			public static final String addressV2 = prefix + "address-v2+json";

			public static final String orders = prefix + "orders+json";
			public static final String changedOrder = prefix + "changed-order+json";
			public static final String changeOrderMsg = prefix + "message+json";
			public static final String orderV3 = prefix + "order-detail-v3+json";
			public static final String orderResults = prefix + "order-results+json";
		}
	}

	public static class Requests {
		private static final String chainsID = "/chains/" + "{" + Params.Path.chainID + "}";
		private static final String usersID = "/users/" + "{" + Params.Path.userID + "}";
		private static final String userID = "/user/" + "{" + Params.Path.userID + "}";
		private static final String storeID = "/store/" + "{" + Params.Path.storeID + "}";
		private static final String storesID = "/stores/" + "{" + Params.Path.storeID + "}";

		// Request Parameters
		public static class Params {

			// Request Parameters included within the URL's path.
			// As new path params are added here, corresponding code will need to be added
			// to ServiceMappings.buildURL()
			public static class Path {
				public static final String token = "token";
				public static final String itemID = "itemId";
				public static final String chainID = "chainId";
				public static final String userID = "userId";
				public static final String storeID = "storeId";
				public static final String orderID = "orderId";
				public static final String regionID = "region";
				public static final String zipCode = "postalCode";
				public static final String parentCatID = "parentCategoryId";
				public static final String categoryID = "categoryId";
				public static final String productSKU = "sku";
				public static final String productID = "productId";
				public static final String circularID = "circularId";
				public static final String circItemID = "circularItemId";
				public static final String pageID = "pageId";
				public static final String recipeID = "recipeId";
				public static final String listID = "listId";
				public static final String listItemID = "listItemId";
				public static final String srcListID = "sourceListId";
				public static final String mwgStoreID = "pseudoStoreId";
				public static final String districtID = "districtId";
				public static final String fulfillType = "fulfillment"; // As in: 'delivery' or 'pickup'
				public static final String promoCode = "promoCode";
				public static final String circCategoriesID = "circCategoriesID";
			}

			// Request Parameters that are part of the URL's query string.
			public static class Query {
				public static final String skip = "skip";
				public static final String take = "take";
				public static final String services = "mwgService";
				public static final String city = "city";
				public static final String coords = "coordinates"; // Geolocation coordinates.
				public static final String toZip = "deliversTo"; // Is this ZIP within a given store's delivery area?
				public static final String filters = "fq";
				public static final String ipAddr = "ip";
				public static final String zipCode = "postalCode";
				public static final String searchTerm = "q";
				public static final String radius = "radius"; // Store Search radius. In meters or kilometers.
				public static final String unitOfMeasure = "unitOfMeasure"; // Unit of measure (m or km) for Store
																			// Search radius.
				public static final String regionCode = "region";
				public static final String storeOwnerID = "storeownerId";
				public static final String storeStatus = "storeStatus";
				public static final String isMember = "isMember";
				public static final String prodsPerCat = "productPerCategory";
				public static final String saleOnlyProds = "saleOnly";
				public static final String userID = "userId";
				public static final String storeID = "storeId";
				public static final String itemLocator = "il";
				public static final String excluded = "exclude"; // Products to exclude from the result set
				public static final String sortOrder = "sort"; // Sort results by Brand, Price, or UnitPrice
				public static final String searchBySound = "sound"; // What does this do? Maybe if you yell your request
																	// really loudly, MWG will hear it?
				public static final String runState = "runState";
				public static final String promotion = "promotion";
				public static final String recipeGroup = "recipeGroup";
				public static final String categoryMap = "categoryMap";
				public static final String evtParams = "eventParameters";
				public static final String sku = "sku";
			}
		}

		public static class Account {
			public static final String prefix = "/account/v7";

			// Log in a registered user
			public static final String login = chainsID + "/authentication";

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
			public static final String checkout = authorize + "/{" + Params.Path.token + "}/authenticate";
			public static final String delete = authorize + "/{" + Params.Path.token + "}";
		}

		public static class Stores {
			public static final String prefix = "/stores/v7";

			public static final String chains = "/chains";
			public static final String regions = chainsID + "/regions";
			public static final String stores = chainsID + "/stores";

			public static final String cities = regions + "/{" + Params.Path.regionID + "}/cities";
			public static final String details = stores + "/{" + Params.Path.storeID + "}";
			public static final String delivers = details + "/delivers-to/{" + Params.Path.zipCode + "}";

			public static final String StoreLocator = "/storelocator/v1/chain";
			public static final String ShoppingListUser = "/shoppinglists/v1/user/";
		}

		public static class Products {
			private static final String products = "/products";
			private static final String product = "/product";
			private static final String nutrition = "/nutrition";
			private static final String sales = "/special";
			private static final String featured = "/featured";
			private static final String productList = "/productList";

			private static final String categoryID = "/category/" + "{" + Params.Path.categoryID + "}";
			private static final String parentCatID = "/category/" + "{" + Params.Path.parentCatID + "}";
			private static final String productID = "/product/" + "{" + Params.Path.productID + "}";
			private static final String productSKU = "/sku/" + "{" + Params.Path.productSKU + "}";

			public static final String prefix = "/product/v7";

			public static final String categories = "/categories" + storeID;
			public static final String circCategoriesWeeklySpecials = "/categories" + storeID + "/circular-product-categories/all-weekly-specials";
			public static final String circCategoriesProductDetail = "/categories" + storeID + "/circular-product-categories/{" + Params.Path.circCategoriesID + "}";
			
			public static final String subCategories = parentCatID + storeID + "/categories";
			public static final String catsWithSales = categories + sales;
			public static final String subCatsWithSales = subCategories + sales;

			public static final String countries = chainsID + "/countries";

			public static final String prodByID = productID + storeID;
			public static final String prodsByCat = products + categoryID + storeID;
			public static final String prodBySKU = product + storeID + productSKU;
			public static final String prodsBySKUs = product + storeID + productList;
			public static final String prodVarsByID = productID + storeID + "/all/variations";
			public static final String prodSearch = products + storeID + "/search";

			public static final String nutritionBySKU = product + storeID + productSKU + nutrition;
			public static final String nutritionByID = productID + storeID + nutrition;

			public static final String suggestedProds = products + storeID + "/suggest";
			public static final String featuredProdsByStore = products + storeID + featured;
			public static final String featuredProdsByCat = products + categoryID + storeID + featured;
			public static final String saleItemsByCat = products + categoryID + storeID + sales;

			// This is a Products endpoint, but it's only used as part of the Circular functionality.
			// Used to get all Products associated with a given Circular Item.
			public static final String circItemProds = products + storeID + "/circular-item/{" + Params.Path.circItemID + "}";
		}

		public static class Circulars {
			private static final String items = "/items";
			private static final String storeID = "/stores/" + "{" + Params.Path.storeID + "}";

			public static final String prefix = "/circulars/v7";

			public static final String categories = chainsID + storeID + "/categories";
			public static final String category = categories + "/{" + Params.Path.categoryID + "}";
			public static final String categoryItems = category + items;
			public static final String categoryItem = categoryItems + "/{" + Params.Path.itemID + "}";

			public static final String search = chainsID + storeID + items;

			public static final String circulars = chainsID + storeID + "/circulars";
			public static final String circular = circulars + "/{" + Params.Path.circularID + "}";
			public static final String fullPages = circular + "/pages";
			public static final String pageDetails = fullPages + "/{" + Params.Path.pageID + "}";
			public static final String pageItems = pageDetails + items;
			public static final String pageItem = pageItems + "/{" + Params.Path.itemID + "}";
		}

		public static class Recipes {
			private static final String recipes = "/recipes";

			private static final String id = "/{" + Params.Path.recipeID + "}";

			public static final String prefix = "/recipes/v7";

			public static final String search = chainsID + recipes;
			public static final String featured = search + "/features";
			public static final String details = search + id;

			public static final String ingredients = details + "/ingredients";
			public static final String emailRequest = details + "/emailRequest";
			public static final String instructions = details + "/directions";
			public static final String nutritional = details + "/nutrients";
			public static final String prepTimes = details + "/times";

			public static final String categories = chainsID + "/categories";
			public static final String searchByCat = categories + "/{" + Params.Path.categoryID + "}" + recipes;

			public static final String userRecipes = chainsID + usersID + recipes;
			public static final String updateRecipe = userRecipes + id;
		}

		public static class PlasticBagFee {
			public static final String prefix = "/bagFee";
			public static final String storeIds = "/storeIds";
		}
		
		public static class Shop {
			private static final String shop = "/shop";
			private static final String storeID = "/store" + "/{" + Params.Path.storeID + "}";

			public static final String prefix = "/shop/v7";

			public static final String contact = shop + storeID + "/contact";
			public static final String settings = shop + storeID + "/settings";

			public static final String entryByChain = shop + usersID + chainsID + "/entry";
			public static final String entryByStore = shop + userID + storeID;

			public static final String message = contact + "/messages";

			public static final String dashboard = shop + userID + storeID + "/dashboard";
		}

		public static class ShoppingList {
			public static final String prefix = "/shoppinglist/v7";

			public static final String lists = chainsID + usersID + "/lists";

			public static final String list = lists + "/{" + Params.Path.listID + "}";
			public static final String items = list + "/items";
			public static final String item = items + "/{" + Params.Path.listItemID + "}";
			public static final String copy = list + "/duplicate";

			// These items are specific to the Duplicate List functionality
			//
			// The Default Limit for the number of items to copy from an existing Shopping List to a new duplicate list.
			// Each item has to be added to the new list via a separate request to the MWG API.
			// Which can result in a ridiculous number of requests for large lists.
			// Wakefern's work-around is to cap the number of items that can be duplicated from one list to the next.
			public static final String strDupeCap = "20";
			public static final int intDupeCap = Integer.parseInt(strDupeCap);
			public static final String copyLimit = "/duplicate/limit";
		}

		public static class Cart {
			private static final String itemID = "/item" + "/{" + Params.Path.itemID + "}";

			public static final String prefix = "/cart/v7";

			// Merge the cart of Guest user, into the now-authenticated user's cart.
			public static final String mergeGuest = "/user/authenticated";

			public static final String cart = userID + storeID;

			public static final String item = cart + itemID;
			public static final String items = cart + "/items";
			public static final String review = cart + "/review";

			public static final String mergeList = cart + "/merge-from/list" + "/{" + Params.Path.srcListID + "}";
		}

		public static class Checkout {
			private static final String fflmnts = "/fulfillments";
			private static final String fflmnt = "/fulfillment";
			private static final String dates = "/dates";
			private static final String times = "/times";
			private static final String addr = "/address";

			private static final String mwgStoreID = "/store/" + "{" + Params.Path.mwgStoreID + "}";
			private static final String mwgStoresID = "/stores/" + "{" + Params.Path.mwgStoreID + "}";
			private static final String deliveryZip = "/delivery/" + "{" + Params.Path.zipCode + "}";
			private static final String districtID = "/district/" + "{" + Params.Path.districtID + "}";
			private static final String orderID = "/order/" + "{" + Params.Path.orderID + "}";

			public static final String prefix = "/checkout/v7";

			public static final String promoCodes = chainsID + storesID + usersID + "/promocodes";
			public static final String promoCodeDelete = promoCodes + "/{" + Params.Path.promoCode + "}";
			
			public static final String substitutions =  userID +storeID + "/substitutions";

			public static final String fulfillOpts = fflmnts + mwgStoreID;
			public static final String deliveryInfo = fulfillOpts + deliveryZip;
			public static final String deliveryDates = deliveryInfo + dates;
			public static final String deliveryTimes = deliveryInfo + times;
			public static final String pickupInfo = fflmnts + mwgStoreID + "/pickup";
			public static final String pickupDates = pickupInfo + dates;
			public static final String pickupTimes = pickupInfo + times;

			public static final String deliveryDistrictDates = deliveryInfo + districtID + dates;
			public static final String deliveryDistrictTimes = deliveryInfo + districtID + times;

			public static final String userCheckoutState = userID + mwgStoreID;
			public static final String reservedTimeslot = usersID + mwgStoresID + "/reserved-timeslot";

			public static final String deliveryAddr = userCheckoutState + addr + "/delivery";
			public static final String billingAddr = userCheckoutState + addr + "/billing";
			public static final String comments = userCheckoutState + "/comments";
			public static final String timeslot = userCheckoutState + fflmnt;
			public static final String userFulfillOpts = userCheckoutState + fflmnts;
			public static final String userFSN = usersID + mwgStoresID + "/fsn";

			public static final String orders = "/orders" + userID + mwgStoreID;
			public static final String order = orderID + userID;

			public static final String payments = "/payments" + mwgStoreID + "/{" + Params.Path.fulfillType + "}";

			public static final String paymentOpts = userID + mwgStoreID + "/payment";
			
			public static final String changedOrder = userID + mwgStoreID + "/changed/order";

			public static final String changeOrderMsg = orderID + userID + mwgStoreID + "/to/cart";
			// Legacy. Used by com.wakefern.payment.GetToken
			public static final String UserCheckout = "/checkout/v5/user";
		}

		public static class Rewards {

			// Legacy endpoints, still in use.
			public static final String Points = "/rewards/api/v1/points";
			public static final String baseURL = "https://wfcapi.shoprite.com";
		}
		
	}
	
	public static class Log {
		public static final String log = "/log";
		
		public static final String email = log + "/email";
		public static final String status = "/status";
		public static final String address = "/address" + "/{addresses}";
		public static final String updateSetting = "/updateSettings";
		public static final String trackUserId = "/trackUserId/{userIds}";
		
		public static final String error = log + "/error";
		public static final String errorList = "/list";
		public static final String errorReset = "/reset";
		
		public static final String logger = log + "/logger";
		public static final String changeLevel = "/level/{logLevel}";
		public static final String getLevel = "/level";
		public static final String appenderList = "/appender/list";
		
		public static final String release = log + "/release";
		public static final String releaseLevel = "/level";
		
		
	}
}
