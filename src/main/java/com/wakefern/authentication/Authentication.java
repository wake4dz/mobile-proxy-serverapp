package com.wakefern.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.*;
import javax.ws.rs.*;
import java.io.IOException;

@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class Authentication extends BaseService{

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public String getInfo(String jsonBody) throws Exception, IOException {

        this.path = ApplicationConstants.Requests.Authentication.Authenticate;
        //this.path = ApplicationConstants.Requests.Authentication.Authenticatev1;
        
        //Authorization
        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);
        
        return HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(),
                mapping.getgenericHeader());
    }

    public Authentication(){
        this.serviceType = new MWGHeader();
    }

}
