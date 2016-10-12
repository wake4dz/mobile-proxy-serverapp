package com.wakefern.ListsPlanning;

import com.wakefern.Coupons.Coupons;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(ApplicationConstants.Requests.Planning.ShoppingListUser)
public class ComparePastPurchasesCoupons extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/pastPurchasesCoupons")
    public String getInfo(@PathParam("userId") String userId, @QueryParam("take") String take, @QueryParam("skip") String skip,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        Coupons coupons = new Coupons();
        String couponList = coupons.getInfo(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC, "");
        Set<String> couponIds = getCouponIds(couponList);

        GetPastPurchases getPastPurchases = new GetPastPurchases();
        String pastPurchases = getPastPurchases.getInfo(userId, "9999", "0", this.token);
        return getPurchaseIds(pastPurchases, couponIds, skip, take).toString();
    }

    public ComparePastPurchasesCoupons() {
        this.serviceType = new MWGHeader();
    }

    private JSONObject getPurchaseIds(String pastPurchases, Set<String> coupons, String skip, String take ){
        int matches = 0;
        int skips = 0;
        JSONObject retval = new JSONObject();
        Object jsonObject = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                .getJSONObject(ApplicationConstants.Planning.ShoppingListItems).get(ApplicationConstants.Planning.ShoppingListItem);
        JSONArray jsonArray = (JSONArray) jsonObject;
        for(Object listItem: jsonArray){
            if(skips < Integer.parseInt(skip)){
                skips++;
                continue;
            }

            if(matches == Integer.parseInt(take)){
                return retval;
            }

            JSONObject currentListItem = (JSONObject) listItem;
            String sku = currentListItem.getJSONObject(ApplicationConstants.Planning.Product)
                    .getString(ApplicationConstants.Planning.SKU);

            for(String couponId: coupons) {
                if (sku.contains(couponId)) {
                    retval.append( ApplicationConstants.Planning.Matches, currentListItem);
                    matches++;
                }
            }
        }

        //Should only be reached is matches < take
        return retval;
    }

    private Set<String> getCouponIds(String couponList){
        Set<String> retval = new HashSet<>();
        JSONArray jsonArray = new JSONArray(couponList);
        for(Object coupon: jsonArray) {
            JSONObject jsonObject = (JSONObject) coupon;
            String upcList = jsonObject.getString(ApplicationConstants.Planning.requirement_upc);
            String upcs[] = upcList.split(" ");
            //Remove beginning 2 zeros then add to set
            for (String upc : upcs) {
                upc = upc.substring(2, upc.length());
                retval.add(upc);
            }
        }
        return retval;
    }
}
