package com.wakefern.Recipes;

import com.wakefern.Caching.WakefernCacheControl;
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
import java.util.*;

/**
 * Created by zacpuste on 8/29/16.
 */
@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class Categories extends BaseService {
    public JSONObject matchedObjects;
    @GET
    @Produces("application/*")
    @Path("/{chainId}/categories")
    public String getInfo(@PathParam("chainId") String chainId, @QueryParam("q") String q,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        matchedObjects = new JSONObject();

        if(q == null || q == "") {
            this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.categories;
        } else {
            this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.categories
                    + ApplicationConstants.StringConstants.queryParam + q;
        }
        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, null);

        String subCategoryXml = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        XMLtoJSONConverter subCategoryJson = new XMLtoJSONConverter();
        Set<Integer> ids = getSubcategories(subCategoryJson.convert(subCategoryXml));

        q = "BBQ"; //todo remove hardcoding of query
        for(Integer id: ids) {
            System.out.print(id);
            RecipesByCategory recipesByCategory = new RecipesByCategory();
            String extractedXml = recipesByCategory.getInfo(chainId, Integer.toString(id), authToken);
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();

            String json = xmLtoJSONConverter.convert(extractedXml);
            matchedObjects = searchJSON(json, q, matchedObjects);
        }

        System.out.print("Before toString");
        return matchedObjects.toString();
    }

    public Categories(){
        this.serviceType = new MWGHeader();
    }

    public JSONObject searchJSON(String jsonString, String query, JSONObject matchedObjects) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONObject(ApplicationConstants.recipeSearch.recipes).getJSONArray(ApplicationConstants.recipeSearch.recipeSummary);
        try{
            for(Object recipe: jsonArray){
                JSONObject jsonRecipe = (JSONObject) recipe;
                String description = jsonRecipe.getString(ApplicationConstants.recipeSearch.description);
                String name = jsonRecipe.getString(ApplicationConstants.recipeSearch.name);

                System.out.print(name + " " + description);

                if(description.contains(query) || name.contains(query) ){
                    System.out.print("Match");
                    matchedObjects.append(name, jsonRecipe);
                }
            }
        } catch (JSONException e){
            System.out.print("Failed to iterate Recipes");
        }
        return matchedObjects;
    }

    public Set<Integer> getSubcategories(String jsonString){
        Set<Integer> retval = new HashSet<>();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONObject(ApplicationConstants.recipeSearch.categories).getJSONArray(ApplicationConstants.recipeSearch.category);
        try {
            for (Object category : jsonArray) {
                JSONObject subcategory = (JSONObject) category;
                JSONArray idCategories = subcategory.getJSONObject(ApplicationConstants.recipeSearch.subCategories).getJSONArray(ApplicationConstants.recipeSearch.category);
                for (Object ids : idCategories) {
                    JSONObject idObject = (JSONObject) ids;
                    Integer id = idObject.getInt(ApplicationConstants.recipeSearch.id);
                    retval.add(id);
                }
            }
        } catch (JSONException e) {
            System.out.print("Failed to iterate Categories");
        }
        System.out.print(retval);
        return retval;
    }
}