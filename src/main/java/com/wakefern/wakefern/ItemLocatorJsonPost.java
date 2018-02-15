package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zacpuste on 10/24/16.
 */
@Path(ApplicationConstants.Requests.Wakefern.ItemLocatorJson)
public class ItemLocatorJsonPost extends BaseService {

    @POST
    @Consumes("application/json")
    @Produces(MWGApplicationConstants.Headers.generic)
    /**
     * JSON Format:
     {
     "store":255,
     "upc":"4133112025,4133112402,4133112406"
     }
     */
    public Response getInfoResponse(@HeaderParam("Authentication") String authToken, String jsonBody) throws Exception, IOException {

        Map<String, String> wkfn = new HashMap<>();

        String path = "https://api.wakefern.com" + ApplicationConstants.Requests.Wakefern.ItemLocatorJson;
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "application/json");
        wkfn.put("Authentication", authToken);

        try {
            return this.createValidResponse(HTTPRequest.executePostJSON(path, jsonBody, wkfn, 0));
        } catch (Exception e) {
            return this.createErrorResponse(e);
        }
    }

    public ItemLocatorJsonPost(){
        this.requestHeader = new MWGHeader();
    }
}
