package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/30/16.
 */
@Path(ApplicationConstants.Requests.Recipes.UpdateProfile)
public class GetProfile extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/chainid{chainId}")
    public String getInfo(@PathParam("userId") String userId, @PathParam("chainId") String chainId, @QueryParam("email") String email,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Recipes.UpdateProfile
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.chainid
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.emailParam + email;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        return xml.replaceAll("</User>", "<ChainId>" + chainId + "</ChainId></User>");
    }

    public GetProfile() {
        this.serviceType = new MWGHeader();
    }
}