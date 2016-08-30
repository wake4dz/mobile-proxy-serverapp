package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
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
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("recipeId") String recipeId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {

        this.token = authToken;
        this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.recipe
                + ApplicationConstants.StringConstants.backSlash + recipeId + ApplicationConstants.StringConstants.email;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, jsonBody);

        return HTTPRequest.executePut("", secondMapping.getServicePath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());

    }
    public EmailRecipesPut(){
        this.serviceType = new MWGHeader();
    }
}
