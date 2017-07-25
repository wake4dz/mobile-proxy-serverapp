package com.wakefern.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(ApplicationConstants.Requests.Authentication.Authenticate)
public class Authentication extends BaseService{

	private final static Logger logger = Logger.getLogger("Authentication");
	
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public Response getInfoResponse(String jsonBody) throws Exception, IOException {
        prepareResponse();
        logger.log(Level.INFO, "[getInfoResponse]::Authenticating user..");
        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);

        try {
        	String jsonResp = HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(),
                    mapping.getgenericHeader(), 0);
            return this.createValidResponse(jsonResp);
        } catch (Exception e){
        	logger.log(Level.SEVERE, "[getInfoResponse]::Exception getInfoResponse ", e);
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String jsonBody) throws Exception, IOException {
        logger.log(Level.INFO, "[getInfo]::Authenticating user..");
        prepareResponse();

        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);
        
        return HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(),
                mapping.getgenericHeader(), 0);
    }

    public void prepareResponse(){
        this.path = ApplicationConstants.Requests.Authentication.Authenticate;
        logger.log(Level.INFO, "[prepareResponse]::Authenticate path: ", this.path);
        //this.path = ApplicationConstants.Requests.Authentication.Authenticatev1;
    }

    public Authentication(){
        this.serviceType = new MWGHeader();
    }

}
