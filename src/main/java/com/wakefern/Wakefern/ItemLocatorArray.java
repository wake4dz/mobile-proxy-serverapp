package com.wakefern.Wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zacpuste on 10/18/16.
 */

@Path(ApplicationConstants.Requests.Wakefern.ItemLocator)
public class ItemLocatorArray extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/{upc}")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("upc") String upc,
                                    @HeaderParam("Authentication") String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = "https://api.wakefern.com" + ApplicationConstants.Requests.Wakefern.ItemLocator
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.backSlash + upc;
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "application/json");
        wkfn.put("Authentication", authToken);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(path, wkfn, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo( String storeId, String upc, String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = "https://api.wakefern.com" + ApplicationConstants.Requests.Wakefern.ItemLocator
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.backSlash + upc;
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "application/json");
        wkfn.put("Authentication", authToken);

        try {
            return HTTPRequest.executeGet(path, wkfn, 0);
        } catch (Exception e){
            return null;
        }
    }

    public ItemLocatorArray(){
        this.serviceType = new MWGHeader();
    }
}