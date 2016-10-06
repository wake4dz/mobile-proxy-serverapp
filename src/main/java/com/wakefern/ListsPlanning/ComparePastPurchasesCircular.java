package com.wakefern.ListsPlanning;

import com.wakefern.Products.ProductById;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
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
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                          @QueryParam("take") String take, @QueryParam("skip") String skip,
                          @HeaderParam("Authorization") String authToken, @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        this.token = authToken;

        GetPastPurchases getPastPurchases = new GetPastPurchases();
        String pastPurchases = getPastPurchases.getInfo(userId, this.token);
        return getPurchaseIds(pastPurchases, storeId, authToken2, take, skip).toString();
    }

    public ComparePastPurchasesCircular() {
        this.serviceType = new MWGHeader();
    }

    private JSONObject getPurchaseIds(String pastPurchases, String storeId, String auth, String take, String skip)throws Exception{
        JSONObject retval = new JSONObject();
        SortedSet<Integer> ids = new TreeSet<Integer>();
        Object jsonObject = new JSONObject(pastPurchases).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                .getJSONObject(ApplicationConstants.Planning.ShoppingListItems).get(ApplicationConstants.Planning.ShoppingListItem);
        JSONArray jsonArray = (JSONArray) jsonObject;
        for(Object listItem: jsonArray){
            JSONObject currentListItem = (JSONObject) listItem;
            ids.add(currentListItem.getJSONObject(ApplicationConstants.Planning.Product).getInt(ApplicationConstants.Planning.Id));
        }

        Iterator it = ids.iterator();
        for(int j = 0; j < Integer.parseInt(skip); j++){
            it.next();
        }

        int matches = 0;
        while (it.hasNext()){
            if(matches >= Integer.parseInt(take)){
                return retval;
            }

            int currentid = (int) it.next();
            String id = String.valueOf(currentid);
            //todo remove test String id = "862795";

            ProductById productById = new ProductById();
            String json = productById.getInfo(id, storeId, "", "", "", auth);

            Object isSale = new JSONObject(json).get(ApplicationConstants.Planning.Sale);
            if (isSale != null){
                retval.append(ApplicationConstants.Planning.Matches, json);
                matches++;
            }
        }
        //Should only be reached is matches < take
        return retval;
    }
}
