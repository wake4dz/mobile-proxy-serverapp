package com.wakefern.global;

import org.apache.log4j.Logger;

import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

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

	public static String constructCouponV3Url(String path) {
		return constructCouponV3Url(path, null);
	}

	public static String constructCouponV3Url(String path, Map<String, String> params) {
		if (params == null) {
			params = new HashMap<>();
		}
		// Always append banner information
		params.put(WakefernApplicationConstants.CouponsV3.PathInfo.banner,
				ApplicationConstants.Requests.CouponsV3.banner);
		String url = ApplicationConstants.Requests.CouponsV3.BaseCouponURL + path;
		UriBuilder builder = UriBuilder.fromPath(url);
		URI output = builder.buildFromMap(params);
		return output.toASCIIString();
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
    		vcapValue = MWGApplicationConstants.getSystemPropertyValue(vcapKeyName).trim();
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

	/**
	 * returns either coupon production or staging url endpoint
	 * @param vcapKeyName coupon_service vcap [Staging/Production] keyword
	 * @return
	 */
	public static String getCouponV3ServiceEndpoint(String vcapKeyName){
		String coupon_service = getVcapValue(vcapKeyName);
		String coupon_domain = (!coupon_service.isEmpty() && coupon_service.equalsIgnoreCase(WakefernApplicationConstants.CouponsV3.coupon_staging)) ?
				WakefernApplicationConstants.CouponsV3.baseURL_staging : WakefernApplicationConstants.CouponsV3.baseURL;
		logger.info("[ApplicationUtils]::getCouponV3ServiceEndpoint::coupon_domain " + coupon_domain);
		return coupon_domain;
	}

    /**
     * if no AppVersion or AppVersion in request is < majorVersion.minorVersion  (Important note: < is the key )
     * 	then applicable
     * 	else not 
     * @param appVerHeader
     * @return true if applicable, false otherwise.
     */
    public static boolean isReleaseApplicable(String appVerHeader, int majorVersion, int minorVersion) {
    	boolean isApplicable = true;
    	
    	if (appVerHeader != null && !appVerHeader.isEmpty()) {
    		String[] headerVerArr = appVerHeader.split("\\.");
    		int majorVerHeader = Integer.parseInt(headerVerArr[0]);
 
    		if (majorVerHeader > majorVersion) { // header major version
    			isApplicable = false;
    		} else if (majorVerHeader == majorVersion) { // check minor version
    			if (Integer.parseInt(headerVerArr[1]) >= minorVersion) {
    				isApplicable = false;
    			}
    		}
    	}
    	return isApplicable;
    }
}
