package com.wakefern.Coupons;

import com.wakefern.Wakefern.Models.WakefernHeader;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.request.HTTPRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */

@Path(ApplicationConstants.Requests.Coupons.GetCoupons)
public class Coupons extends BaseService {
    public JSONObject matchedObjects;
    @GET
    @Produces("application/*")
    public String getInfo(@DefaultValue(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)
                  @QueryParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam,
                  @DefaultValue("") @QueryParam("query") String query)
                  throws Exception, IOException {
        matchedObjects = new JSONObject();
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCoupons
                    + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        String coupons = HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader());

        if(query == ""){
            return coupons;
        }

        search(coupons, query);
        return matchedObjects.toString();
    }

    public Coupons () {this.serviceType = new WakefernHeader();}

    private JSONObject search(String coupons, String query){
        query = query.toLowerCase();

        JSONArray jsonArray = new JSONArray(coupons);

        for(Object coupon: jsonArray){
            JSONObject currentCoupon = (JSONObject) coupon;

            String brandName = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.brandName);
            String shortDescription = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.shortDescription);
            String longDescription = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.longDescription);
            String requirementDescription = currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.requirementDescription);

            if(brandName.toLowerCase().contains(query) || shortDescription.toLowerCase().contains(query)
                    || longDescription.toLowerCase().contains(query) || requirementDescription.toLowerCase().contains(query)){
                matchedObjects.append("", currentCoupon);
            }
        }
        return matchedObjects;
    }
}
