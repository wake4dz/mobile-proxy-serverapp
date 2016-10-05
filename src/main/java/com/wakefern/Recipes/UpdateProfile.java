package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zacpuste on 9/27/16.
 */
@Path(ApplicationConstants.Requests.Recipes.UpdateProfile)
public class UpdateProfile extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}")
    public String getInfo(@PathParam("userId") String userId, @QueryParam("email") String email, @QueryParam("chainId") String chainId, @QueryParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        GetProfile getProfile = new GetProfile();
        String jsonBody = getProfile.getInfo(userId, chainId, email, authToken);
        Pattern pattern = Pattern.compile("(<PreferredStore><Id>.*</Id></PreferredStore>)");
        Matcher matcher = pattern.matcher(jsonBody);
        jsonBody = matcher.replaceAll("<PreferredStore><Id>" + storeId + "</Id></PreferredStore>");
        jsonBody = jsonBody.replaceAll("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
        jsonBody = jsonBody.replaceAll("xmlns=\"http://schemas.datacontract.org/2004/07/MyWebGrocer.Account.Service.ServiceModels\"", "");

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