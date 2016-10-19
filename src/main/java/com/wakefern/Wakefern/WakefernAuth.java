package com.wakefern.Wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/18/16.
 */
@Path(ApplicationConstants.Requests.Wakefern.ItemLocatorJson)
public class WakefernAuth extends BaseService {
    @GET
    @Produces("application/*")
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public WakefernAuth(){
        this.serviceType = new MWGHeader();
    }
}
