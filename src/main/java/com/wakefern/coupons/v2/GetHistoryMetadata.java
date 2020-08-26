package com.wakefern.coupons.v2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by loicao on 10/15/18.
 */
@Path(ApplicationConstants.Requests.CouponsV2.GetHistoryMetadata)
public class GetHistoryMetadata extends BaseService {
	private final static Logger logger = Logger.getLogger(GetHistoryMetadata.class);

    @POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
    									@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType, 
    									@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn, 
    									String jsonString) throws Exception, IOException {
        //Execute POST
        this.requestPath = ApplicationUtils.constructCouponUrl(ApplicationConstants.Requests.CouponsV2.GetHistoryMetadata, fsn).toString();
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
        headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
        
        try {
        		String response = HTTPRequest.executePostJSON(this.requestPath, jsonString, headerMap, VcapProcessor.getApiLowTimeout());
            return this.createValidResponse(response);
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_V2_GET_HISTORY_METADATA);
        	
			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }
}
