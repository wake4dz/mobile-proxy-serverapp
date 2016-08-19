package com.wakefern.Products;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Categories.Subcategories)
public class SubcategoriesWithSpecials extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}/categories/special")
    public String getInfo(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.Subcategories + ApplicationConstants.StringConstants.backSlash
                + categoryId + ApplicationConstants.StringConstants.store + ApplicationConstants.StringConstants.backSlash + storeId
                + ApplicationConstants.StringConstants.categories + ApplicationConstants.StringConstants.special;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        myJSONObj.put(ApplicationConstants.RequestType.HTTPRequest + ApplicationConstants.RequestType.SubcategoriesWithSpecials,
                HTTPRequest.executeGet( secondMapping.getPath(), secondMapping.getgenericHeader()));

        return myJSONObj.toString();
    }

    public SubcategoriesWithSpecials(){
        this.serviceType = new MWGHeader();
    }
}
