package com.wakefern.global;

import org.apache.log4j.Logger;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ApplicationUtils {
	private final static Logger logger = Logger.getLogger(ApplicationUtils.class);
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
    
    public static String getVcapValue(String vcapKeyName){
    	String vcapValue = "";
    	try{
    		vcapValue = MWGApplicationConstants.getSystemProperytyValue(vcapKeyName).trim();
    	} catch(Exception e){
    		logger.error("[ApplicationUtils]::getVcapValue::Failed! Exception " + vcapKeyName + e.getMessage());
    	}
    	return vcapValue;
    }
    
    /**
     * returns either coupon production or staging url endpoint
     * @param vcapKeyName coupon_service vcap [Staging/Production] keyword
     * @return
     */
    public static String getCouponServiceEndpoint(String vcapKeyName){
    	String coupon_service = getVcapValue(vcapKeyName);
    	String coupon_domain = (!coupon_service.isEmpty() && coupon_service.equalsIgnoreCase(WakefernApplicationConstants.CouponsV2.coupon_staging)) ? 
    			WakefernApplicationConstants.CouponsV2.baseURL_staging : WakefernApplicationConstants.CouponsV2.baseURL;
		logger.info("[ApplicationUtils]::getCouponServiceEndpoint::coupon_domain " + coupon_domain);
    	return coupon_domain;
    }
}
