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
@Path(ApplicationConstants.Requests.Wakefern.ItemLocatorAuth)
public class WakefernAuth extends BaseService {
    @GET
    @Produces("text/plain")
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = "https://api.wakefern.com" + ApplicationConstants.Requests.Wakefern.ItemLocatorAuth;
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
        wkfn.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(path, wkfn));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public WakefernAuth(){
        this.serviceType = new MWGHeader();
    }
}
