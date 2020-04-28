package com.wakefern.wakefern.itemLocator;


import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Created by zacpuste on 10/18/16.
 */

public class ItemLocatorArray extends BaseService {
	
	private final static Logger logger = Logger.getLogger(ItemLocatorArray.class);

    public String getInfo(String storeId, String upc, String authToken) {
        Map<String, String> wkfn = new HashMap<>();

        try {
	        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;
	        
	        wkfn.put(ApplicationConstants.Requests.Header.contentType, "application/json");
	        wkfn.put("Authentication", authToken);

	        logger.trace("path = " + path);
	        
            return HTTPRequest.executeGet(path, wkfn, 0);
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.ITEM_LOCATOR_ARRAY);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"authToken", authToken, "contentType", "application/json" );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return null;
        }
    }

    public ItemLocatorArray(){
        this.requestHeader = new MWGHeader();
    }
}