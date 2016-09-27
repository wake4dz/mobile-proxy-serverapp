package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/27/16.
 */
@Path(ApplicationConstants.Requests.Recipes.UpdateProfile)
public class UpdateProfile extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}")
    public String getInfo(@PathParam("userId") String userId, @QueryParam("email") String email,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Recipes.UpdateProfile
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.emailParam + email;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, jsonBody);

        return HTTPRequest.executePut("", secondMapping.getServicePath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());
    }

    public UpdateProfile(){
        this.serviceType = new MWGHeader();
    }
}
