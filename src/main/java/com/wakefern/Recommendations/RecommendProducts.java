package com.wakefern.Recommendations;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

@Path(ApplicationConstants.Requests.Recommendations.ProductRecommendations)
public class RecommendProducts extends BaseService {

    @GET
    @Produces("application/*")
    @Path("/{userId}/sessid/{userAuth}")
    public Response getInfoResponse(@PathParam("userId") String userId,
    						@PathParam("userAuth") String userAuth,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
    	prepareResponse(userId, userAuth, authToken);
    	
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMappingWithURL(this, ApplicationConstants.Requests.Recommendations.BaseRecommendationsURL);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
    

    public String getInfo(String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public RecommendProducts(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String userAuth, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Recommendations.ProductRecommendations
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.sessid
                + ApplicationConstants.StringConstants.backSlash + userAuth;
    }

}
