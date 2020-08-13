package com.wakefern.products;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wakefern.dao.sku.ProductSKUsDAO;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.services.MI9TimeoutService;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetProductBySkus extends BaseService {

	private final static Logger logger = Logger.getLogger(GetProductBySkus.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetProductBySkus() {
		this.requestPath = MWGApplicationConstants.Requests.Products.prefix
				+ MWGApplicationConstants.Requests.Products.prodsBySKUs;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.Products.accept)
	@Produces(MWGApplicationConstants.Headers.Products.accept)
	@Path(MWGApplicationConstants.Requests.Products.prodsBySKUs)
	public Response getResponse(@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String mwgStoreID,
			@QueryParam(MWGApplicationConstants.Requests.Params.Path.productSKU) List<String> skus,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.reservedTimeslot) String reservedTimeslot) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			ProductSKUsDAO skusDao = null;

			// divide sku list into 127 sku batch to avoid url length limits
			List<List<String>> skuList = getSkusList(this.formatSkus(skus));

			for (List<String> skusBatch : skuList) {
				ProductSKUsDAO newSkusDao = mapper.readValue(
						makeRequest(mwgStoreID, skusBatch, sessionToken, reservedTimeslot), ProductSKUsDAO.class);

				if (skusDao == null) {
					// the Source uri in response will only reflect the first request
					skusDao = newSkusDao;
				} else {
					skusDao.getItems().addAll(newSkusDao.getItems());
					skusDao.setItemCount(skusDao.getItemCount() + newSkusDao.getItemCount());
				}
			}

			return this.createValidResponse(mapper.writer().writeValueAsString(skusDao));

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_GET_PRODUCT_BY_SKUS);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"mwgStoreId", mwgStoreID, "skus", skus, "sessionToken", sessionToken, "accept", accept,
					"contentType", contentType, "reservedTimeslot", reservedTimeslot);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}

	/**
	 * Break up the skus list into 127 skus per batch to prepare for multiple sku
	 * call
	 *
	 * @param listOfSkuList
	 * @param skuList
	 * @return
	 */
	private List<List<String>> getSkusList(List<String> skuList) {
		final int chunkSize = 127;
		final int skuListSize = skuList.size();

		return IntStream.range(0, (skuListSize - 1) / chunkSize + 1).mapToObj(i -> {
			int startIndex = i * chunkSize;
			return skuList.subList(startIndex,
					skuListSize - chunkSize >= startIndex ? startIndex + chunkSize : skuListSize);
		}).collect(Collectors.toList());
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
	public String getInfo(String mwgStoreID, ArrayList<String> productSKUs, String sessionToken,
			String reservedTimeslot) throws Exception {
		return makeRequest(mwgStoreID, productSKUs, sessionToken, reservedTimeslot);
	}

	// -------------------------------------------------------------------------
	// Private Methods
	// -------------------------------------------------------------------------

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
	private String makeRequest(String mwgStoreID, List<String> productSKUs, String sessionToken,
			String reservedTimeslot) throws Exception {

		this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.Products.accept,
				MWGApplicationConstants.Headers.json, sessionToken, reservedTimeslot);
		this.requestParams = new HashMap<String, String>();
		this.queryParams = new HashMap<String, String>();

		// Build the Map of Request Path parameters
		this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, mwgStoreID);

		// Join skus into format: sku1,sku2,...,skuN
		final String skusString = productSKUs.stream().collect(Collectors.joining(","));

		// Build the Map of Request Query parameters
		this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sku, skusString);

		return this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutService.PRODUCTS_GET_BY_SKUS,
				MI9TimeoutService.getTimeout(MI9TimeoutService.PRODUCTS_GET_BY_SKUS));
	}

	/**
	 * Format (flatten) skus to a multi-element string sku array supporting both
	 * formats:
	 * 
	 * 1. sku=sku1,sku2,...,skuN ([[sku1,sku2,...,skuN]] 2. sku=sku1&sku=sku2
	 * ([sku1,sku2,...,skuN])
	 * 
	 * @param skus
	 * @return
	 */
	private List<String> formatSkus(List<String> skus) {
		if (skus.size() == 1) {
			return Arrays.asList(skus.get(0).split(","));
		}

		// 0 or more than 1 (already proper format)
		return skus;
	}
}