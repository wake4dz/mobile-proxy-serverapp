package com.wakefern.Circular;

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

@Path(ApplicationConstants.Requests.Circular.Categories)
public class RetrieveCircular extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/{circularId}")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,@PathParam("circularId") String circularId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        JSONObject myJSONObj = new JSONObject();

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Circular.Categories+ "/" + chainId + "/stores/" + storeId + "/circulars/" + circularId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        //todo remove debugging
        myJSONObj.put("headerToken", authToken);
        myJSONObj.put("storeId", storeId);

        myJSONObj.put("HTTPRequest: Retrieve a Circular", HTTPRequest.executeGet( secondMapping.getPath(),
                secondMapping.getgenericHeader()));

        return myJSONObj.toString();
    }

    public RetrieveCircular(){
        this.serviceType = new MWGHeader();
    }
}
