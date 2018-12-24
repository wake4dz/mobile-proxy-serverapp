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
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Created by loicao on 10/16/18.
 */
@Path(ApplicationConstants.Requests.CouponsV2.RemoveCouponFromPPC)
public class RemoveCouponFromPPC extends BaseService {
	private final static Logger logger = Logger.getLogger(RemoveCouponFromPPC.class);

    @POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
    									@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType, 
    									@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn, 
    									@QueryParam(ApplicationConstants.Requests.CouponsV2.coupon_id) String coupon_id, 
    									String jsonString) throws Exception, IOException {
        //Execute POST
        StringBuilder sb = ApplicationUtils.constructCouponUrl(ApplicationConstants.Requests.CouponsV2.RemoveCouponFromPPC, fsn);
		sb.append(WakefernApplicationConstants.CouponsV2.QueryParam.CouponParam);
		sb.append(coupon_id);
		sb.append(WakefernApplicationConstants.CouponsV2.QueryParam.ClipSource);
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
        headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
        		String response = HTTPRequest.executePostJSON(sb.toString(), jsonString, headerMap, 0);
            return this.createValidResponse(response);
        } catch (Exception e){
			String errorData = LogUtil.getRequestData("RemoveCouponFromPPC::Exception", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }
}
