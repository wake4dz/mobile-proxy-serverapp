package com.wakefern.Recommendations;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakefern.dao.recommend.Recommend;
import com.wakefern.dao.recommend.RecommendProductDAO;
import com.wakefern.dao.recommend.RecommendVPUPCsDAO;
import com.wakefern.dao.sku.ImageLink;
import com.wakefern.dao.sku.Item;
import com.wakefern.dao.sku.ProductSKUsDAO;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.products.GetProductBySkus;

import com.wakefern.request.HTTPRequest;

@Path(ApplicationConstants.Requests.Recommendations.ProductRecommendationsv2)
public class ProductsRecommendation extends BaseService {

	private final static Logger logger = Logger.getLogger(ProductsRecommendation.class);
	
    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("{externalStoreId}/email/{email}/fsn/{ppc}")
    public Response getInfoResponse(
        @PathParam("externalStoreId") String externalStoreId,
        @DefaultValue("") @PathParam("email") String email,
		@PathParam("ppc") String ppc,
		@DefaultValue("") @QueryParam("storeId") String storeId, //pseudoStoreId
		
		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
    ) {
    	
    		// This request relies on a legacy endpoint maintained by Wakefern.
    		// Ignore the session token sent by the UI.
    		// Use the legacy Wakefern Auth Token instead.
    	try {
			int count = 0;
			GetProductBySkus getProductBySku = new GetProductBySkus();
			ObjectMapper mapper = new ObjectMapper();
			RecommendProductDAO recProdDao = new RecommendProductDAO();
			List<Recommend> recmdList = new ArrayList<Recommend>();
			
			this.requestHeader = new MWGHeader();
	        this.requestToken  = MWGApplicationConstants.getProductRecmdAuthToken();
	        this.requestPath = ApplicationConstants.Requests.Recommendations.ProductRecommendationsv2 + "/"+externalStoreId+"/email/"+email+"/fsn/"+ppc;
	        
	        ServiceMappings secondMapping = new ServiceMappings();
	        secondMapping.setMappingWithURL(this, ApplicationConstants.Requests.Recommendations.BaseRecommendationsURL);
	    		String secondMapPath = secondMapping.getPath();

      
        		// CALL & GET LIST OF RECOMMENDED SKUs
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
        		ArrayList<String> skuList = new ArrayList<String>();
        		for(String upcStr : upcList){
        			try{
        				skuList.add(upcStr.substring(2));
        			} catch(Exception e) {
        				//error
        			}
        		}
			// Bundle SKUs and call MWG to retrieve SKUs' detail.
        		String skusResp = getProductBySku.getInfo(storeId, skuList, sessionToken);
        		ProductSKUsDAO pdSKUDao = mapper.readValue(skusResp, ProductSKUsDAO.class);
        		for(Item item : pdSKUDao.getItems()) {

            		Recommend recmd = new Recommend();
            		recmd.setAisle(item.getAisle());		recmd.setBrand(item.getBrand());
            		recmd.setCategory(item.getCategory());	recmd.setName(item.getName());
            		recmd.setSku(item.getSku());			recmd.setProductId(Integer.parseInt(item.getId()));
            		recmd.setSize(item.getSize());			recmd.setInStock(item.getInStock());
            		for(ImageLink imgLnk : item.getImageLinks()){
            			if(imgLnk.getRel().equalsIgnoreCase("large")){
            				recmd.setImagelink(imgLnk.getUri());
            			}
            		}
            		recmd.setItemKey(item.getItemKey());	recmd.setItemType(item.getItemType());
            		recmd.setRegularPrice(item.getRegularPrice()); recmd.setCurrentPrice(item.getCurrentPrice());
//		            		recmd.setNote(pddao.get);
//		            		recmd.setDateText(pddao.getda);
//		            		recmd.setCouponId(couponId);
            		recmdList.add(recmd);
            		count ++;
        		}
        		recProdDao.setTotalCount(count);
        		recProdDao.setRecommend(recmdList);

        		String resp = mapper.writer().writeValueAsString(recProdDao);
        		
        		return this.createValidResponse(resp);
        
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.RECOMMENDATIONS_PRODUCTS_RECOMMENDATION);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "externalStoreId", externalStoreId, 
        			"email", email, "ppc", ppc, "storeId", storeId, 
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
