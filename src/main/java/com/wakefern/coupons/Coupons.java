package com.wakefern.coupons;

import com.wakefern.dao.coupon.CouponDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernHeader;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by brandyn.brosemer on 9/13/16.
 */

@Path(ApplicationConstants.Requests.Coupons.GetCoupons)
public class Coupons extends BaseService {
	
	private final static Logger logger = Logger.getLogger(CouponAddToPPC.class);
	
	public JSONObject matchedObjects;
	private long startTime, endTime;


	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	public Response getInfoResponse(
			@DefaultValue(WakefernApplicationConstants.Coupons.Metadata.PPC_All) 
			@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PPC) String ppcParam,
			
			@DefaultValue("") 
			@QueryParam("query") String query, 
			
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam("Authorization") String authToken
		) {

		try {
			// for /getCouponMetadata endpoint

			ObjectMapper mapper = new ObjectMapper();
			
        	// return this.createValidResponse(mockCoupon);	
			String[] couponOmittedValuesArr = { 
					"coupon_id", 
					"requirement_description", 
					"brand_name",
					"short_description", 
					"image_url", 
					"total_downloads", 
					"external_id", 
					"pos_live_date",
					"display_start_date", 
					"display_end_date", 
					"Category", 
					"requirement_upcs", 
					"expiration_date",
					"coupon_value", 
					"targeting_buckets" 
			};

			FilterProvider filterCouponFields = new SimpleFilterProvider().addFilter("filterByValue", SimpleBeanPropertyFilter.filterOutAllExcept(couponOmittedValuesArr));
			
			ObjectWriter writer = mapper.writer(filterCouponFields);
			
			CouponDAO[] couponDaoArr = new CouponDAO[1];
			CouponDAO couponDao = new CouponDAO();
			
			couponDao.setCouponId("1");
			couponDao.setBrandName("Attention: Update Required!");
			couponDao.setShortDescription("This app is outdated. Please update to the latest version for optimal performance and to ensure coupons will load to your card.");
			couponDao.setCouponValue("$0.00");
			
			couponDao.setId("1");
			couponDao.setFeatured("N");
			couponDao.setRequirementDescription("This app is outdated. Please update to the latest version for optimal performance and to ensure coupons will load to your card.");
			couponDao.setImageUrl("");
			couponDao.setRewardUpcs("");
			couponDao.setTotalDownloads("0");
			couponDao.setTargetingBuckets("");
			couponDao.setTargetedOffer("N");
			couponDao.setTags("");
			couponDao.setEnabled("N");
			couponDao.setPosLiveDate("");
			couponDao.setDisplayStartDate("");
			couponDao.setDisplayEndDate("");
			couponDao.setOfferType("0");
			couponDao.setCategory("ShopRite");
			couponDao.setPublicationId("1");
			couponDao.setLongDescription("");
			couponDao.setRequirementUpcs("");
			couponDao.setSubcategory("");
			couponDao.setOfferPriority("99999");
			couponDao.setExpirationDate("");
			couponDao.setLongDescriptionHeader("ShopRite Announcement");
			
					
			couponDaoArr[0] = couponDao;
			
			String cnrDaoStr = writer.writeValueAsString(couponDaoArr);

			return this.createValidResponse(cnrDaoStr);
			
		
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_COUPONS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"ppcParam", ppcParam, "authToken", authToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
		}
	}

	public String getInfo(String ppcParam, String query, String authToken) throws Exception, IOException {

		this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;

		matchedObjects = new JSONObject();
		
		this.requestPath = ApplicationConstants.Requests.Coupons.BaseCouponURL
				+ ApplicationConstants.Requests.Coupons.GetCoupons
				+ WakefernApplicationConstants.Coupons.Metadata.PPCQuery + ppcParam;

		// Execute Post
		ServiceMappings serviceMappings = new ServiceMappings();
		serviceMappings.setCouponMapping(this);

		String coupons = HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), VcapProcessor.getApiLowTimeout());

		if (query.isEmpty()) {// == "") {
			return coupons;
		}

		JSONArray matchedObjects2 = new JSONArray();
		return search(coupons, query, matchedObjects2).toString();
	}

	public Coupons() {
		this.requestHeader = new WakefernHeader();
	}

	private JSONObject search(String coupons, String query, JSONArray matchedObjects2) {
		query = query.toLowerCase();

		JSONArray jsonArray = new JSONArray(coupons);

		for (Object coupon : jsonArray) {
			JSONObject currentCoupon = (JSONObject) coupon;

			String brandName = currentCoupon.getString(WakefernApplicationConstants.Coupons.Search.brandName);
			String shortDescription = currentCoupon.getString(WakefernApplicationConstants.Coupons.Search.shortDescription);
			String longDescription = currentCoupon.getString(WakefernApplicationConstants.Coupons.Search.longDescription);
			String requirementDescription = currentCoupon.getString(WakefernApplicationConstants.Coupons.Search.requirementDescription);

			if (brandName.toLowerCase().contains(query) 
					|| shortDescription.toLowerCase().contains(query)
					|| longDescription.toLowerCase().contains(query)
					|| requirementDescription.toLowerCase().contains(query)) {
				
				matchedObjects2.put(matchedObjects2.length(), currentCoupon);
			}
		}
		
		return sortCouponsByCategory(matchedObjects2);
	}

	private JSONObject sortCouponsByCategory(JSONArray matchedObjects2) {
		Set<String> categoryIds = new HashSet<>();
		JSONObject jsonObject = new JSONObject();

		for (Object coupon : matchedObjects2) {
			JSONObject currentCoupon = (JSONObject) coupon;
			categoryIds.add(currentCoupon.getString(WakefernApplicationConstants.Coupons.Search.category));
		}

		JSONArray retval = new JSONArray();
		
		for (String id : categoryIds) {
			JSONObject formatting = new JSONObject();
			JSONArray currentId = new JSONArray();
			
			for (Object coupon : matchedObjects2) {
				JSONObject currentCoupon = (JSONObject) coupon;
				String category = (currentCoupon
						.getString(WakefernApplicationConstants.Coupons.Search.category));
				if (category.equals(id)) {
					currentId.put(currentId.length(), currentCoupon);
				}
			}
			
			formatting.put("Name", id);
			formatting.put("Items", currentId);
			retval.put(retval.length(), formatting);
		}
		
		jsonObject.put("CouponCategories", retval);
		jsonObject.put("totalCoupons", totalCoupons(retval));
		
		return jsonObject;
	}

	private int totalCoupons(JSONArray jsonArray) {
		int retval = 0;
		
		for (Object category : jsonArray) {
			JSONObject currentCategory = (JSONObject) category;
			retval += currentCategory.getJSONArray("Items").length();
		}
		
		return retval;
	}
}
