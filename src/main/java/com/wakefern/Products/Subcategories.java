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

@Path(ApplicationConstants.Requests.Categories.Subcategories)
public class Subcategories extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}/categories")
    public String getInfo(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.Subcategories + "/" + categoryId + "/store/" + storeId + "/categories";

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        //todo remove debugging
        myJSONObj.put("headerToken", authToken);
        myJSONObj.put("storeId", storeId);

        myJSONObj.put("HTTPRequest: Categories with Specials", HTTPRequest.executeGet( secondMapping.getPath(),
                secondMapping.getgenericHeader()));

        return myJSONObj.toString();
    }

    public Subcategories(){
        this.serviceType = new MWGHeader();
    }
}
