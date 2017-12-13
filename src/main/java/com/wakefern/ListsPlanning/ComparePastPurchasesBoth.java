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
import java.util.Set;

/**
 * Created by zacpuste on 10/14/16.
 */
@Path(MWGApplicationConstants.Requests.Stores.ShoppingListUser)
public class ComparePastPurchasesBoth extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/pastPurchasesBoth")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @DefaultValue("9999") @QueryParam("take") String take, @DefaultValue("0") @QueryParam("skip") String skip,
                                    @HeaderParam("Authorization") String authToken, @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        /**
         * AuthToken == Rosetta Token
         * AuthToken2 == User Token//
         */
        
        try{
            ComparePastPurchasesCircular comparePastPurchasesCircular = new ComparePastPurchasesCircular();
            String onSale = comparePastPurchasesCircular.getInfoFilter(userId, storeId, take, skip, "", isMember, authToken, authToken2,"Days:60");

            Coupons coupons = new Coupons();
            String couponList = coupons.getInfo(WakefernApplicationConstants.Requests.Coupons.Metadata.PPC_All, "", authToken);

            ComparePastPurchasesCoupons comparePastPurchasesCoupons = new ComparePastPurchasesCoupons();
            Set<String> ids = comparePastPurchasesCoupons.getCouponIds(couponList);
            return this.createValidResponse(getPurchaseIds(onSale, ids, skip, take).toString());
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }

    public ComparePastPurchasesBoth() {
        this.serviceType = new MWGHeader();
    }

    public JSONObject getPurchaseIds(String pastPurchases, Set<String> coupons, String skip, String take ){
        int matches = 0;
        int skips = 0;
        JSONObject retval = new JSONObject();
        try {//Assume multiple items in compare circular, exception if there is only one
            JSONObject jsonObject = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.Circular);
            JSONArray jsonArray = jsonObject.getJSONArray(ApplicationConstants.Planning.Matches);
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
        } catch (Exception e){ //There is only one item in the compare circular list
            Object toConvert = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.Circular).get(ApplicationConstants.Planning.Matches);
            JSONObject singleObj = (JSONObject) toConvert;
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
}