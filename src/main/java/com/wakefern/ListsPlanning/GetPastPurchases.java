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
    public String getInfo(@PathParam("userId") String userId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        GetUserLists getUserLists = new GetUserLists();
        String userListsJson = getUserLists.getInfo(userId, this.token);

        try {
            String pastPurchases = getPast(userListsJson);
            GetListById getListById = new GetListById();
            return getListById.getInfo(userId, pastPurchases, this.token);
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
}