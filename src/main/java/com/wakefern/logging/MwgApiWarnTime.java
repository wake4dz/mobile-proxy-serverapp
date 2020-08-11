package com.wakefern.logging;

/*
 *  Author:  Danny Zheng
 *  Date:    10/11/2018
 *  Purpose: To provide each MWG API call with its own upper limited process time. 
 *  		 If the actual API call time exceeds the value defined here, there would be a warn log message from its REST controller class
 *  
 *           The team needs to set each time with more margin of error. Say a API call usually takes 500 ms, we can set it to 3000 ms 
 *           to accommodate any delays, like the slow network latency time, busy server, busy DB server, etc.
 *           
 *           The current API call time-out is 30000 ms (namely 30 seconds) which is for every API call. 
 *           With the specific warn time set here for each API and the log4j warn log message would help monitoring the MWG's back-end process.
 *           
 *           Note: I put this warnTime feature on the REST controller class level, 
 *           	   so the actual process time = our pre-call time + network latency + MWG's back-end process time + network latency + our post-call time.
 *                 But our pre-call and post-call process time should be very short, maybe in the range of few milliseconds, a negligible factor. 
 *                 
 *           Note:  MwgApiWarnTime.java is modeled and modified from MwgErrorType.java
 */
public enum MwgApiWarnTime {
	AUTHENTICATION_AUTHENTICATE_CHECKOUT(1000),
	AUTHENTICATION_AUTHENTICATE_USER(1000),
	
	CART_GET_CONTENTS(3000),	
	
	ORDERS_CREATE_ORDER(30000),	
	
	CATEGORIES_GET_SUB_CATEGORIES(1000),  // as an example
	
	PRODUCTS_GET_BY_SKU(1000),
	
	ITEM_LOCATOR_ARRAY(1000);
	
	MwgApiWarnTime( int warnTime) {
		this.warnTime = warnTime;
	}
	
	private int warnTime;
	
	public int getWarnTime() {
		return this.warnTime;
	}
}
