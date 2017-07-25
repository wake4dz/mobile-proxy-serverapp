package com.wakefern.Coupons;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.dao.coupon.CouponDAO;
import com.wakefern.dao.product.ImageLink;
import com.wakefern.dao.product.ProductDetail;
import com.wakefern.dao.recommend.CouponsAndRecommendationsDao;
import com.wakefern.dao.recommend.Recommend;
import com.wakefern.dao.recommend.RecommendProduct;
import com.wakefern.dao.recommend.RecommendVPUPCs;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.wakefern.Products.ProductBySku;
import com.wakefern.Recommendations.RecommendUPCs;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.request.HTTPRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */

@Path(ApplicationConstants.Requests.Coupons.GetCouponsRecommendations)
public class CouponsAndRecommendations extends BaseService {
	
	private final static Logger logger = Logger.getLogger("CouponsAndRecommendations");
	private long startTime, endTime;
	
    public JSONObject matchedObjects;
    @GET
    @Produces("application/*")
    public Response getInfoResponse(@DefaultValue("") @QueryParam("store") String storeId,
    								@DefaultValue("") @QueryParam("pseudo") String pseudoStoreId,
    								@DefaultValue("") @QueryParam("email") String email,
    								@DefaultValue(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)
                                    @QueryParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam,
                                    @DefaultValue("") @QueryParam("query") String query,
                                    @HeaderParam("Authorization") String authToken,
                                    @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        try{
            if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
                this.token = ApplicationConstants.Requests.Tokens.couponToken;
            }else{
                this.token = ApplicationConstants.Requests.Tokens.couponToken;
            }
        }catch(Exception e){
        	this.token = ApplicationConstants.Requests.Tokens.couponToken;
        }

		ObjectMapper mapper = new ObjectMapper();
        if(!storeId.isEmpty()){
			/**
			 * Get coupon metadata
			 */
        	startTime = System.currentTimeMillis();
        	
        	Coupons coupon = new Coupons();
        	//TODO: need to work on query, as response will be different for coupon metadata.
//        	String vpPath = "https://vp.shoprite.com/api/wfc/store/"+storeId+"/email/"+email+"/fsn/"+ppcParam;
//        	CouponDAO[] cd = mapper.readValue(couponResp, CouponDAO[].class);
        	String couponResp = coupon.getInfo(ppcParam, query, authToken);
//        	System.out.println("coupon: "+couponResp);
//        	List<CouponDAO> couponDaoList = Arrays.asList(mapper.readValue(couponResp, CouponDAO[].class));
        	CouponDAO[] couponDaoArr = mapper.readValue(couponResp, CouponDAO[].class);
			/**
			 * Get VP recommended UPCs
			 */
        	RecommendUPCs recUPCs = new RecommendUPCs();
        	String vpResp = recUPCs.getInfo(storeId, email, ppcParam, authToken);
        	RecommendVPUPCs recUPCsDAO = mapper.readValue(vpResp, RecommendVPUPCs.class);
        	List<String> upcList = recUPCsDAO.getProducts();
        	RecommendProduct recommendProduct = new RecommendProduct();
        	int counter = 0;
        	List<Recommend> recList = new ArrayList<Recommend>();
//        	Map<String, String> couponIdUPCsMap = new HashMap<String, String>();

        	//loop through coupon dao & map couponId with UPCs
//            for(CouponDAO couponDao : couponDaoList){
//            	String[]
//            	couponIdUPCsMap.put(couponDao.getCouponId(), couponDao.getRequirementUpcs());
//            	if(couponDao.getRequirementUpcs().contains(upcStr)){
//	                recommend.setCouponId(couponDao.getCouponId());
//            	}
//            }
        	for(String upcStr : upcList){
        		String skuData = upcStr.substring(2,upcStr.length());
        		/**
        		 * Get Product Detail for each UPC
        		 */
	        	ProductBySku productSKUs = new ProductBySku();
	        	String productResp = productSKUs.getInfo(pseudoStoreId, skuData, "", authToken2);
                ProductDetail productDetail = mapper.readValue(productResp, ProductDetail.class);
                try{
	                Recommend recommend = new Recommend();
//	                recommend.setAisle(productDetail.getAisle());
	                recommend.setBrand(productDetail.getBrand());
	                recommend.setCategory(productDetail.getCategory());
	                recommend.setName(productDetail.getName());
	                recommend.setSku(skuData);
	                recommend.setProductId(productDetail.getId());
	                recommend.setRegularPrice(productDetail.getRegularPrice());
	                recommend.setCurrentPrice(productDetail.getCurrentPrice());
	                recommend.setSize(productDetail.getSize());
	                
	                recommend.setInStock(productDetail.getInStock());
	                String imgLinkStr = productDetail.getImageLinks().get(0).getUri();
	                for(ImageLink imgLink : productDetail.getImageLinks()){
	                	if(imgLink.getRel().equals("thumbnail")){
	                		imgLinkStr = imgLink.getUri();
	                	}
	                }
	                recommend.setImagelink(imgLinkStr);
	                if(productDetail.getSale() != null){
		                recommend.setNote(productDetail.getSale().getDescription1() + " " + productDetail.getSale().getDescription2());
		                recommend.setDateText(productDetail.getSale().getDateText());
	                } else{
	                	recommend.setNote(" ");
	                	recommend.setDateText(" ");
	                }
	                recommend.setItemKey(productDetail.getItemKey());
	                recommend.setItemType(productDetail.getItemType());
	                
	                //Loop through coupon metadata to find sku match to assign coupon id to recommended products
	                /**
	            	 * match coupon metadata with product detail
	            	 */
	                recommend.setCouponId("");

	                //If couponse resp string contains upc, then loop through and map coupon id to recommend
	                if(couponResp.contains(upcStr)){
	                	System.out.println("[getInfoResponse]::"+upcStr+" has coupon");
		                for(CouponDAO couponDao : couponDaoArr){
		                	if(couponDao.getRequirementUpcs().contains(upcStr)){
		    	                recommend.setCouponId(couponDao.getCouponId());
		                	}
		                }
	                }
	                recList.add(recommend);
	                counter ++;
                } catch(Exception e){
                	System.out.println("Exception: "+e.getMessage());
                	System.out.println("Exception, product detail: \n"+ productResp);
                	logger.log(Level.INFO,"Exception: "+e.getMessage());
                	logger.log(Level.INFO, "Exception, product detail: ", productResp);
                }
        	}
        	ObjectMapper objMapper = new ObjectMapper();
        	recommendProduct.setRecommend(recList);
        	recommendProduct.setTotalCount(counter);
        	
        	CouponsAndRecommendationsDao cnrDao = new CouponsAndRecommendationsDao();
        	cnrDao.setRecommendProducts(recommendProduct);
        	cnrDao.setCouponDaoArr(couponDaoArr);
        	
        	String[] couponOmittedValuesArr = {

        		    "coupon_id",
        		    "featured",
        		    "requirement_description",
        		    "brand_name",
        		    "reward_upcs",
        		    "short_description",
        		    "image_url",
        		    "value",
        		    "external_id",
        		    "pos_live_date",
        		    "display_start_date",
        		    "display_end_date",
        		    "Category",
        		    "publication_id",
        		    "long_description",
        		    "requirement_upcs",
        		    "subcategory",
        		    "offer_priority",
        		    "expiration_date"
        	};
//        			"id", "__createdAt", "__updatedAt", "__version", "__deleted", "total_downloads", "targeting_buckets",
//        		    "targeted_offer", "tags", "enabled", "offer_type", "long_description_header"};
        	
        	FilterProvider filterCouponFields = new SimpleFilterProvider()
        			.addFilter("filterByValue", SimpleBeanPropertyFilter.filterOutAllExcept(couponOmittedValuesArr));
        	ObjectWriter writer = objMapper.writer(filterCouponFields);
        	
        	String cnrDaoStr = writer.writeValueAsString(cnrDao);
        	
        	endTime = System.currentTimeMillis();

        	System.out.println("[CouponsAndRecommendations]::Total process time (ms): "+ (endTime - startTime));
        	
			/**
			 * return response with coupon meta data & recommedation products
			 */
        	return this.createValidResponse(cnrDaoStr);
        } else{
	        matchedObjects = new JSONObject();
	        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCoupons
	                    + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;
	
	        //Execute Post
	        ServiceMappings serviceMappings = new ServiceMappings();
	        serviceMappings.setCouponMapping(this);
	
	        try {
	            String coupons = HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0);
	
	            if (query == "") {
	                return this.createValidResponse(coupons);
	            }
	
	            JSONArray matchedObjects2 = new JSONArray();
	            return this.createValidResponse(search(coupons, query, matchedObjects2).toString());
	        } catch (Exception e){
	            return this.createErrorResponse(e);
	        }
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

        if (query == "") {
            return coupons;
        }

        JSONArray matchedObjects2 = new JSONArray();
        return search(coupons, query, matchedObjects2).toString();
    }

    public CouponsAndRecommendations () {this.serviceType = new WakefernHeader();}

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
