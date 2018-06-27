package com.wakefern.coupons;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.WakefernHeader;

import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;

/**
 * Created by zacpuste on 9/20/16.
 */
@Path(ApplicationConstants.Requests.Coupons.GetCouponId)
public class CouponIdList extends BaseService {
    public JSONObject matchedObjects;

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    public Response getInfoResponse(@DefaultValue(WakefernApplicationConstants.Coupons.Metadata.PPC_All)
                                    @QueryParam(WakefernApplicationConstants.Coupons.Metadata.PPC) String ppcParam,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {

    		this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;

        matchedObjects = new JSONObject();
        prepareResponse(ppcParam);

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String ppcParam, String authToken) throws Exception, IOException {
    	
    		this.requestToken = ApplicationConstants.Requests.Tokens.couponToken;
    	
        matchedObjects = new JSONObject();
        prepareResponse(ppcParam);

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        return HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0);
    }

    public CouponIdList() {     this.requestHeader = new WakefernHeader();    }

    private void prepareResponse(String ppcParam){
        this.requestPath = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCouponId
                + WakefernApplicationConstants.Coupons.Metadata.PPCQuery + ppcParam;
    }
}