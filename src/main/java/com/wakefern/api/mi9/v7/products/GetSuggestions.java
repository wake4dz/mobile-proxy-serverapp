package com.wakefern.api.mi9.v7.products;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MI9TimeoutRegistry;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.net.URLEncoder;

@Path(MWGApplicationConstants.Requests.Products.prefix)
public class GetSuggestions extends BaseService {

	private final static Logger logger = Logger.getLogger(GetSuggestions.class);

	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public GetSuggestions() {
        this.requestPath = MWGApplicationConstants.Requests.Products.prefix + MWGApplicationConstants.Requests.Products.suggestedProds;
    }

	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Products.suggestedProds)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.storeID) String storeID,

    		@QueryParam(MWGApplicationConstants.Requests.Params.Query.searchTerm) String searchTerm,

    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken
	) {
        try {
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, sessionToken);
			this.requestParams = new HashMap<>();
			this.queryParams   = new HashMap<>();

			// Build the Map of Request Path parameters
			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.storeID, storeID);
			this.queryParams.put(MWGApplicationConstants.Requests.Params.Query.searchTerm, URLEncoder.encode(searchTerm, "UTF-8"));

            String jsonResponse = this.mwgRequest(BaseService.ReqType.GET, null, MI9TimeoutRegistry.PRODUCTS_GET_SUGGEST, MI9TimeoutRegistry.getTimeout(MI9TimeoutRegistry.PRODUCTS_GET_SUGGEST));

            return this.createValidResponse(jsonResponse);
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.PRODUCTS_GET_SUGGESTIONS);

        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
        			"storeId", storeID, "searchTerm", searchTerm,
        			"sessionToken", sessionToken,
        			"accept", accept,
        			"contentType", contentType );

    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}

