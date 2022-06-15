package com.wakefern.api.proxy.wakefern.itemLocator;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationUtils;
import com.wakefern.wakefern.WakefernAuth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.Requests.Proxy + "/itemlocator")
public class GetItemLocator extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetItemLocator.class);

    private static final int TIMEOUT_MS = 10000;

    @GET
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path("/item/location/{storeId}/{upc}")
    public Response getItem(
            @PathParam("storeId") String storeId,
            @PathParam("upc") String upc) {
        Map<String, String> wkfn = new HashMap<>();

        try {
	        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;
            final String authToken = WakefernAuth.getInfo(ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));

            wkfn.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
	        wkfn.put("Authentication", authToken);

	        logger.trace("URL path: " + path);
	        
            return this.createValidResponse(HTTPRequest.executeGet(path, wkfn, TIMEOUT_MS));
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, ErrorType.PROXY_ITEMLOCATOR_GET_ITEM_LOCATOR);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "contentType", "application/json" );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
    		return this.createErrorResponse(errorData, e);
        }
    }


}