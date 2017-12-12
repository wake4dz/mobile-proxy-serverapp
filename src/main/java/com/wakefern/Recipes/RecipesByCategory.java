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
 * Created by zacpuste on 8/29/16.
 */
@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class RecipesByCategory extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/category/{subCategoryId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("subCategoryId") String subCategoryId,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, subCategoryId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        try {
            String response = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            return this.createValidResponse(xmLtoJSONConverter.convert(response));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String subCategoryId, String authToken) throws Exception, IOException {
        prepareResponse(chainId, subCategoryId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String response = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(response);
    }

    public RecipesByCategory(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String subCategoryId, String authToken){
    		
    		this.token = ApplicationConstants.Requests.Tokens.planningToken;

        this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.category
                + ApplicationConstants.StringConstants.backSlash + subCategoryId;
    }
}
