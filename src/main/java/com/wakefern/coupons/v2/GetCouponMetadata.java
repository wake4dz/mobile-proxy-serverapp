package com.wakefern.coupons.v2;

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
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wakefern.dao.couponv2.CouponDAOV2;
import com.wakefern.dao.couponv2.Tag;
import com.wakefern.dao.couponv2.TargetingBucket;
import com.wakefern.dao.couponv2.WfcTag;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by loicao on 10/10/19.
 */
@Path(ApplicationConstants.Requests.CouponsV2.CouponMetadata)
public class GetCouponMetadata extends BaseService {
	private final static Logger logger = Logger.getLogger(GetCouponMetadata.class);

	public JSONObject matchedObjects;

	static Map<String, String> tagMap = new HashMap<String, String>();
	
	static{
    	tagMap.put("ForYou", "For You:#EB2305"); //coupon.targeting_buckets[description=ForYou]
    	tagMap.put("Y", "In Circular:#909090"); //coupon.featured = 'Y'
    	tagMap.put("NATL", "");
    	tagMap.put("EMPOFFER", "");
    	tagMap.put("PROMO1", "ShopRite from Home:#EB2305");
    	tagMap.put("PROMO3", "Limit 4:#762FA0");
    	tagMap.put("PROMO5", "Procter & Gamble:#0740A3");
    	tagMap.put("PROMO8", "");
    	tagMap.put("","");
	}
	
    @POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
    									@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType, 
    									@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion, 
    									@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn, 
    									String jsonString) throws Exception, IOException {
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
        	} catch (Exception e){
        		logger.error("[GetCouponMetadata]::getInfoResponse - "+e.getMessage());
        	}
            return this.createValidResponse(response);
        } catch (Exception e){
			String errorData = LogUtil.getRequestData("GetCouponMetadata::Exception", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
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
    		couponDaoV2Obj.getTargetingBuckets();//.getTargetingBuckets()
    		// set WFC coupon filter label
    		couponDaoV2Obj.setWfcTags(setCouponFilterOption(couponDaoV2Obj.getTags(), couponDaoV2Obj.getFeatured(), couponDaoV2Obj.getTargetingBuckets()));
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
    
    private List<WfcTag> setCouponFilterOption(List<Tag> mi9TagsList, String featured, List<TargetingBucket> tbList){
    	List<WfcTag> wfcTags = new ArrayList<WfcTag>();
    	if(!mi9TagsList.isEmpty()){
    		for(Tag mi9Tag : mi9TagsList){
    			wfcTags.add(getWfcTags(mi9Tag.getTag()));
    		}
    	}
    	if(featured.equalsIgnoreCase("Y")){
			wfcTags.add(getWfcTags(featured.toUpperCase()));
    	}
    	if(!tbList.isEmpty()){
    		for(TargetingBucket tb : tbList){
    			wfcTags.add(getWfcTags(tb.getDescription()));
    		}
    	}
    	
    	return wfcTags;
    }
    
    /**
     * retrieve value from predefined Map of coupon label
     * @param mi9Tag
     * @return
     */
    private WfcTag getWfcTags(String mi9Tag){
    	WfcTag wfcTag = new WfcTag();
    	wfcTag.setTag(tagMap.get(mi9Tag));
    	return wfcTag;
    }
}
