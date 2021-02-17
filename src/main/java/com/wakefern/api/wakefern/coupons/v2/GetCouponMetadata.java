package com.wakefern.api.wakefern.coupons.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wakefern.dao.couponv2.CouponDAOV2;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by loicao on 10/10/19.
 */
@Path(ApplicationConstants.Requests.CouponsV2.CouponMetadata)
public class GetCouponMetadata extends BaseService {
	private final static Logger logger = Logger.getLogger(GetCouponMetadata.class);

    @POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
    									@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType, 
    									@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion, 
    									@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn, 
    									String jsonString) {
        //Execute POST
        this.requestPath = ApplicationUtils.constructCouponUrl(ApplicationConstants.Requests.CouponsV2.CouponMetadata, fsn).toString();
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
        headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
        
        try {
        	String response = HTTPRequest.executePostJSON(this.requestPath, jsonString, headerMap, VcapProcessor.getApiHighTimeout());
        	
        	try{
            	if(isFilterCoupon(appVersion)){ // if appVersion is empty or less than 3.16
            		response = this.filterCouponType(response);
            	}
            	
            	//Note: this is a temp solution for the existing old mobile app UI out in the production as of 2/9/2021
            	if (VcapProcessor.isInCircularConversion()) {
            		response = convertInCircular(response);
            	}
            	
            	logger.debug("After filtered response: " + response);
            	
        	} catch (Exception e){
        		logger.error("[GetCouponMetadata]::getInfoResponse - "+e.getMessage());
        	}
        
            return this.createValidResponse(response);
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_V2_GET_COUPON_METADATA);
        	
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }
    
    /**
     * filter out coupon with offer type 14, currently set globally, future use will be filter by app version number,
     * app version # will be sent via request header.
     * @param resp
     * @return
     * @throws IOException 
     */
    private String filterCouponType(String response) throws IOException{
        final String offer_type_exclude="14";
        
    	ObjectMapper mapper = new ObjectMapper();
    	CouponDAOV2[] couponDaoArr = mapper.readValue(response, CouponDAOV2[].class);
    	ObjectWriter writer = mapper.writer();
    	List<CouponDAOV2> couponList = new ArrayList<CouponDAOV2>();
    	for(CouponDAOV2 couponDaoV2Obj : couponDaoArr){
    		if(couponDaoV2Obj.getOfferType().equals(offer_type_exclude)){
    			continue;
    		}
			couponList.add(couponDaoV2Obj);
    	}
    	return writer.writeValueAsString(couponList);
    }

    /**
     * Check coupon version to initiate coupon filter out offer_type 14, 
     * if no AppVersion or AppVersion in request is < 3.18,
     * 	then filter out the coupon
     * 	else not filter
     * @param appVerHeader
     * @return true if need filter, false otherwise.
     */
    private boolean isFilterCoupon(String appVerHeader) {
    	boolean isFilter = true;
    	// filter out coupon below 3.18
    	int majorVerToNotFilter = 3;
    	int minorVerToNotFilter = 18;
    	
    	if (appVerHeader != null && !appVerHeader.isEmpty()) {
    		logger.info("[GetCouponMetadata]::isFilterCoupon::Version-" + appVerHeader);
    		String[] headerVerArr = appVerHeader.split("\\.");
    		int majorVerHeader = Integer.parseInt(headerVerArr[0]);
    		// check major version
    		if (majorVerHeader > majorVerToNotFilter) { // header major ver above 3..
    			isFilter = false;
    		} else if (majorVerHeader == majorVerToNotFilter) { // check minor version
    			if (Integer.parseInt(headerVerArr[1]) >= minorVerToNotFilter) {
    				isFilter = false;
    			}
    		}
    	}
    	return isFilter;
    }
    
    /**
     * As of 2/17/2021, any in-circular coupon items are marked with "featured=Y" in the ShopRite mobile app UI. 
     * The coupon supplier of Inmar is planning to use "featured=Y" for any featured items in the future,
     * 	and tag any in-circular coupon items with a tag value of "circular" instead.
     * 
     * Due to many old apps out in the Prod, we try to set "feature=Y" when we see a tag value of "circular" for any
     * in-circular items. But this creates some UI discrepancies issue for combining featured items and in-circular items in 
     * the same UI display location.
     */
    private String convertInCircular(String response) {
    	JSONArray couponArray = new JSONArray(response);
    	JSONArray updatedCouponArray = new JSONArray();
    	
    	logger.debug("Coupon array size: " + couponArray.length());
    	for (int i = 0; i < couponArray.length(); i++) {
			JSONObject coupon = couponArray.getJSONObject(i);
			JSONArray tagArray = coupon.optJSONArray("tags");
			
			if ((tagArray != null) && (tagArray.length() > 0)) {
				for (int j = 0; j < tagArray.length(); j++) {
					JSONObject tag = tagArray.getJSONObject(j);
					if (tag.getString("tag").equalsIgnoreCase("CIRCULAR")) {
						logger.debug("tag value: " + tag.getString("tag") + ",    featured:" + coupon.getString("featured"));
						coupon.remove("featured");
						coupon.put("featured", "Y");
						
						//no need to check the rest of tag values
						break;
					}
				}
			}
			updatedCouponArray.put(coupon);
    	}
    	
    	return updatedCouponArray.toString();
    }
}
