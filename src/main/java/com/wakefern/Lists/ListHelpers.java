package com.wakefern.Lists;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.ShoppingLists.ShoppingListsGet;
import com.wakefern.ShoppingLists.ShoppingListsPost;
import com.wakefern.global.ApplicationConstants;

/**
 * Created by brandyn.brosemer on 9/23/16.
 */
public final class ListHelpers {

	private final static Logger logger = Logger.getLogger("ListHelpers");
	private static StringBuilder sb = new StringBuilder();
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
        	logger.log(Level.SEVERE, "[getUserList]::EXCEPTION! "+e.getMessage()+ ", listName: "+listName);
            //System.out.println(ExceptionHandler.Exception(e));
        }
        return null;
    }

    static String getUsersLists(String chainId,String userId,String isMember, String authToken){
        ShoppingListsGet shopList = new ShoppingListsGet();
        String json = "";
        try {
            json = shopList.getInfo(chainId, userId, isMember, authToken);
            return json;
        } catch (Exception e) {
        	logger.log(Level.SEVERE, ListHelpers.errorMsgStr("[getUsersLists]::EXCEPTION! "+e.getMessage(), userId, "", authToken, "", "", json));
            return e.getMessage();
        }
    }

    static String getListId(String listName,String userId,String isMember, String authToken, String storeId) {
    	if(listName.isEmpty()){
            return "Error string empty";
        }
        listName = ApplicationConstants.Lists.getListType(listName);
        String userLists = ListHelpers.getUsersLists("FBFB139", userId, isMember, authToken);
        String listId = null;
        String errorMsg = "";
        try{
	        JSONArray userJson = new JSONArray(userLists);
	        
	        listId = ListHelpers.getUserList(userJson, ApplicationConstants.Lists.getListType(listName));
	        if(listId == null){
	        	ShoppingListsPost createList = new ShoppingListsPost();
	        	listName = "{'Name': '" + listName + "'}";
//	        	try {
					listId = createList.getInfo(userId, storeId, isMember,authToken, listName);
					JSONObject jObj = new JSONObject(listId);
					listId = jObj.getString("Id");
//				} catch (Exception e) {
//		        	errorMsg = ListHelpers.errorMsgStr("[getListId]::LISTID EXCEPTION! "+e.getMessage()+", listId: "+listId+", listName: "+listName
//		        			+", isMember: "+isMember, userId, storeId, authToken);
//		        	logger.log(Level.SEVERE, errorMsg);
//				}
	        }
        } catch(Exception e){
        	errorMsg = ListHelpers.errorMsgStr("[getListId]::EXCEPTION! "+e.getMessage()+", chainId: FBFB139, listName: "+listName
        			+", isMember: "+isMember, userId, storeId, authToken, "", "", userLists);
        	logger.log(Level.SEVERE, errorMsg);
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
    

	public static String errorMsgStr(String msg, String userId, String storeId, String authToken, String filter, String req, String response){
		return msg + ", userId: "+userId+", storeId, "+storeId+", token: "+authToken+", filter: "+filter+", req: "+req+", response: "+response;
	}
	
	public static String errorMsgStr(String msg, String userId, String storeId, String authToken){
		return msg + ", userId: "+userId+", storeId, "+storeId+", token: "+authToken;
	}

	public static String errorMsgStr(String msg, String userId, String storeId, String authToken, String listId, String listName){
		return msg + ", userId: "+userId+", storeId, "+storeId+", token: "+authToken+ ", listId: "+listId+", listName: "+listName;
	}
}
