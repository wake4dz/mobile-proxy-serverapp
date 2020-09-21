package com.wakefern.api.mi9.v7.coupons;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernHeader;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * Created by zacpuste on 9/27/16.
 */
@Path(ApplicationConstants.Requests.Coupons.GetCouponIdByPPC)
public class CouponIdListByPPC extends BaseService {

	private final static Logger logger = Logger.getLogger(CouponAddToPPC.class);
	
    public JSONObject matchedObjects;

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    public Response getInfoResponse(@DefaultValue(WakefernApplicationConstants.Coupons.Metadata.PPC_All) 
    								@QueryParam(WakefernApplicationConstants.Coupons.Metadata.PPC) String ppcParam,
    					    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    					    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
                                    @HeaderParam("Authorization") String authToken) {
       
    	try {
    		// for /getCouponIDListByPPC endpoint
	
    		String mockCoupon = "{ \"available_ids_array\": [\"1\"],  \"clipped_active_ids_array\": []}";
    		
        	return this.createValidResponse(mockCoupon);	
    		
        } catch (Exception e){
        	LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_COUPON_ID_LIST_BY_PPC);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), 
        			"ppcParam", ppcParam, "authToken", authToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

    public String getInfo(String ppcParam, String authToken) throws Exception, IOException {
    	
    		this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;

        matchedObjects = new JSONObject();
        prepareResponse(ppcParam);

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        return HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getGenericHeader(), VcapProcessor.getApiLowTimeout());
    }

    public CouponIdListByPPC() {     this.requestHeader = new WakefernHeader();    }

    private void prepareResponse(String ppcParam){
        this.requestPath = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCouponIdByPPC
                + WakefernApplicationConstants.Coupons.Metadata.PPCQuery + ppcParam;
    }
}
