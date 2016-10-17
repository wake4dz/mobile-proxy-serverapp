package com.wakefern.ListsPlanning;

import com.wakefern.Products.ProductBySku;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(ApplicationConstants.Requests.Planning.ShoppingListUser)
public class ComparePastPurchasesCircular extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/pastPurchasesCircular")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("9999")@QueryParam("take") String take, @DefaultValue("0") @QueryParam("skip") String skip,
                                    @DefaultValue("") @QueryParam("category") String category, @HeaderParam("Authorization") String authToken,
                                    @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        this.token = authToken;

        try {
            GetPastPurchases getPastPurchases = new GetPastPurchases();
            String pastPurchases = getPastPurchases.getInfo(userId, "9999", "0", category, this.token);
            String retval = "";
            try {
                retval = getPurchaseIds(pastPurchases, storeId, authToken2, take, skip).toString();
            } catch (Exception ex){
                return this.createValidResponse(ex.getMessage());
            }
            return this.createValidResponse(retval);
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String take, String skip, String category, String authToken, String authToken2) throws Exception, IOException {
        this.token = authToken;

        GetPastPurchases getPastPurchases = new GetPastPurchases();
        String pastPurchases = getPastPurchases.getInfo(userId, "9999", "0", category, this.token);
        //Need to pad String response type to allow ComparePastPurchasesBoth to access the array
        JSONObject matches = getPurchaseIds(pastPurchases, storeId, authToken2, take, skip);
        JSONObject retval = new JSONObject();
        retval.put(ApplicationConstants.Planning.Circular, matches);
        return retval.toString();
    }

    public ComparePastPurchasesCircular() {
        this.serviceType = new MWGHeader();
    }

    private JSONObject getPurchaseIds(String pastPurchases, String storeId, String auth, String take, String skip)throws Exception {
        JSONObject retval = new JSONObject();
        SortedSet<String> ids = new TreeSet<String>();
        try {//Assume multiple items in past purchases, exception if there is only one
            Object jsonObject = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                    .getJSONObject(ApplicationConstants.Planning.ShoppingListItems).get(ApplicationConstants.Planning.ShoppingListItem);
            JSONArray jsonArray = (JSONArray) jsonObject;
            for(Object listItem: jsonArray){ //get all of the ids from past purchases
                JSONObject currentListItem = (JSONObject) listItem;
                String sku = currentListItem.getJSONObject(ApplicationConstants.Planning.Product).getString(ApplicationConstants.Planning.SKU);
                ids.add(sku);
            }
        } catch (Exception e){//There is only one item in the past purchases list
            try {
                String singleSku = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                        .getJSONObject(ApplicationConstants.Planning.ShoppingListItems).getJSONObject(ApplicationConstants.Planning.ShoppingListItem)
                        .getJSONObject(ApplicationConstants.Planning.Product).getString(ApplicationConstants.Planning.SKU);
                ProductBySku productBySku = new ProductBySku();
                String json = productBySku.getInfo(storeId, singleSku, auth);
                JSONObject jsonObject = new JSONObject(json);
                retval.put(ApplicationConstants.Planning.Matches, jsonObject);
                return retval;
            } catch (Exception ex){//ShoppingListItem doesn't exist, all results have been filtered out
                ex = new Exception(ApplicationConstants.Planning.CategoryErrorMessage);
                ExceptionHandler exceptionHandler = new ExceptionHandler();
                throw exceptionHandler.Exception(ex);
            }
        }

        //Page over any skips
        Iterator it = ids.iterator();

        for(int j = 0; j < Integer.parseInt(skip); j++){
            it.next();
        }

        int matches = 0;
        try {
            while (it.hasNext()) {
                try {
                    if (matches >= Integer.parseInt(take)) {
                        return retval;
                    }

                    //int currentid = (int) it.next();
                    String sku = it.next().toString();

                    ProductBySku productBySku = new ProductBySku();
                    String json = productBySku.getInfo(storeId, sku, auth);

                    //Verify that a new item is on sale
                    Object isSale = new JSONObject(json).get(ApplicationConstants.Planning.Sale);
                    if (isSale != null) {
                        JSONObject jsonObject = new JSONObject(json);
                        retval.append(ApplicationConstants.Planning.Matches, jsonObject);
                        matches++;
                    }
                } catch (Exception e){
                    System.out.print("Invalid SKU " + e.getMessage());
                    continue;
                }
            }
        } catch (Exception e) {
            throw e;
        }
        //Should only be reached is matches < take
        return retval;
    }

    String json;

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}