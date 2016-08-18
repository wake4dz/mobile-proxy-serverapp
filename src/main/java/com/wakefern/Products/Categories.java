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
    public String getInfo(@PathParam("storeId") String storeId) throws Exception, IOException {


        JSONObject myJSONObj = new JSONObject();
       // this.request = request;
        GetTokens getTokens = new GetTokens();

//        //URL obj = new URL("https://shopritemobileapplication.mybluemix.net/api/product/v5/categories/store/C627119202/");
//        URL obj = new URL("https://api.shoprite.com/api/product/v5/categories/store/C627119202/");
//        URLConnection conn = obj.openConnection();
//        Map<String, List<String>> map = conn.getHeaderFields();
//
//        System.out.println("Printing All Response Header for URL: " + obj.toString() + "\n");
//        int i = 0;
//        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
//            myJSONObj.put("Header" + i, entry.getKey());
//            myJSONObj.put("Value" + i, entry.getValue());
//            i++;
//        }


        Enumeration headerNames = this.request.getHeaderNames();
        int i = 0;
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            myJSONObj.put("Header" + i, key);
            myJSONObj.put("Value" + i, value);
            i++;
        }

        this.token = getTokens.getAuthToken(this);
        //String storeId = getTokens.getStoreId(this);

        this.path = ApplicationConstants.Requests.Categories.CategoriesFromStoreId + storeId;


        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

//        //todo remove debugging
//        myJSONObj.put("headerToken", this.token);
//        myJSONObj.put("newPath", this.path);

//        myJSONObj.put("HTTPRequest: Categories", HTTPRequest.executePost("", secondMapping.getPath(), "", "",
//                secondMapping.getgenericHeader()));

        return myJSONObj.toString();

    }

    public Categories(){
        this.serviceType = new MWGHeader();
    }
}
