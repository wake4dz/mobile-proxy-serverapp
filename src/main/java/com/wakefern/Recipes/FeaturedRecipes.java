package com.wakefern.Recipes;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zacpuste on 8/29/16.
 */
@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class FeaturedRecipes extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/featured")
    public String getInfo(@PathParam("chainId") String chainId, @DefaultValue("")@QueryParam("q") String q,
            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.featured
                + ApplicationConstants.StringConstants.queryParam + q;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, null);

        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        String xml = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        String featuredReturnJson = xmLtoJSONConverter.convert(xml);

        JSONObject matchedObject = extractId(featuredReturnJson, chainId, authToken);
        return matchedObject.toString();
    }

    public FeaturedRecipes(){
        this.serviceType = new MWGHeader();
    }

    public JSONObject extractId(String featuredReturnJson, String chainId, String authToken){
        JSONObject jsonObject = new JSONObject(featuredReturnJson);
        JSONObject jsonObject1 = jsonObject.getJSONObject("FeaturedRecipeSummaries");
        JSONArray jsonArray = jsonObject1.getJSONArray("FeaturedRecipeSummary");
        JSONArray retval = new JSONArray();
        Set<Integer> ids = new HashSet<>();
        try {
            for (Object featuredRecipe : jsonArray) {
                JSONObject currentRecipe = (JSONObject) featuredRecipe;
                ids.add(currentRecipe.getInt("Id"));
            }
        }catch (Exception e){
            System.out.print("Error");
        }
        for(int id: ids){
            try {
                RecipeDetails recipeDetails = new RecipeDetails();
                String json = recipeDetails.getInfo(chainId, String.valueOf(id), authToken);
                retval.put(retval.length(), json);
            } catch (Exception e){
                System.out.print("Error in id set");
            }
        }
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("FeaturedRecipeSummary", retval);
        return jsonObject2;
    }
}

