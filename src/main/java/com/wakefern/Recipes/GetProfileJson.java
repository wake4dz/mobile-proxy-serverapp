package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 10/5/16.
 */
@Path(ApplicationConstants.Requests.Recipes.UpdateProfile)
public class GetProfileJson extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{userId}/chainid/{chainId}/json")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("chainId") String chainId, @QueryParam("email") String email,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(userId, chainId, email, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        try {
            String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
            xml = xml.replaceAll("</User>", "<ChainId>" + chainId + "</ChainId></User>");
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            return this.createValidResponse(xmLtoJSONConverter.convert(xml));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String chainId, String email, String authToken) throws Exception, IOException {
        prepareResponse(userId, chainId, email, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        xml = xml.replaceAll("</User>", "<ChainId>" + chainId + "</ChainId></User>");
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xml);
    }

    public GetProfileJson() {
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String chainId, String email, String authToken){
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }

        this.path = ApplicationConstants.Requests.Recipes.UpdateProfile
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.chainid
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.emailParam + email;
    }
}
