package com.wakefern.global;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ApplicationUtils {
	
	public static final int upcLength = 11;
	
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
    
    /**
     * Reformat UPC to comply with MWG's getProductBySKUs API service. The API requires specific SKU's length of 11 
     * 		numeric character to return product detail. Any UPC length other than 11 will return empty product array.
     * @param upcStr - the upc String
     * @param upcLengthSpec - the length of upc after formatting, will be 11 numeric length.
     * @return
     */
	public static String skuScrubber(String upcStr, int upcLengthSpec) {
		StringBuilder sb = new StringBuilder();
		int upcLength = upcStr.length();
		if(upcLength == upcLengthSpec){
			sb.append(upcStr);
		} else if(upcLength < upcLengthSpec){
			int diff = upcLengthSpec-upcLength;
			for(int i=0; i< diff; i++){
				sb.append("0");
			}
			sb.append(upcStr);
		} else{
			int diff = upcLength - upcLengthSpec;
			sb.append(upcStr.substring(diff));
		}
		return sb.toString();
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
