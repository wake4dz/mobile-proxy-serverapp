package com.wakefern.ShoppingLists;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zacpuste on 9/1/16.
 */

@Path(ApplicationConstants.Requests.ShoppingLists.slUser)
public class DuplicateList extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/duplicate")
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                          @QueryParam("id1") String id1, @QueryParam("name") String name,
                          @DefaultValue("")@QueryParam("isMember") String isMember,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.ShoppingLists.slUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.duplicate
                + ApplicationConstants.StringConstants.id1 + id1 + ApplicationConstants.StringConstants.name + name;
        if(!isMember.isEmpty()){
            this.requestPath += ApplicationConstants.StringConstants.isMemberAmp;
        }

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        //Get items from first list - max and min hardcoded for take and skip respectively
        ShoppingListItemsGet shoppingListItemsGet = new ShoppingListItemsGet();
        String firstList = shoppingListItemsGet.getInfo(userId, storeId, id1, "9999", "0", "", isMember, authToken);

        //Create second List
        ShoppingListsPost shoppingListsPost = new ShoppingListsPost();
        String jsonToPass = "{\"Name\": \"" + URLDecoder.decode(name , "UTF-8") + "\"}";
        String newListJson;
        try {
            newListJson = shoppingListsPost.getInfo(userId, storeId, isMember, authToken, jsonToPass);
            if(newListJson == null){
                IOException e = new IOException("Duplicate List Name");
                throw e;
            }
        } catch (Exception e){
            JSONObject jsonObject = new JSONObject("{ Error: " + ExceptionHandler.ExceptionMessage(e) + "}");
            return jsonObject.toString();
        }

        String newListId = newListId(newListJson);
        JSONObject retval = new JSONObject("{ Id: " + newListId + "}");

        //Duplicate first list into the second list
        ShoppingListItemsPost shoppingListItemsPost = new ShoppingListItemsPost();
        return retval + shoppingListItemsPost.getInfo(userId, storeId, newListId, isMember, authToken, firstList);
    }
    public DuplicateList(){
        this.requestHeader = new MWGHeader();
    }

    public String newListId(String newList){
        JSONObject jsonObject = new JSONObject(newList);
        return jsonObject.getString(ApplicationConstants.StringConstants.Id);
    }
}