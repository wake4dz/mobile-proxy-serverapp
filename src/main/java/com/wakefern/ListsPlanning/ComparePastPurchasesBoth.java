package com.wakefern.ListsPlanning;

import com.wakefern.Coupons.Coupons;
import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by zacpuste on 10/14/16.
 */
@Path(ApplicationConstants.Requests.Planning.ShoppingListUser)
public class ComparePastPurchasesBoth extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/pastPurchasesBoth")
    public String getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("9999") @QueryParam("take") String take, @DefaultValue("0") @QueryParam("skip") String skip,
                                    @HeaderParam("Authorization") String authToken, @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        this.token = authToken;

//        try {
            System.out.print("A");
            ComparePastPurchasesCircular comparePastPurchasesCircular = new ComparePastPurchasesCircular();
            String onSale = comparePastPurchasesCircular.getInfo(userId, storeId, take, skip, "", authToken, authToken2);
            System.out.print("B");
            Coupons coupons = new Coupons();
            String couponList = coupons.getInfo(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All, "");
            System.out.print("C");
            ComparePastPurchasesCoupons comparePastPurchasesCoupons = new ComparePastPurchasesCoupons();
            Set<String> ids = comparePastPurchasesCoupons.getCouponIds(couponList);
//            return this.createValidResponse(getPurchaseIds(onSale, ids, skip, take).toString());
            return getPurchaseIds(onSale, ids, skip, take).toString();
//        } catch (Exception e) {
//            return this.createErrorResponse(e);
//        }
    }

//    public String getInfo(String userId, String storeId, String take, String skip, String authToken, String authToken2) throws Exception, IOException {
//
//    }

    public ComparePastPurchasesBoth() {
        this.serviceType = new MWGHeader();
    }

    public JSONObject getPurchaseIds(String pastPurchases, Set<String> coupons, String skip, String take ){
        int matches = 0;
        int skips = 0;
        JSONObject retval = new JSONObject();
        try {//Assume multiple items in compare circular, exception if there is only one

            Object obj = pastPurchases;
            JSONArray jsonArray = new JSONArray(obj);
//            Object jsonObject = new JSONObject(pastPurchases).get("Matches");
//            JSONArray jsonArray = new JSONArray(jsonObject);
            System.out.print("D:: ");
            for (Object listItem : jsonArray) {
                if (skips < Integer.parseInt(skip)) {
                    skips++;
                    continue;
                }

                if (matches == Integer.parseInt(take)) {
                    return retval;
                }

                System.out.print("F");
                JSONObject currentListItem = (JSONObject) listItem;
                String sku = currentListItem.getString(ApplicationConstants.Planning.SKU);
                System.out.print("G");

                for (String couponId : coupons) {
                    if (sku.contains(couponId)) {
                        retval.append(ApplicationConstants.Planning.Matches, currentListItem);
                        matches++;
                    }
                }
            }
        } catch (Exception e){ //There is only one item in the compare circular list
            System.out.print("E::" + e.getMessage());
            Object toConvert = new JSONObject(pastPurchases).get("Matches");
            JSONObject singleObj = (JSONObject) toConvert;
            String singleSku = singleObj.getString(ApplicationConstants.Planning.SKU);
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
}