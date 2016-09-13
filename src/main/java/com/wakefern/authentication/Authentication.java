package com.wakefern.authentication;

import com.ibm.json.java.JSONObject;
import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGBody;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.*;

import javax.ws.rs.*;

import java.io.IOException;
import java.util.Map;

@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class Authentication extends BaseService{

    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public String getInfo(String jsonBody) throws Exception, IOException {

        this.path = ApplicationConstants.Requests.Authentication.Authenticate;
        //this.path = ApplicationConstants.Requests.Authentication.Authenticatev1;

        JSONObject myJSONObj = new JSONObject();
        myJSONObj.put("message", jsonBody);
        
        //Authorization
        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);
        
        myJSONObj.put("HTTPRequest", HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(),
                mapping.getgenericHeader()));
        
        return myJSONObj.toString();
    }

    public Authentication(){
        this.serviceType = new MWGHeader();
    }

}
