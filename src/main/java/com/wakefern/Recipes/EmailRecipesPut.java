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
 * Created by zacpuste on 8/30/16.
 */
@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class EmailRecipesPut extends BaseService {
    @PUT
    @Produces("application/*")
    /**
     * XML for put:
     <Recipe>
     <Recipient>zachary.puster@rosetta.com</Recipient>
     <Sender>test@mywebgrocer.com</Sender>
     <SenderName>The Sender</SenderName>
     <Subject>Recipe</Subject>
     <Subdomain>ShopRite</Subdomain>
     </Recipe>
     */
    @Path("/{chainId}/recipe/{recipeId}/email")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("recipeId") String recipeId,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        if(this.token.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }

        prepareResponse(chainId, recipeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, jsonBody);

        try {
            return this.createValidResponse(HTTPRequest.executePut("", secondMapping.getServicePath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String recipeId,String authToken, String jsonBody) throws Exception, IOException {
        if(this.token.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }

        prepareResponse(chainId, recipeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, jsonBody);

        return HTTPRequest.executePut("", secondMapping.getServicePath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());
    }

    public EmailRecipesPut(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String recipeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.recipe
                + ApplicationConstants.StringConstants.backSlash + recipeId + ApplicationConstants.StringConstants.email;
    }
}
