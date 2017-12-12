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
	
	/**
	 * Constructor
	 */
    public Authentication() {
        this.serviceType = new MWGHeader();
        this.path = MWGApplicationConstants.Requests.Authentication.authenticate;
    }
	
    @POST
    @Consumes("application/json")
    @Produces("application/*")
    public Response getInfoResponse(String jsonBody) throws Exception, IOException {
    	
        try {
        		String jsonResp = makeRequest();
            return this.createValidResponse(jsonResp);
        } catch (Exception e) {
        		logger.log(Level.SEVERE, "[getInfoResponse]::Exception getInfoResponse ", e);
            return this.createErrorResponse(e);
        }
    }

    	/**
    	 * Triggers the Auth Request for the Session Token, but does not format the response.
    	 * For use internally.  This is not a REST endpoint.
    	 * 
    	 * @return
    	 * @throws Exception
    	 * @throws IOException
    	 */
    public String getInfo() throws Exception, IOException {
    		return makeRequest();
    }
    
    /**
     * Make the Auth Request to MWG for a Session Token.
     * 
     * @return
     * @throws Exception
     * @throws IOException
     */
    private String makeRequest() throws Exception, IOException {
        // The purpose of this request is to simply retrieve a valid Session Token from MWG.
        // As such, just send "{}" as POST data.  
    		// Any actual data sent, would just be ignored anyway.
        
    		ServiceMappings mapping = new ServiceMappings();
        mapping.setMapping(this, "{}");
        
        String reqURL = mapping.getPath();
        String reqData = mapping.getGenericBody();
        Map<String, String> reqHead = mapping.getgenericHeader();
        
        return HTTPRequest.executePost("", reqURL, "", reqData, reqHead, 0);
    }

// TODO: CAN THIS GO AWAY?
//
//    public void prepareResponse() {
//        this.path = MWGApplicationConstants.Requests.Authentication.authenticate;
//        logger.log(Level.INFO, "[prepareResponse]::Authenticate path: ", this.path);
//    }
}
