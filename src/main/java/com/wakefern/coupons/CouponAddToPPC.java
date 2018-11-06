package com.wakefern.coupons;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernHeader;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import java.io.IOException;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;


/**
 * Created by zacpuste on 9/27/16.
 */
@Path(ApplicationConstants.Requests.Coupons.CouponAddPPC)
public class CouponAddToPPC extends BaseService {

	private final static Logger logger = Logger.getLogger(CouponAddToPPC.class);
	
    public JSONObject matchedObjects;

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    public Response getInfoResponse(@DefaultValue("") @QueryParam(WakefernApplicationConstants.Coupons.Metadata.PPC) String ppcParam,
                                    @DefaultValue("") @QueryParam(WakefernApplicationConstants.Coupons.Metadata.CouponId) String couponId,
                            		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
                            		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
                                    @HeaderParam("Authorization") String authToken) {

    	try {
	    	this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;
	
	        matchedObjects = new JSONObject();
	        prepareResponse(ppcParam, couponId);
	
	        //Execute Post
	        ServiceMappings serviceMappings = new ServiceMappings();
	        serviceMappings.setCouponMapping(this);

            return this.createValidResponse(HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0));
        } catch (Exception e){

        	LogUtil.addErrorMaps(e, MwgErrorType.COUPONS_COUPON_ADD_TO_PPC);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRevelantStackTrace(e), 
        			"ppcParam", ppcParam, "couponID", couponId, 
        			"authToken", authToken, "accept", accept, "contentType", contentType);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }

    public String getInfo(String ppcParam, String couponId, String authToken) throws Exception, IOException {
    	
        this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;

        matchedObjects = new JSONObject();
        prepareResponse(ppcParam, couponId);

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        return HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0);
    }

    public CouponAddToPPC() {     this.requestHeader = new WakefernHeader();    }

    private void prepareResponse(String ppcParam, String couponId){
        this.requestPath = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.CouponAddPPC
                + WakefernApplicationConstants.Coupons.Metadata.PPCQuery + ppcParam
                + WakefernApplicationConstants.Coupons.Metadata.CouponParam + couponId
                + WakefernApplicationConstants.Coupons.Metadata.ClipSource + WakefernApplicationConstants.Coupons.Metadata.ClipSource_App_SR;
        logger.debug("[prepareResponse]::Add coupon to PPC - " + this.requestPath);
    }
}
