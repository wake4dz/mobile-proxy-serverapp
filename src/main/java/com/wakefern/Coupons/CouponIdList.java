package com.wakefern.Coupons;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.request.HTTPRequest;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/20/16.
 */
@Path(ApplicationConstants.Requests.Coupons.GetCouponId)
public class CouponIdList extends BaseService {
    public JSONObject matchedObjects;

    @GET
    @Produces("application/*")
    public String getInfo(@DefaultValue(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)
                          @QueryParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam)
                          throws Exception, IOException {
        matchedObjects = new JSONObject();
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCouponId
                + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        return HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader());
    }

    public CouponIdList() {     this.serviceType = new WakefernHeader();    }
}