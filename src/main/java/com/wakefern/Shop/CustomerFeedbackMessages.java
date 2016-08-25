package com.wakefern.Shop;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Shop.ShopStore)
public class CustomerFeedbackMessages extends BaseService {

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    @Path("/{storeId}/contact/messages")
    public String getInfo(@PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Shop.ShopStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.contact
                + ApplicationConstants.StringConstants.messages;

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody);

        System.out.print("\n\n\n\n Body:");
        System.out.print(mapping.getGenericBody());
        System.out.print("\n\n\n\n Header:");
        for(Map.Entry<String, String> entry: mapping.getgenericHeader().entrySet()){
            System.out.print(entry.getKey().toString() + ": " + entry.getValue().toString());
        }
        System.out.print("\r\r\r\r");

        return (HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), mapping.getgenericHeader()));
    }

    public CustomerFeedbackMessages(){
        this.serviceType = new MWGHeader();
    }

}

