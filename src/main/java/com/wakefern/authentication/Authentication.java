package com.wakefern.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class Authentication extends BaseService{
//
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public Response getInfoResponse(String jsonBody) throws Exception, IOException {
        prepareResponse();

        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);

        try {
            return this.createValidResponse(HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(),
                    mapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String jsonBody) throws Exception, IOException {
        prepareResponse();

        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);
        
        return HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(),
                mapping.getgenericHeader(), 0);
    }

    public void prepareResponse(){
        this.path = ApplicationConstants.Requests.Authentication.Authenticate;
        //this.path = ApplicationConstants.Requests.Authentication.Authenticatev1;
    }

    public Authentication(){
        this.serviceType = new MWGHeader();
    }

}
