package com.wakefern.Recommendations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakefern.dao.product.ImageLink;
import com.wakefern.dao.product.ProductDetailDAO;
import com.wakefern.dao.recommend.Recommend;
import com.wakefern.dao.recommend.RecommendProductDAO;
import com.wakefern.dao.recommend.RecommendVPUPCsDAO;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.products.GetBySku;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Recommendations.ProductRecommendationsv2)
public class ProductsRecommendation extends BaseService {

	private final static Logger logger = Logger.getLogger("RecommendProducts");
	
    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("{externalStoreId}/email/{email}/fsn/{ppc}")
    public Response getInfoResponse(
        @PathParam("externalStoreId") String externalStoreId,
        @DefaultValue("") @PathParam("email") String email,
		@PathParam("ppc") String ppc,
		@DefaultValue("") @QueryParam("storeId") String storeId, //pseudoStoreId
		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
    ) throws Exception, IOException {
    	
    		// This request relies on a legacy endpoint maintained by Wakefern.
    		// Ignore the session token sent by the UI.
    		// Use the legacy Wakefern Auth Token instead.

		int count = 0;
		GetBySku gbs = new GetBySku();
		ObjectMapper mapper = new ObjectMapper();
		RecommendProductDAO recProdDao = new RecommendProductDAO();
		List<Recommend> recmdList = new ArrayList<Recommend>();
		
		this.requestHeader = new MWGHeader();
        this.requestToken  = MWGApplicationConstants.getProductRecmdAuthToken();
        this.requestPath = ApplicationConstants.Requests.Recommendations.ProductRecommendationsv2 + "/"+externalStoreId+"/email/"+email+"/fsn/"+ppc;
        
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMappingWithURL(this, ApplicationConstants.Requests.Recommendations.BaseRecommendationsURL);
    		String secondMapPath = secondMapping.getPath();

        try {
        		// CALL & GET LIST OF RECOMMENDED SKU
        		String jsonResp = HTTPRequest.executeGet(secondMapPath, secondMapping.getgenericHeader(), 0);
        		RecommendVPUPCsDAO recUPCDao = mapper.readValue(jsonResp, RecommendVPUPCsDAO.class);
        		List<String> upcList = recUPCDao.getProducts();
        		
        		/**
        		 DATA FROM V1
			      "Aisle": "",
			      "Brand": "Bananas",
			      "Category": "Produce",
			      "Name": "Yellow",
			      "Sku": "000000040112",
			      "ProductId": "179860",
			      "RegularPrice": "$0.59",
			      "CurrentPrice": "$0.39/ea (avg.)",
			      "Size": "0.8 lb",
			      "InStock": true,
			      "Imagelink": "http://content.shoprite.com/legacy/productimagesroot/SJ/0/179860.jpg",
			      "Note": "On Sale! Reduced Price! $0.10 off Min Qty: 1, Limit: 12 Items",
			      "DateText": "Sale price valid from Sunday, Mar 11, 2018 until Saturday, Mar 17, 2018",
			      "coupon_id": "",
			      "ItemKey": "product~179860~0.8 lb",
			      "ItemType": "Product"
        		 */
        		
        		for(String upcStr : upcList){
        			try{
        				// FOR EACH SKU, CALL MWG & GET PRODUCT DETAIL.
	            		String skuResp = gbs.getInfo(storeId, upcStr.substring(2), "true", sessionToken);
	            		ProductDetailDAO pddao = mapper.readValue(skuResp, ProductDetailDAO.class);
	            		Recommend recmd = new Recommend();
	            		recmd.setAisle(pddao.getAisle());		recmd.setBrand(pddao.getBrand());
	            		recmd.setCategory(pddao.getCategory());	recmd.setName(pddao.getName());
	            		recmd.setSku(pddao.getSku());			recmd.setProductId(pddao.getId());
	            		recmd.setSize(pddao.getSize());			recmd.setInStock(pddao.getInStock());
	            		for(ImageLink imgLnk : pddao.getImageLinks()){
	            			if(imgLnk.getRel().equalsIgnoreCase("large")){
	            				recmd.setImagelink(imgLnk.getUri());
	            			}
	            		}
	            		recmd.setItemKey(pddao.getItemKey());	recmd.setItemType(pddao.getItemType());
	            		recmd.setRegularPrice(pddao.getRegularPrice()); recmd.setCurrentPrice(pddao.getCurrentPrice());
//	            		recmd.setNote(pddao.get);
//	            		recmd.setDateText(pddao.getda);
//	            		recmd.setCouponId(couponId);
	            		recmdList.add(recmd);
	            		count ++;
        			} catch(Exception e){
        				//Product not found for sku, do nothing//
//        				logger.log(Level.INFO, "Exception getting SKU - SKU does not exist.");
        			}
        		}
        		recProdDao.setTotalCount(count);
        		recProdDao.setRecommend(recmdList);

        		String resp = mapper.writer().writeValueAsString(recProdDao);
        		
        		return this.createValidResponse(resp);
        
        } catch (Exception e){
        	logger.log(Level.SEVERE, "[getInfoResponse]::Product Recommendation Exception!!!, URL: " + secondMapPath + e.getMessage());
            return this.createErrorResponse(e);
        }
    }
}
