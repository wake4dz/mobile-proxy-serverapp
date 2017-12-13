package com.wakefern.ListsPlanning;

import com.wakefern.Lists.GetItemsInList;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(MWGApplicationConstants.Requests.Stores.ShoppingListUser)
public class GetPastPurchases extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/pastPurchases")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId, @DefaultValue("100") @QueryParam("take") String take,
                                    @DefaultValue("0") @QueryParam("skip") String skip, @DefaultValue("") @QueryParam("category") String category,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,@DefaultValue("Days:0") @QueryParam("fq") String filter,
                                    @HeaderParam("Authorization") String authToken, @HeaderParam("Authorization2") String authToken2) throws Exception, IOException {
        this.token = authToken2; //User Auth
        ShoppingList shoppingList = new ShoppingList();

        GetUserLists getUserLists = new GetUserLists();
        String userListsJson = getUserLists.getInfo(userId, isMember, authToken);

        try {
            String pastPurchases = getPast(userListsJson);
            shoppingList.setId(pastPurchases);
            GetItemsInList getItemsInList = new GetItemsInList();
            String list = getItemsInList.getInfoFilter(storeId, userId, isMember, authToken2, "", pastPurchases, "9999", "0","", filter);
            return this.createValidResponse(paging(list, take, skip, category, shoppingList).toString());
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
    
    //fq=Days%3a30

    public String getInfo(String userId, String storeId, String take, String skip, String category, 
    		String isMember, String authToken, String authToken2) throws Exception, IOException {
        return this.getInfoFilter(userId,storeId,take,skip,category,isMember,authToken,authToken2,null);
    }
    
    public String getInfoFilter(String userId, String storeId, String take, String skip, String category, 
    		String isMember, String authToken, String authToken2,String filter) throws Exception, IOException {
        this.token = authToken2;
        ShoppingList shoppingList = new ShoppingList();

        GetUserLists getUserLists = new GetUserLists();
        String userListsJson = getUserLists.getInfo(userId, isMember, authToken);

        try {
            String pastPurchases = getPast(userListsJson);
            GetItemsInList getItemsInList = new GetItemsInList();
            String list = getItemsInList.getInfoFilter(storeId, userId, isMember, authToken2, "", pastPurchases, "9999", "0", "",filter);
            return paging(list, take, skip, category, shoppingList).toString();
        } catch (Exception e){
            ExceptionHandler exceptionHandler = new ExceptionHandler();
            return exceptionHandler.exceptionMessageJson(e);
        }
    }

    public GetPastPurchases() {
        this.serviceType = new MWGHeader();
    }

    private String getPast(String userListJson) throws Exception {
        Object jsonObject = new JSONObject(userListJson).getJSONObject(ApplicationConstants.Planning.ShoppingLists)
                .get(ApplicationConstants.Planning.ShoppingList);
        JSONArray jsonArray = (JSONArray) jsonObject;
        jsonArray.length();
        for(int i = 0;i<jsonArray.length();i++){
            JSONObject currentList = (JSONObject) jsonArray.get(i);
            if(currentList.get(ApplicationConstants.Planning.Name).equals(ApplicationConstants.Planning.MyPastPurchases)){
                return currentList.getString(ApplicationConstants.Planning.Id);
            }
        }

        //Should not be reached
        throw new Exception(ApplicationConstants.Planning.PastPurchasesError);
    }

    private JSONObject paging(String list, String take, String skip, String category, ShoppingList shoppingList){
        //Declare Object structure
        shoppingList.setName(ApplicationConstants.Planning.MyPastPurchases);

        //Declare paging items
        Integer skipCount = 0;
        Integer matches = 0;
        Integer skipInt = Integer.parseInt(skip);
        Integer takeInt = Integer.parseInt(take);

        //Declare containers
        JSONObject retval = new JSONObject();
        JSONObject shoppingListItem = new JSONObject();

        try {//Try is to catch one item in list case
            //Get shopping list array
            Object jsonObject = new JSONObject(list).get(ApplicationConstants.Planning.Items);
            JSONArray jsonArray = (JSONArray) jsonObject;

            for(int i = 0;i<jsonArray.length();i++){
                JSONObject currentListItem = (JSONObject) jsonArray.get(i);

                //Handle any necessary skipping
                if (skipCount < skipInt) {
                    skipCount++;
                    continue;
                }

                //Hit take limit, return matches
                if (matches == takeInt) {
                    JSONObject shoppingListItems = new JSONObject();
                    shoppingListItems.put(ApplicationConstants.Planning.Id, shoppingList.getId());
                    shoppingListItems.put(ApplicationConstants.Planning.Name, shoppingList.getName());
                    shoppingListItems.put(ApplicationConstants.Planning.ShoppingListItems, shoppingListItem);
                    retval.put(ApplicationConstants.Planning.ShoppingList, shoppingListItems);
                    return retval;
                }

                if(category.isEmpty()){// == "") {
                    shoppingListItem.append(ApplicationConstants.Planning.ShoppingListItem, currentListItem);
                    matches++;
                } else {
                    String categoryId = currentListItem.getString(ApplicationConstants.Planning.Category);
                    if(category.contains(categoryId)){
                        shoppingListItem.append(ApplicationConstants.Planning.ShoppingListItem, currentListItem);
                        matches++;
                    }
                }
            }
        } catch (Exception e) { //There is only one item in the list, just return it back
            JSONObject temp = new JSONObject(list);
            return temp;
        }
        //Should only be reached if matches < take
        JSONObject shoppingListItems = new JSONObject();
        shoppingListItems.put(ApplicationConstants.Planning.Id, shoppingList.getId());
        shoppingListItems.put(ApplicationConstants.Planning.Name, shoppingList.getName());
        shoppingListItems.put(ApplicationConstants.Planning.ShoppingListItems, shoppingListItem);
        retval.put(ApplicationConstants.Planning.ShoppingList, shoppingListItems);
        return retval;
    }

    public class ShoppingList{
        String id;
        String dateModified;
        String name;
        JSONObject rewards;
        JSONObject coupons;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDateModified() {
            return dateModified;
        }

        public void setDateModified(String dateModified) {
            this.dateModified = dateModified;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public JSONObject getRewards() {
            return rewards;
        }

        public void setRewards(JSONObject rewards) {
            this.rewards = rewards;
        }

        public JSONObject getCoupons() {
            return coupons;
        }

        public void setCoupons(JSONObject coupons) {
            this.coupons = coupons;
        }
    }
}