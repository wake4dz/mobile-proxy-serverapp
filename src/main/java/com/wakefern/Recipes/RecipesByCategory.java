package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/29/16.
 */
@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class RecipesByCategory extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/category/{subCategoryId}")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("subCategoryId") String subCategoryId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.category
                + ApplicationConstants.StringConstants.backSlash + subCategoryId;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, null);

        return HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
    }

    public RecipesByCategory(){
        this.serviceType = new MWGHeader();
    }
}

