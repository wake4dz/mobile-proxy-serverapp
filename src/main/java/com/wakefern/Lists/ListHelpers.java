package com.wakefern.Lists;

import com.wakefern.Lists.Models.GenericListItem;
import com.wakefern.ShoppingLists.ShoppingListsGet;
import com.wakefern.ShoppingLists.ShoppingListsPost;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import org.codehaus.jackson.map.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
public final class ListHelpers {

    static String getUserList(JSONArray jsonArray, String listName){
        try{
            for (int i = 0, size = jsonArray.length(); i < size; i++)
            {
                JSONObject userListObj = jsonArray.getJSONObject(i);
                if(userListObj.getString("Name").equalsIgnoreCase(listName)){
                    return userListObj.getString("Id");
                }
            }
        }catch(Exception e){
            System.out.println(ExceptionHandler.Exception(e));
        }
        return null;
    }

    static String getUsersLists(String chainId,String userId,String authToken){
        ShoppingListsGet shopList = new ShoppingListsGet();
        try {
            String json = shopList.getInfo(chainId, userId, authToken);
            return json;
        } catch (Exception e) {
            ExceptionHandler.Exception(e);
            return e.getMessage();
        }
    }

    static String getListId(String listName,String userId,String authToken, String storeId) throws IOException {
    	if(listName.isEmpty()){
            return "Error string empty";
        }
        
        String userLists = ListHelpers.getUsersLists("FBFB139", userId, authToken);
        JSONArray userJson = new JSONArray(userLists);
        
        String listId = ListHelpers.getUserList(userJson, ApplicationConstants.Lists.getListType(listName));
        if(listId == null){
        	ShoppingListsPost createList = new ShoppingListsPost();
        	listName = "{'Name': '" + listName + "'}";
        	try {
				listId = createList.getInfo(userId, storeId, authToken, listName);
				JSONObject jObj = new JSONObject(listId);
				listId = jObj.getString("Id");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        return listId;
    }

    static String getItemId(String jsonRespone,String itemKey){
        JSONObject userList = new JSONObject(jsonRespone);
        JSONArray items = userList.getJSONArray("Items");

        for (int i = 0, size = items.length(); i < size; i++)
        {
            JSONObject userListObj = items.getJSONObject(i);
            if(userListObj.getString("ItemKey").equalsIgnoreCase(itemKey)){
                return userListObj.getString("Id");
            }
        }
        return null;
    }

}
