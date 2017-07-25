package com.wakefern.Coupons;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.dao.coupon.CouponDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.request.HTTPRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */

@Path(ApplicationConstants.Requests.Coupons.GetCoupons)
public class Coupons extends BaseService {
    public JSONObject matchedObjects;
	private long startTime, endTime;
	private final static Logger logger = Logger.getLogger("Coupons");

    @GET
    @Produces("application/*")
    public Response getInfoResponse(@DefaultValue(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)
                                    @QueryParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam,
                                    @DefaultValue("") @QueryParam("query") String query,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        try{
            if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
                this.token = ApplicationConstants.Requests.Tokens.couponToken;
            }else{
                this.token = ApplicationConstants.Requests.Tokens.couponToken;
            }
        }catch(Exception e){
        	this.token = ApplicationConstants.Requests.Tokens.couponToken;
        }
        
        startTime = System.currentTimeMillis();
        matchedObjects = new JSONObject();
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCoupons
                    + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        try {
            String coupons = HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0);
            
            endTime = System.currentTimeMillis();
        	System.out.println("[Coupons]::Total process time (ms): "+ (endTime - startTime));
        	
        	ObjectMapper mapper = new ObjectMapper();
        	CouponDAO[] couponDaoArr = mapper.readValue(coupons, CouponDAO[].class);

            if (query.isEmpty()){// == "") {

            	String[] couponOmittedValuesArr = {
            		    "coupon_id",
            		    "featured",
            		    "requirement_description",
            		    "brand_name",
            		    "reward_upcs",
            		    "short_description",
            		    "image_url",
            		    "external_id",
            		    "pos_live_date",
            		    "display_start_date",
            		    "display_end_date",
            		    "Category",
            		    "requirement_upcs",
            		    "expiration_date",
            		    "coupon_value"
            	};
//            			"id", "__createdAt", "__updatedAt", "__version", "__deleted", "total_downloads", "targeting_buckets",
//            		    "targeted_offer", "tags", "enabled", "offer_type", "long_description_header"};
            	
            	FilterProvider filterCouponFields = new SimpleFilterProvider()
            			.addFilter("filterByValue", SimpleBeanPropertyFilter.filterOutAllExcept(couponOmittedValuesArr));
            	ObjectWriter writer = mapper.writer(filterCouponFields);
            	for(CouponDAO couponDao : couponDaoArr){
            		try{
            			//"short_description": "Save $.50 On Crystal Farms Chunk Cheese"
	            		String [] shortDesc = couponDao.getShortDescription().split(" ");
            			if(shortDesc[1].contains("$"))
            				couponDao.setCouponValue(shortDesc[1]);
            			else if(shortDesc[0].contains("$")){
            				couponDao.setCouponValue(shortDesc[0]);
            			} else{
            				for(String value : shortDesc){
            					if(value.contains("$")){
                    				couponDao.setCouponValue(value);
                    				break;
            					}
            				}
            			}
            		} catch(Exception e){
                    	logger.log(Level.SEVERE, "[getInfoResponse]::Exception retrieved coupon ppc: {0}, msg: {1}", new Object[]{ppcParam, e.toString()});
            		}
            	}
            	
            	String cnrDaoStr = writer.writeValueAsString(couponDaoArr);
            	
                return this.createValidResponse(cnrDaoStr);
            }

            JSONArray matchedObjects2 = new JSONArray();
            return this.createValidResponse(search(coupons, query, matchedObjects2).toString());
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String ppcParam,  String query, String authToken) throws Exception, IOException {
        try{
            if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
                this.token = ApplicationConstants.Requests.Tokens.couponToken;
            }else{
                this.token = ApplicationConstants.Requests.Tokens.couponToken;
            }
        }catch(Exception e){
            this.token = ApplicationConstants.Requests.Tokens.couponToken;
        }

        matchedObjects = new JSONObject();
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCoupons
                + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        String coupons = HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0);

        if (query.isEmpty()){// == "") {
            return coupons;
        }

        JSONArray matchedObjects2 = new JSONArray();
        return search(coupons, query, matchedObjects2).toString();
    }

    public Coupons () {this.serviceType = new WakefernHeader();}

    private JSONObject search(String coupons, String query, JSONArray matchedObjects2){
        query = query.toLowerCase();

        JSONArray jsonArray = new JSONArray(coupons);

        for(Object coupon: jsonArray){
            JSONObject currentCoupon = (JSONObject) coupon;

            String brandName = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.brandName);
            String shortDescription = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.shortDescription);
            String longDescription = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.longDescription);
            String requirementDescription = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.requirementDescription);

            if(brandName.toLowerCase().contains(query) || shortDescription.toLowerCase().contains(query)
                    || longDescription.toLowerCase().contains(query) || requirementDescription.toLowerCase().contains(query)){
                matchedObjects2.put(matchedObjects2.length(), currentCoupon);
            }
        }
        return sortCouponsByCategory(matchedObjects2);
    }

    private JSONObject sortCouponsByCategory( JSONArray matchedObjects2 ){
        Set<String> categoryIds = new HashSet<>();
        JSONObject jsonObject = new JSONObject();

        for (Object coupon: matchedObjects2){
            JSONObject currentCoupon = (JSONObject) coupon;
            categoryIds.add(currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.category));
        }

        JSONArray retval = new JSONArray();
        for(String id: categoryIds){
            JSONObject formatting = new JSONObject();
            JSONArray currentId = new JSONArray();
            for(Object coupon: matchedObjects2){
                JSONObject currentCoupon = (JSONObject) coupon;
                String category = (currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.category));
                if(category.equals(id)) {
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

    private int totalCoupons(JSONArray jsonArray){
        int retval = 0;
        for(Object category: jsonArray){
            JSONObject currentCategory = (JSONObject) category;
            retval += currentCategory.getJSONArray("Items").length();
        }
        return retval;
    }
}
