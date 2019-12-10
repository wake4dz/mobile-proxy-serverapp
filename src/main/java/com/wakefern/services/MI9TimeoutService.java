package com.wakefern.services;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wakefern.global.VcapProcessor;

/**
 * Get customized timeout for MI9 services.
 * @author sfl1c
 *
 */
public class MI9TimeoutService {	
	private final static Logger logger = Logger.getLogger(MI9TimeoutService.class);

	private static final String TAG = MI9TimeoutService.class.getSimpleName();
	
	private static final int HIGH_TO = VcapProcessor.getApiHighTimeout();
	private static final int MEDIUM_TO = VcapProcessor.getApiMediumTimeout();
	private static final int LOW_TO = VcapProcessor.getApiLowTimeout();
	
	//CART
	public static final String CART_GET_CONTENTS = "com.wakefern.cart.GetContents";	// takes 20% of total time (100% being 10.1 billions ms)
	public static final String CART_CREATE_ITEM = "com.wakefern.cart.CreateItem"; 	// add to cart, takes 7.9%
	public static final String CART_UPDATE_ITEM = "com.wakefern.cart.UpdateItem"; 	// takes 3% of total time
	public static final String CART_DELETE_ITEM = "com.wakefern.cart.DeleteItem"; 	// takes 2.6%
	//CIRCULAR
	public static final String CIRCULARS_GET_DETAILS = "com.wakefern.circulars.GetCircularsDetails"; //takes 2.3%
	public static final String CIRCULARS_GET_FULL_PAGES = "com.wakefern.circulars.GetFullPages";	 //takes 1.7%
	public static final String CIRCULAR_GET_PRODUCT_CATEGORY_WKLY_SPECIALS = "com.wakefern.products.categories.GetCirProductCategoriesWklySpecials"; //takes 0.4%
	public static final String CIRCULAR_GET_ITEM_PRODUCTS = "com.wakefern.products.GetCircularItemProducts"; //takes 1.2%
	// PRODUCT
	public static final String PRODUCTS_GET_BY_SKUS = "com.wakefern.products.GetProductsBySKUs"; //takes 3.2%
	public static final String PRODUCTS_SEARCH = "com.wakefern.products.Search";			//takes 8% of total time
	public static final String PRODUCTS_GET_BY_ID = "com.wakefern.products.GetById"; 		//takes 7.7%
	public static final String PRODUCTS_GET_SUGGEST = "com.wakefern.products.GetSuggestions"; //takes 3.7%
	public static final String PRODUCTS_GET_NUTRITION_BY_ID = "com.wakefern.products.GetNutritionById";	//takes 1.2%
	//SHOPPING LIST
	public static final String SHOPPINGLIST_GET_LISTS = "com.wakefern.shoppingLists.GetLists"; 					//takes 2.1%
	public static final String SHOPPINGLIST_GET_LIST_ITEMS = "com.wakefern.shoppingLists.GetListItems"; 		// takes 4.2%
	public static final String SHOPPINGLIST_CREATE_LIST_ITEMS = "com.wakefern.shoppingLists.CreateListItems"; 	//takes 1.5%
	//CHECKOUT
	public static final String CHECKOUT_GET_STATE = "com.wakefern.checkout.users.GetCheckoutState";			//takes 6.6%
	public static final String CHECKOUT_UPDATE_STATE = "com.wakefern.checkout.users.UpdateCheckoutState";	//takes 1.6%
	public static final String CHECKOUT_CREATE_ORDERS = "package com.wakefern.checkout.orders.CreateOrders";
	private static Map<String, Integer> serviceTimeoutMap = null;
	
	/**
	 * Initialized the timeout for the API services
	 */
	static {
		logger.info(TAG + "::load timeout config for MI9 APIs..");
		serviceTimeoutMap = new HashMap<String, Integer>();
		//CART
		serviceTimeoutMap.put(CART_GET_CONTENTS, HIGH_TO);
		serviceTimeoutMap.put(CART_CREATE_ITEM, HIGH_TO);
		serviceTimeoutMap.put(CART_UPDATE_ITEM, MEDIUM_TO);
		serviceTimeoutMap.put(CART_DELETE_ITEM, MEDIUM_TO);
		
		//CIRCULAR
		serviceTimeoutMap.put(CIRCULARS_GET_DETAILS, MEDIUM_TO);
		serviceTimeoutMap.put(CIRCULARS_GET_FULL_PAGES, HIGH_TO);
		serviceTimeoutMap.put(CIRCULAR_GET_PRODUCT_CATEGORY_WKLY_SPECIALS, HIGH_TO);
		serviceTimeoutMap.put(CIRCULAR_GET_ITEM_PRODUCTS, MEDIUM_TO);
		
		// PRODUCT
		serviceTimeoutMap.put(PRODUCTS_GET_BY_SKUS, LOW_TO);
		serviceTimeoutMap.put(PRODUCTS_GET_BY_ID, LOW_TO);
		serviceTimeoutMap.put(PRODUCTS_GET_SUGGEST, LOW_TO);
		serviceTimeoutMap.put(PRODUCTS_GET_NUTRITION_BY_ID, LOW_TO);
		
		//SHOPPING LIST
		serviceTimeoutMap.put(PRODUCTS_SEARCH, LOW_TO);
		serviceTimeoutMap.put(SHOPPINGLIST_GET_LISTS, LOW_TO);
		serviceTimeoutMap.put(SHOPPINGLIST_GET_LIST_ITEMS, LOW_TO);
		serviceTimeoutMap.put(SHOPPINGLIST_CREATE_LIST_ITEMS, LOW_TO);
		
		//CHECKOUT
		serviceTimeoutMap.put(CHECKOUT_GET_STATE, MEDIUM_TO);
		serviceTimeoutMap.put(CHECKOUT_UPDATE_STATE, HIGH_TO);
		serviceTimeoutMap.put(CHECKOUT_CREATE_ORDERS, 40000);
	}
	
	
	public static int getTimeout(String serviceKeyName){
		int defaultTimeout = HIGH_TO;
		try{
			defaultTimeout = serviceTimeoutMap.get(serviceKeyName);
		} catch (Exception e){
			logger.error(TAG+"::Exception! Assign 30 sec default timeout for "+serviceKeyName);
		}
		return defaultTimeout;
	}
}
