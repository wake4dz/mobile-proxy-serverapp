package com.wakefern.ListsPlanning;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.mywebgrocer.models.MWGHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(ApplicationConstants.Requests.Planning.ShoppingListUser)
public class GetPastPurchases extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/pastPurchases")
    public String getInfo(@PathParam("userId") String userId, @DefaultValue("100") @QueryParam("take") String take,
                          @DefaultValue("0") @QueryParam("skip") String skip, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        GetUserLists getUserLists = new GetUserLists();
        String userListsJson = getUserLists.getInfo(userId, this.token);

        try {
            String pastPurchases = getPast(userListsJson);
            GetListById getListById = new GetListById();
            String list = getListById.getInfo(userId, pastPurchases, this.token);
            return paging(list, take, skip).toString();
        } catch (Exception e){
            ExceptionHandler exceptionHandler = new ExceptionHandler();
            return exceptionHandler.ExceptionMessageJson(e);
        }
    }

    public GetPastPurchases() {
        this.serviceType = new MWGHeader();
    }

    private String getPast(String userListJson) throws Exception {
        Object jsonObject = new JSONObject(userListJson).getJSONObject(ApplicationConstants.Planning.ShoppingLists)
                .get(ApplicationConstants.Planning.ShoppingList);
        JSONArray jsonArray = (JSONArray) jsonObject;
        for(Object list: jsonArray){
            JSONObject currentList = (JSONObject) list;
            if(currentList.get(ApplicationConstants.Planning.Name).equals(ApplicationConstants.Planning.MyPastPurchases)){
                return currentList.getString(ApplicationConstants.Planning.Id);
            }
        }

        //Should not be reached
        throw new Exception(ApplicationConstants.Planning.PastPurchasesError);
    }

    private JSONObject paging(String list, String take, String skip){
        //Declare Object structure
        ShoppingList shoppingList = new ShoppingList();
        shoppingList = setNonItemData(shoppingList, list);

        //Declare paging items
        Integer skipCount = 0;
        Integer matches = 0;
        Integer skipInt = Integer.parseInt(skip);
        Integer takeInt = Integer.parseInt(take);

        //Declare containers
        JSONObject retval = new JSONObject();
        JSONObject shoppingListItem = new JSONObject();

        //Get shopping list array
        Object jsonObject = new JSONObject(list).getJSONObject(ApplicationConstants.Planning.ShoppingList)
                .getJSONObject(ApplicationConstants.Planning.ShoppingListItems).get(ApplicationConstants.Planning.ShoppingListItem);
        JSONArray jsonArray = (JSONArray) jsonObject;

        for(Object listItem: jsonArray) {
            JSONObject currentListItem = (JSONObject) listItem;

            //Handle any necessary skipping
            if (skipCount < skipInt) {
                skipCount++;
                continue;
            }

            //Hit take limit, return matches
            if (matches == takeInt) {
                JSONObject shoppingListItems = new JSONObject();
                //shoppingList.setShoppingListItems(shoppingListItems.append(ApplicationConstants.Planning.ShoppingListItems, shoppingListItem));
                shoppingListItems.put(ApplicationConstants.Planning.Id, shoppingList.getId());
                shoppingListItems.put(ApplicationConstants.Planning.DateModified, shoppingList.getDateModified());
                shoppingListItems.put(ApplicationConstants.Planning.Name, shoppingList.getName());
                shoppingListItems.put(ApplicationConstants.Planning.Rewards, shoppingList.getRewards());
                shoppingListItems.put(ApplicationConstants.Planning.Coupons, shoppingList.getCoupons());
                shoppingListItems.put(ApplicationConstants.Planning.ShoppingListItems, shoppingListItem);
                retval.put(ApplicationConstants.Planning.ShoppingList, shoppingListItems);
                return retval;
            }

            shoppingListItem.append(ApplicationConstants.Planning.ShoppingListItem, currentListItem);
            matches++;
        }
        //Should only be reached if matches < take
        JSONObject shoppingListItems = new JSONObject();
        shoppingListItems.put(ApplicationConstants.Planning.Id, shoppingList.getId());
        shoppingListItems.put(ApplicationConstants.Planning.DateModified, shoppingList.getDateModified());
        shoppingListItems.put(ApplicationConstants.Planning.Name, shoppingList.getName());
        shoppingListItems.put(ApplicationConstants.Planning.Rewards, shoppingList.getRewards());
        shoppingListItems.put(ApplicationConstants.Planning.Coupons, shoppingList.getCoupons());
        shoppingListItems.put(ApplicationConstants.Planning.ShoppingListItems, shoppingListItem);
        retval.put(ApplicationConstants.Planning.ShoppingList, shoppingListItems);
        return retval;
    }

    private ShoppingList setNonItemData(ShoppingList shoppingList, String list){
        JSONObject jsonObject = new JSONObject(list).getJSONObject(ApplicationConstants.Planning.ShoppingList);

        shoppingList.setId(jsonObject.getString(ApplicationConstants.Planning.Id));
        shoppingList.setDateModified(jsonObject.getString(ApplicationConstants.Planning.DateModified));
        shoppingList.setName(jsonObject.getString(ApplicationConstants.Planning.Name));
        shoppingList.setRewards(jsonObject.getJSONObject(ApplicationConstants.Planning.Rewards));
        shoppingList.setCoupons(jsonObject.getJSONObject(ApplicationConstants.Planning.Coupons));

        return shoppingList;
    }

    public class ShoppingList{
        String id;
        String dateModified;
        String name;
        JSONObject rewards;
        JSONObject coupons;
        JSONObject shoppingListItems;


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

        public JSONObject getShoppingListItems() {
            return shoppingListItems;
        }

        public void setShoppingListItems(JSONObject shoppingListItems) {
            this.shoppingListItems = shoppingListItems;
        }
    }
}