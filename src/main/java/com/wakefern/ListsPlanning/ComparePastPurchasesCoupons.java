package com.wakefern.ListsPlanning;

import com.wakefern.Coupons.Coupons;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(MWGApplicationConstants.Requests.Stores.ShoppingListUser)
public class ComparePastPurchasesCoupons extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/pastPurchasesCoupons")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @DefaultValue("9999") @QueryParam("take") String take,
                                    @DefaultValue("0") @QueryParam("skip") String skip,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken, @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        /**
         * AuthToken == Rosetta Token
         * AuthToken2 == User Token
         */

        Coupons coupons = new Coupons();
        String couponList = coupons.getInfo(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC, "", authToken);
        Set<String> couponIds = getCouponIds(couponList);

        try {
            GetPastPurchases getPastPurchases = new GetPastPurchases();
            String pastPurchases = getPastPurchases.getInfo(userId, storeId, "9999", "0", "", isMember, authToken, authToken2);
            return this.createValidResponse(getPurchaseIds(pastPurchases, couponIds, skip, take).toString());
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String take, String skip, String isMember, String authToken, String authToken2) throws Exception, IOException {
        Coupons coupons = new Coupons();
        String couponList = coupons.getInfo(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC, "", authToken);
        Set<String> couponIds = getCouponIds(couponList);

        GetPastPurchases getPastPurchases = new GetPastPurchases();
        String pastPurchases = getPastPurchases.getInfo(userId, storeId, "9999", "0", "", isMember, authToken, authToken2);
        return getPurchaseIds(pastPurchases, couponIds, skip, take).toString();
    }

    public ComparePastPurchasesCoupons() {
        this.requestHeader = new MWGHeader();
    }

    private JSONObject getPurchaseIds(String pastPurchases, Set<String> coupons, String skip, String take ){
        int matches = 0;
        int skips = 0;
        JSONObject retval = new JSONObject();
        try {//Assume multiple items in past purchases, exception if there is only one
            JSONObject jsonObject = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                    .getJSONObject(ApplicationConstants.Planning.ShoppingListItems);
            JSONArray jsonArray = jsonObject.getJSONArray(ApplicationConstants.Planning.ShoppingListItem);
            for (Object listItem : jsonArray) {
                if (skips < Integer.parseInt(skip)) {
                    skips++;
                    continue;
                }

                if (matches == Integer.parseInt(take)) {
                    return retval;
                }

                JSONObject currentListItem = (JSONObject) listItem;
                String sku = currentListItem.getString(ApplicationConstants.Planning.Sku);

                for (String couponId : coupons) {
                    if (sku.contains(couponId)) {
                        retval.append(ApplicationConstants.Planning.Matches, currentListItem);
                        matches++;
                    }
                }
            }
        } catch (Exception e){ //There is only one item in the past purchases list
            //System.out.print("Error:: " + e.getMessage());
            JSONObject singleObj = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                    .getJSONObject(ApplicationConstants.Planning.ShoppingListItems).getJSONObject(ApplicationConstants.Planning.ShoppingListItem);
            String singleSku = singleObj.getString(ApplicationConstants.Planning.Sku);
            for (String couponId : coupons) {
                if (singleSku.contains(couponId)) {
                    retval.append(ApplicationConstants.Planning.Matches, singleObj);
                    return retval;
                }
            }
        }

        //Should only be reached is matches < take
        return retval;
    }

    public Set<String> getCouponIds(String couponList){
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
