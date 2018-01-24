package com.wakefern.Recommendations;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.Wakefern.WakefernApplicationConstants;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

@Path(ApplicationConstants.Requests.Recommendations.ProductRecommendations)
public class RecommendProducts extends BaseService {

	private final static Logger logger = Logger.getLogger("RecommendProducts");
	
    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("/{userId}/sessid/{userAuth}")
    public Response getInfoResponse(
    		@PathParam("userId") String userId,
		@PathParam("userAuth") String sessionSecret,
        @HeaderParam("Authorization") String sessionToken
    ) throws Exception, IOException {
    	
    		// This request relies on a legacy endpoint maintained by Wakefern.
    		// Ignore the session token sent by the UI.
    		// Use the legacy Wakefern Auth Token instead.
    	
		this.requestHeader = new MWGHeader();
        this.requestToken  = WakefernApplicationConstants.Requests.authToken;
        this.requestPath   = ApplicationConstants.Requests.Recommendations.ProductRecommendations + "/" + userId + "/sessid" + "/" + sessionSecret;
                
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMappingWithURL(this, ApplicationConstants.Requests.Recommendations.BaseRecommendationsURL);
    		String secondMapPath = secondMapping.getPath();

        try {
        		String jsonResp = HTTPRequest.executeGet(secondMapPath, secondMapping.getgenericHeader(), 0);
            return this.createValidResponse(jsonResp);
        
        } catch (Exception e){
        		logger.log(Level.SEVERE, "[getInfoResponse]::Product Recommendation Exception!!!, Path: " + secondMapPath, e);
            return this.createErrorResponse(e);
        }
    }
}
