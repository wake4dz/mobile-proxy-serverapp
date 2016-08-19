package com.wakefern.Circular;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularCategory extends BaseService{
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/categories/{categoryId}")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("categoryId") String categoryId,
                          @HeaderParam("Authorization") String authToken)throws Exception, IOException {

        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.categories + ApplicationConstants.StringConstants.backSlash
                + categoryId;

//        this.path = ApplicationConstants.Requests.Circular.Categories + "/" + chainId + "/stores/" + storeId + "/categories/" + categoryId;



        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        myJSONObj.put("HTTPRequest: Circular Categories", HTTPRequest.executeGet( secondMapping.getPath(),
                secondMapping.getgenericHeader()));

        return myJSONObj.toString();
    }

    public CircularCategory(){
        this.serviceType = new MWGHeader();
    }
}
