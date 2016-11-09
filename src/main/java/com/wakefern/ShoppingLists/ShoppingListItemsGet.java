package com.wakefern.ShoppingLists;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 9/9/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slItemsUser)
public class ShoppingListItemsGet extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/list/{listId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId , @PathParam("listId") String listId,
                                    @QueryParam("take") String take, @QueryParam("skip") String skip,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {

        String path = prepareResponse(userId, storeId, listId, take, skip, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return this.createValidResponse(HTTPRequest.executeGetJSON(path, secondMapping.getgenericHeader(), 0));
    }

    public String getInfo(String userId, String storeId , String listId, String take, String skip, String isMember, String authToken) throws Exception, IOException {
        return this.getInfoFilter(userId, storeId, listId, take, skip, isMember, authToken, null);
    }
    
    public String getInfoFilter(String userId, String storeId , String listId, String take, 
    		String skip, String isMember, String authToken,String filter) throws Exception, IOException {
    	String path = prepareResponse(userId, storeId, listId, take, skip, isMember, authToken);

    	if(!filter.isEmpty()){
    		path += filter;
    		System.out.println("Filter With Path :: " + path);
    	}

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGetJSON(path, secondMapping.getgenericHeader(), 0);
    }

    public String prepareResponse(String userId, String storeId, String listId, String take, String skip, String isMember, String authToken){
        this.token = authToken;

        this.path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.take
                + take + ApplicationConstants.StringConstants.skip + skip;
        if(!isMember.isEmpty()){
            this.path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slItemsUser
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                    + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.take
                    + take + ApplicationConstants.StringConstants.skip + skip + ApplicationConstants.StringConstants.isMemberAmp;
        }
        return path;
    }

    public ShoppingListItemsGet(){
        this.serviceType = new MWGHeader();
    }
}

