package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
/**
 * Created by zacpuste on 10/5/16.
 */

@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class RecipeDetailsXml extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/recipe/{recipeId}/xml")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("recipeId") String recipeId,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, recipeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, null);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String recipeId, String authToken) throws Exception, IOException {
        prepareResponse(chainId, recipeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, null);

        return HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
    }

    public RecipeDetailsXml(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String recipeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.recipe
                + ApplicationConstants.StringConstants.backSlash + recipeId;
    }
}

