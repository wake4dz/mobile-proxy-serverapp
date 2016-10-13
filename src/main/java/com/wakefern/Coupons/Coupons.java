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
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */

@Path(ApplicationConstants.Requests.Coupons.GetCoupons)
public class Coupons extends BaseService {
    public JSONObject matchedObjects;
    @GET
    @Produces("application/*")
    public Response getInfoResponse(@DefaultValue(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All)
                  @QueryParam(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC) String ppcParam,
                            @DefaultValue("") @QueryParam("query") String query)
                  throws Exception, IOException {

        matchedObjects = new JSONObject();
        this.path = ApplicationConstants.Requests.Coupons.BaseCouponURL + ApplicationConstants.Requests.Coupons.GetCoupons
                    + WakefernApplicationConstants.Requests.Coupons.Metadata.PPCQuery + ppcParam;

        //Execute Post
        ServiceMappings serviceMappings = new ServiceMappings();
        serviceMappings.setCouponMapping(this);

        try {
            String coupons = HTTPRequest.executePostJSON(serviceMappings.getPath(), "", serviceMappings.getgenericHeader());

            if (query == "") {
                return this.createValidResponse(coupons);
            }

            JSONArray matchedObjects2 = new JSONArray();
            return this.createValidResponse(search(coupons, query, matchedObjects2).toString());
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public Coupons () {this.serviceType = new WakefernHeader();}

    private JSONObject search(String coupons, String query, JSONArray matchedObjects2){
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
                matchedObjects2.put(matchedObjects2.length(), currentCoupon);
            }
        }
        return sortCouponsByCategory(matchedObjects2);
    }

    private JSONObject sortCouponsByCategory( JSONArray matchedObjects2 ){
        Set<String> categoryIds = new HashSet<>();
        JSONObject jsonObject = new JSONObject();

        for (Object coupon: matchedObjects2){
            JSONObject currentCoupon = (JSONObject) coupon;
            categoryIds.add(currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.category));
        }

        JSONArray retval = new JSONArray();
        for(String id: categoryIds){
            JSONObject formatting = new JSONObject();
            JSONArray currentId = new JSONArray();
            for(Object coupon: matchedObjects2){
                JSONObject currentCoupon = (JSONObject) coupon;
                String category = (currentCoupon.getString(WakefernApplicationConstants.Requests.Coupons.Search.category));
                if(category.equals(id)) {
                    currentId.put(currentId.length(), currentCoupon);
                }
            }
            formatting.put("Name", id);
            formatting.put("Items", currentId);
            retval.put(retval.length(), formatting);
        }
        jsonObject.put("CouponCategories", retval);
        jsonObject.put("totalCoupons", totalCoupons(retval));
        return jsonObject;
    }

    private int totalCoupons(JSONArray jsonArray){
        int retval = 0;
        for(Object category: jsonArray){
            JSONObject currentCategory = (JSONObject) category;
            retval += currentCategory.getJSONArray("Items").length();
        }
        return retval;
    }
}
