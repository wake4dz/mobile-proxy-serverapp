package com.wakefern.api.wakefern.items.location;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernAuth;

/**
 * Created by loicao on 09/03/19.
 * 
 * Danny's note on 2/5/2020: it is not used currently
 */
@Path(WakefernApplicationConstants.ItemLocator.prefix)
public class ObtainItemsInfoToken extends BaseService {
	private final static Logger logger = LogManager.getLogger(ObtainItemsInfoToken.class);

	public JSONObject matchedObjects;
	
    @POST
    @Produces(MWGApplicationConstants.Headers.json)
    @Consumes(MWGApplicationConstants.Headers.json)
    @Path(WakefernApplicationConstants.ItemLocator.itemInfo_tokenPath)
    public Response getItemLocatorResponse(@HeaderParam(ApplicationConstants.Requests.Header.contentAccept) String acceptType, 
    							@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
    							@HeaderParam(ApplicationConstants.Requests.Header.appCode) String appCode,
    							String jsonStr) {
        try {
	        String response = WakefernAuth.getItemInfo(appCode);
            return this.createValidResponse(response);
        } catch (Exception e){
			String errorData = LogUtil.getRequestData("GetItemsLocationToken::Exception", LogUtil.getRelevantStackTrace(e));
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }
}