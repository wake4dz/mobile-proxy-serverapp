package com.wakefern.wakefern.itemLocator;


import com.wakefern.global.ApplicationConstants;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *  9/29/2021
 *  Danny Zheng
 *  Call Wakefern Item Locator API from V8 GetShopppingCart API; it throws an exception if something goes wrong
 */
public class Mi9V8ItemLocatorArray {
	
	private final static Logger logger = LogManager.getLogger(Mi9V8ItemLocatorArray.class);

    public static String getInfo(String storeId, String upc, String authToken) throws Exception{
        Map<String, String> wkfn = new HashMap<>();

        try {
	        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;
	        
	        wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
	        wkfn.put("Authentication", authToken);

	        logger.trace("path = " + path);
	     
            return HTTPRequest.executeGet(path, wkfn, 0);
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, ErrorType.PROXY_MI9V8_ITEM_LOCATOR_ARRAY);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"authToken", authToken, "contentType", "application/json" );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            throw new Exception(LogUtil.getExceptionMessage(e));
        }
    }
}