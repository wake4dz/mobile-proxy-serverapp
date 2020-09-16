package com.wakefern.api.mi9.v7.coupons.v2;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by kjng on 05/15/19.
 */
@Path(ApplicationConstants.Requests.CouponsV2.GetCouponIDByPromoCode)
public class GetCouponIDByPromoCode extends BaseService {

	private final static Logger logger = Logger.getLogger(GetCouponIDByPromoCode.class);
	private final static String url = ApplicationConstants.Requests.CouponsV2.BaseCouponURL + ApplicationConstants.Requests.CouponsV2.GetCouponIDByPromoCode;

	@POST
	@Produces(MWGApplicationConstants.Headers.json)
	public Response getInfoResponse(@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType,
    								@HeaderParam("Authorization") String authToken,
    								String jsonString) {
		
        try {
        	
        	Map<String, String> headerMap = new HashMap<String, String>();
	        headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
	        headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
	        String response = HTTPRequest.executePostJSON(url, jsonString, headerMap, VcapProcessor.getApiLowTimeout());
	        return this.createValidResponse(response);
			
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_V2_GET_COUPON_ID_BY_PROMO_CODE);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
            
        }
    }
}
