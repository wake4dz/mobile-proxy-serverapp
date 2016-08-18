package com.wakefern.Products;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * Created by zacpuste on 8/18/16.
 */

@Path(ApplicationConstants.Requests.Categories.CategoriesFromStoreId)
public class Categories extends BaseService {

    private HttpServletRequest request;

    @GET
    @Produces("application/*")
    @Path("{storeId}")
    public String getInfo(@PathParam("storeId") String storeId, @HeaderParam("Authorization") String authToken) throws Exception, IOException {


        JSONObject myJSONObj = new JSONObject();
       // this.request = request;
        GetTokens getTokens = new GetTokens();



        this.token = getTokens.getAuthToken(this);
        //String storeId = getTokens.getStoreId(this);

        this.path = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + storeId;


        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

//        //todo remove debugging
        myJSONObj.put("headerToken", authToken);
        myJSONObj.put("storeId", storeId);

//        myJSONObj.put("HTTPRequest: Categories", HTTPRequest.executePost("", secondMapping.getPath(), "", "",
//                secondMapping.getgenericHeader()));

        return myJSONObj.toString();

    }

    public Categories(){
        this.serviceType = new MWGHeader();
    }
}
