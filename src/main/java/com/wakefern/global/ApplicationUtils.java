package com.wakefern.global;

import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ApplicationUtils {
    public static Boolean isEmpty(String str){
        if(str.isEmpty() || str == null){
            return true;
        }
        return false;
    }
    
    public static StringBuilder constructCouponUrl(String pathUrl, String fsnReqParam) {
    		StringBuilder sb = new StringBuilder();
        	sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURL);
        	sb.append(pathUrl);
    		sb.append("?fsn=");
    		sb.append(fsnReqParam);
    		return sb;
    }
    
    public static String constructItemLocatorUrl(String storeId, String upcs) {
		StringBuilder sb = new StringBuilder();
    	sb.append(WakefernApplicationConstants.ItemLocator.baseURL);
    	sb.append(WakefernApplicationConstants.ItemLocator.prefix);
		sb.append("/secure/itemData/store/");
    	sb.append(storeId);
		sb.append("/upc/");
		sb.append(upcs);
		return sb.toString();
    }
}
