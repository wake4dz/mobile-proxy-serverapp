package com.wakefern.Coupons;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.request.HTTPRequest;
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
    @Produces("application/*")
    public Response getInfoResponse(@DefaultValue(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)
                                    @QueryParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.couponToken;
        }else{
        	this.token = authToken;
        }

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
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.couponToken;
        }else{
        	this.token = authToken;
        }

        matchedObjects = new JSONObject();
        prepareResponse(ppcParam);

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        return HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader(), 0);
    }

    public CouponIdList() {     this.serviceType = new WakefernHeader();    }

    private void prepareResponse(String ppcParam){
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCouponId
                + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;
    }
}