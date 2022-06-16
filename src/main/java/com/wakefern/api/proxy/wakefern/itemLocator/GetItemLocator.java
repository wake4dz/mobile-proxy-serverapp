package com.wakefern.api.proxy.wakefern.itemLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.ErrorType;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Proxy + "/itemlocator")
public class GetItemLocator extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetItemLocator.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/item/location/{storeId}/{upc}")
	public Response getItem(@PathParam("storeId") String storeId, 
			@PathParam("upc") String upc) { // note: upc's last digit of checksum of an UPC is already removed by caller
		Map<String, String> wkfn = new HashMap<>();

		try {
			String path = WakefernApplicationConstants.ItemLocator.baseURL
					+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;

			final String authToken = WakefernAuth.getInfo(WakefernApplicationConstants
					.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
			wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
			wkfn.put("Authentication", authToken);

			logger.trace("URL path: " + path);

			return this.createValidResponse(HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout()));
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_ITEMLOCATOR_GET_ITEM_LOCATOR);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", "application/json");

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

	/*
	 * for mobile app's items locator info used for a shopping cart
	 */
	@POST
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/item/cart/{storeId}")
	public Response getCartItemLocator(@PathParam("storeId") String storeId, 
			String jsonBody) { // note: upc's last checksum digit is already removed before calling this API
		
		int partitionNumber = 0;
		int currentListPositon = 0;
				
		JSONObject processedItemsJObj = new JSONObject();

		try {
			JSONObject itemsJObj = new JSONObject(jsonBody);
			JSONArray itemsJArray = (JSONArray) itemsJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);

			int itemsSize = itemsJArray.length();
			
			// calculate a right partition number
		    partitionNumber = itemsSize/ WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE;
		    if (itemsSize % WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE > 0) {
		    	partitionNumber++;
		    }

			List<String> partitionItemsList = null;
			StringBuilder partitionItemsSB = null;

			logger.trace("ITEM_PARTITION_SIZE: " + WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE);
			
			for (int i=0; partitionNumber > i; i++) {
				partitionItemsList = new ArrayList<>();
				partitionItemsSB = new StringBuilder();
				
				// build each partition data to be used for a Wakefern's Item Locator API call
				while ((WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE * (i + 1) > currentListPositon) && (itemsSize > currentListPositon)) {
						partitionItemsSB.append(itemsJArray.getString(currentListPositon) + ",");
						partitionItemsList.add(itemsJArray.getString(currentListPositon));
						
						currentListPositon++;
				}
		
				String path = WakefernApplicationConstants.ItemLocator.baseURL
						+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + partitionItemsSB.toString();
	
				Map<String, String> wkfn = new HashMap<>();
				final String authToken = WakefernAuth.getInfo(WakefernApplicationConstants
						.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
				wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
				wkfn.put("Authentication", authToken);
	
				String responseData = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());
	
				logger.trace("partitionNumber: " + (i + 1));
				logger.trace("URL path: " + path);
				logger.trace("PartitionItemsSB: " + partitionItemsSB.toString());
				logger.trace("PartitionItemsList: " + partitionItemsList.toString());
				logger.trace("responseData: " + responseData);
				
		        processedItemsJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Items, 
		        		ItemLocatorUtils.generateItemLocator(partitionItemsList, responseData));
			}

			return this.createValidResponse(processedItemsJObj.toString());

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_ITEMLOCATOR_GET_CART_ITEM_LOCATOR);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", "application/json");

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

}