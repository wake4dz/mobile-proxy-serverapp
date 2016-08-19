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
        this.path = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + "/" + storeId + "/special";

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        //todo remove debugging
        myJSONObj.put("headerToken", authToken);
        myJSONObj.put("storeId", storeId);

        myJSONObj.put("HTTPRequest: Categories with Specials", HTTPRequest.executeGet( secondMapping.getPath(),
                secondMapping.getgenericHeader()));

        return myJSONObj.toString();
    }

    public CategoriesWithSpecials(){
        this.serviceType = new MWGHeader();
    }
}


