package com.wakefern.Rewards;

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

@Path(ApplicationConstants.Requests.Rewards.Points)
public class GetPointsForPPC extends BaseService {

    @GET
    @Produces("application/*")
    @Path("/{ppc}")
    public Response getInfoResponse(@PathParam("ppc") String ppc,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
    	prepareResponse(ppc, authToken);
    	
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMappingWithURL(this, ApplicationConstants.Requests.Rewards.BasePointsURL);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
    

    public String getInfo(String ppc, String authToken) throws Exception, IOException {
    	prepareResponse(ppc, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public GetPointsForPPC(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String ppc, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Rewards.Points
                + ApplicationConstants.StringConstants.backSlash + ppc;
    }

}
