package com.wakefern.global;

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
    
//public static String constructCouponUrl(String pathUrl, String fsnReqParam, String queryKey, String queryValue) {
//    		StringBuilder sb = new StringBuilder();
//        	sb.append(ApplicationConstants.Requests.CouponsV2.BaseCouponURL);
//        	sb.append(pathUrl);
//    		sb.append("?fsn=");
//    		sb.append(fsnReqParam);
//    		sb.append(queryKey);
//    		sb.append(queryValue);
//    		return sb.toString();
//    }
}
