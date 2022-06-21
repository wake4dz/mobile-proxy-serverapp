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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;
import com.wakefern.wakefern.itemLocator.ItemLocatorUtils;

@Path(ApplicationConstants.Requests.Proxy + "/itemlocator")
public class GetItemLocator extends BaseService {

	private final static Logger logger = LogManager.getLogger(GetItemLocator.class);

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Deprecated
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
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", "application/json");

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

	@GET
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/item/location/v2/{storeId}/{upc}")
	public Response getItemV2(@PathParam("storeId") String storeId,
							@PathParam("upc") String upc) {
		Map<String, String> wkfn = new HashMap<>();

		try {
			String path = WakefernApplicationConstants.ItemLocator.baseURL
					+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;

			final String authToken = WakefernAuth.getInfo(WakefernApplicationConstants
					.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
			wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
			wkfn.put("Authentication", authToken);

			logger.trace("URL path: " + path);

			String response = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());
			JSONObject itemLocatorObj = ItemLocatorUtils.generateItemLocator(upc, response);
			return this.createValidResponse(itemLocatorObj.toString());
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, ErrorType.PROXY_ITEMLOCATOR_GET_ITEM_LOCATOR_V2);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", "application/json");

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

	/*
	 * Fetch item locator info by cart skus
	 */
	@POST
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path("/item/cart/{storeId}")
	public Response getCartItemLocator(@PathParam("storeId") String storeId, 
			String jsonBody) {

		int currentListPosition = 0;
				
		JSONObject processedItemsJObj = new JSONObject();

		try {
			JSONObject itemsJObj = new JSONObject(jsonBody);
			JSONArray itemsJArray = (JSONArray) itemsJObj.get(WakefernApplicationConstants.Mi9V8ItemLocator.Items);

			final int itemsSize = itemsJArray.length();
			final int numRequests = ItemLocatorUtils.getNumRequests(itemsSize);

			List<String> partitionItemsList;
			StringBuilder partitionItemsSB;

			final String authToken = WakefernAuth.getInfo(WakefernApplicationConstants
					.getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));


			JSONObject skusToItemLocatorDict = new JSONObject();

			for (int i=0; i < numRequests; i++) {
				partitionItemsList = new ArrayList<>();
				partitionItemsSB = new StringBuilder();
				
				// build each partition data to be used for a Wakefern's Item Locator API call
				while ((WakefernApplicationConstants.Mi9V8ItemLocator.ITEM_PARTITION_SIZE * (i + 1) > currentListPosition) && (itemsSize > currentListPosition)) {
					partitionItemsSB.append(itemsJArray.getString(currentListPosition)).append(",");
					partitionItemsList.add(itemsJArray.getString(currentListPosition));
						
					currentListPosition++;
				}
		
				final String path = WakefernApplicationConstants.ItemLocator.baseURL
						+ WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + partitionItemsSB;
	
				Map<String, String> wkfn = new HashMap<>();
				wkfn.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
				wkfn.put("Authentication", authToken);

				String responseData = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());
	
				logger.trace("partitionNumber: " + (i + 1));
				logger.trace("URL path: " + path);
				logger.trace("PartitionItemsSB: " + partitionItemsSB);
				logger.trace("PartitionItemsList: " + partitionItemsList);
				logger.trace("responseData: " + responseData);
	
				Map<String, JSONObject> processedPartitionItems = ItemLocatorUtils.generateItemLocator(partitionItemsList, responseData);

				logger.trace("processedPartitionItems: " + processedPartitionItems);
				
				// using for-each loop for iteration over Map.entrySet()
		        for (Map.Entry<String, JSONObject> entry : processedPartitionItems.entrySet())  {
		        	logger.trace("entry.getKey(): " + entry.getKey() + ", entry.getValue():" + entry.getValue());
					skusToItemLocatorDict.put(entry.getKey(), entry.getValue());
		        }
			}

			// create response object
			processedItemsJObj.put(WakefernApplicationConstants.Mi9V8ItemLocator.Items, skusToItemLocatorDict);
			return this.createValidResponse(processedItemsJObj.toString());

		} catch (Exception e) {
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"contentType", "application/json");

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}