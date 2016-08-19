package com.wakefern.Products;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Categories.CategoriesFromStoreId)
public class CategoriesWithSpecials extends BaseService {

    @GET
    @Produces("application/*")
    @Path("{storeId}/special")
    public String getInfo(@PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        //this.path = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + "/" + storeId + "/special";
        this.path = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.special;
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        myJSONObj.put(ApplicationConstants.RequestType.HTTPRequest + ApplicationConstants.RequestType.CategoriesWithSpecials,
                HTTPRequest.executeGet( secondMapping.getPath(), secondMapping.getgenericHeader()));

        return myJSONObj.toString();
    }

    public CategoriesWithSpecials(){
        this.serviceType = new MWGHeader();
    }
}


