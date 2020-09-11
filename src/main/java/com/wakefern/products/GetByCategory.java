package com.wakefern.products;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.net.URLEncoder;
import java.util.HashMap;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetByCategory extends BaseService {

	private final static Logger logger = Logger.getLogger(GetByCategory.class);

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public GetByCategory() {
		this.requestPath = MWGApplicationConstants.Requests.Products.prefix
				+ MWGApplicationConstants.Requests.Products.prodsByCat;
	}

	@GET
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Products.prodsByCat)
	public Response getResponse(@PathParam(MWGApplicationConstants.Requests.Params.Path.categoryID) String categoryID,
			@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,

			@QueryParam(MWGApplicationConstants.Requests.Params.Query.isMember) String isMember,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.userID) String userID,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.excluded) String prodsToExclude,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.filters) String[] prodfilters,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String searchTerm,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.skip) String skip,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.sortOrder) String sortOrder,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchBySound) String searchBySound,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.take) String take,

			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.reservedTimeslot) String reservedTimeslot) {
		try {
			this.requestHeader = new MWGHeader(accept == null ? MWGApplicationConstants.Headers.Products.productList : accept,
					MWGApplicationConstants.Headers.json, sessionToken, reservedTimeslot);
			this.requestParams = new HashMap<>();
			this.queryParams = new HashMap<>();

			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.categoryID, categoryID);

			/**
			 * To handle multiple query filter in request parameter &fq ie
			 * http://shopritemobileproddep.mybluemix.net/api/product/v7/products/category/520589/store/027F776?take=10&skip=0&fq=brand%3AArmour&fq=brand%3ABenecol
			 */
			StringBuilder sb = new StringBuilder();
			for (String prodFilter : prodfilters) {
				sb.append(URLEncoder.encode(prodFilter, "UTF-8"));
				sb.append("&fq=");
			}
			String prodFilterStr = sb.toString();

			// Build the Map of Request Query parameters
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.isMember, isMember);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.userID, userID);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.excluded, prodsToExclude);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.filters,
					prodFilterStr.isEmpty() ? prodFilterStr
							: prodFilterStr.substring(0, prodFilterStr.length() - "&fq=".length()));
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, searchTerm);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.skip, skip);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.sortOrder, sortOrder);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchBySound, searchBySound);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.take, take);

			String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.products.GetByCategory");

			if (LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
					String trackData = LogUtil.getRequestData("storeId", storeID, "categoryID", categoryID, "isMember",
							isMember, "prodsToExclude", prodsToExclude, "prodfilters", prodfilters, "searchTerm",
							searchTerm, "skip", skip, "sortOrder", sortOrder, "searchBySound", searchBySound, "take",
							take, "userID", userID, "sessionToken", sessionToken, "accept", accept, "contentType",
							contentType);
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}

			return this.createValidResponse(jsonResponse);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_GET_BY_CATEGORY);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeId",
					storeID, "categoryID", categoryID, "isMember", isMember, "prodsToExclude", prodsToExclude,
					"prodfilters", prodfilters, "searchTerm", searchTerm, "skip", skip, "sortOrder", sortOrder,
					"searchBySound", searchBySound, "take", take, "userID", userID, "sessionToken", sessionToken,
					"accept", accept, "contentType", contentType, "reservedTimeslot", reservedTimeslot);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse(errorData, e);
		}
	}
}
