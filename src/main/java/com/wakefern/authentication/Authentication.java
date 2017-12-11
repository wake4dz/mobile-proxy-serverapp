package com.wakefern.authentication;

import com.wakefern.global.*;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(MWGApplicationConstants.Requests.Authentication.authenticate)
public class Authentication extends BaseService {

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
        	String jsonResp = HTTPRequest.executePost("", mapping.getPath(), "", mapping.getGenericBody(), mapping.getgenericHeader(), 0);
            return this.createValidResponse(jsonResp);
        } catch (Exception e) {
        		logger.log(Level.SEVERE, "[getInfoResponse]::Exception getInfoResponse ", e);
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String jsonBody) throws Exception, IOException {
        prepareResponse();

        ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, jsonBody);
        
        String reqURL = mapping.getPath();
        String reqData = mapping.getGenericBody();
        Map<String, String> reqHead = mapping.getgenericHeader();
        
        return HTTPRequest.executePost("", reqURL, "", reqData, reqHead, 0);
    }

    public void prepareResponse() {
        this.path = MWGApplicationConstants.Requests.Authentication.authenticate;
        logger.log(Level.INFO, "[prepareResponse]::Authenticate path: ", this.path);
    }

    public Authentication(){
        this.serviceType = new MWGHeader();
    }
}
