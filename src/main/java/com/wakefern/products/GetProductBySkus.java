package com.wakefern.products;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wakefern.dao.sku.Item;
import com.wakefern.dao.sku.ProductSKUsDAO;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetProductBySkus extends BaseService {
	
	private final static Logger logger = Logger.getLogger("GetProductBySkus");
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetProductBySkus() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.prodsBySKUs;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.Products.accept)
    @Produces(MWGApplicationConstants.Headers.Products.accept)
    @Path(MWGApplicationConstants.Requests.Products.prodsBySKUs)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
    		@QueryParam(MWGApplicationConstants.Requests.Params.Path.productSKU) ArrayList<String> skus,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) throws Exception, IOException {
        try {
    			/**
    			 * MWG only allows max of 128 SKUs request for product detail, if # of sku exceeds 128, cut it into 2 and 
    			 */
    			ObjectMapper mapper = new ObjectMapper();
    			List<List<String>> listOfSkuList= new ArrayList<List<String>>();

    			if(skus.size() > 128) {
    				logger.log(Level.INFO, "SKU size more than 128: "+skus.toString());
    			}
    			//divide sku list into 127 sku batch
    			getSkusList(listOfSkuList, skus);

    			String jsonResponse = makeRequest(mwgStoreID, listOfSkuList.get(0), sessionToken);
    			ProductSKUsDAO primSkusDao = mapper.readValue(jsonResponse, ProductSKUsDAO.class);
    			
    			for(int i = 1; i< listOfSkuList.size(); i++) {
        			jsonResponse = makeRequest(mwgStoreID, listOfSkuList.get(i), sessionToken);
        			ProductSKUsDAO secSkusDao = mapper.readValue(jsonResponse, ProductSKUsDAO.class);
        			for(Item item: secSkusDao.getItems()) {
        				primSkusDao.getItems().add(item);
        			}
        			primSkusDao.setItemCount(primSkusDao.getItemCount()+secSkusDao.getItemCount());
    			}
    			
    			ObjectWriter writer = mapper.writer();
    			jsonResponse = writer.writeValueAsString(primSkusDao);
        			
            return this.createValidResponse(jsonResponse);
        
        } catch (Exception e) {
			logger.log(Level.SEVERE, "Exception getting multiple SKU: "+e.getMessage());
            return this.createErrorResponse(e);
        }
    }
	
	/**
	 * If the Sku list has more than MWG allowable 128 Sku, recursively break it down into 127 sku batch to prepare for multiple sku call
	 * @param listOfSkuList
	 * @param skuList
	 * @return
	 */
	private List<List<String>> getSkusList(List<List<String>> listOfSkuList, List<String> skuList){
		if(skuList.size()<128) {
			listOfSkuList.add(skuList);
			return listOfSkuList;
		}
		List<String> secSku = new ArrayList<String>(skuList.subList(127,  skuList.size()));
		skuList.subList(127,  skuList.size()).clear();
		listOfSkuList.add(skuList);
		return getSkusList(listOfSkuList, secSku);
	}
	
	/**
	 * For Internal Use Only.<br>
	 * Not an API endpoint.<br>
	 * Get Product info by SKU.
	 * 
	 * @param mwgStoreID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	public String getInfo(String mwgStoreID, ArrayList<String> productSKUs, String sessionToken) throws Exception {
		return makeRequest(mwgStoreID, productSKUs, sessionToken);
	}
	
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

	/**
	 * Trigger the request to the MWG API.
	 * 
	 * @param mwgStoreID
	 * @param productSKU
	 * @param isMember
	 * @param sessionToken
	 * @return
	 * @throws Exception
	 */
	private String makeRequest(String mwgStoreID, List<String> productSKUs, String sessionToken) throws Exception {
		
		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.accept, MWGApplicationConstants.Headers.json, sessionToken);
		this.requestParams = new HashMap<String, String>();
		this.queryParams   = new HashMap<String, String>();
		
		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);
		
		StringBuilder sb = new StringBuilder();
		String skuStr = "";
		for(String prodSKUsStr : productSKUs) {
			sb.append(prodSKUsStr);
			sb.append("&sku=");
		}
		skuStr = sb.toString();

		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sku, skuStr.substring(0, skuStr.length()-"&sku=".length()));
		
		return this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.GetProductsBySKUs");
	}
}