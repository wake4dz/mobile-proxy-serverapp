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
import java.util.*;

/**
 * Created by zacpuste on 8/29/16.
 */
@Path(ApplicationConstants.Requests.Recipes.RecipeChain)
public class Categories extends BaseService {
    public JSONArray matchedObjects2;
    @GET
    @Produces("application/*")
    @Path("/{chainId}/categories")
    public String getInfo(@PathParam("chainId") String chainId, @DefaultValue("") @QueryParam("q") String q,
                          @DefaultValue("") @QueryParam("category") String category,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        
        //No query parameter case
        if(q == ""){
            this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.categories;

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setServiceMapping(this, null);

            String subCategoryXml = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader());
            XMLtoJSONConverter subCategoryJson = new XMLtoJSONConverter();
            return subCategoryJson.convert(subCategoryXml);
        }
        JSONObject matchedObjects = new JSONObject();
        matchedObjects2 = new JSONArray();
        Set<Integer> ids = new HashSet<>();
        q = q.toLowerCase();

        //No category parameter case
        if(category == "") {
            this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                    + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.categories
                    + ApplicationConstants.StringConstants.queryParam + q;

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setServiceMapping(this, null);

            String subCategoryXml = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader());
            XMLtoJSONConverter subCategoryJson = new XMLtoJSONConverter();
            ids = getSubcategories(subCategoryJson.convert(subCategoryXml));
            //All query parameter case
        } else {
            ids.add(Integer.parseInt(category));
        }

        for(Integer id: ids) {
            RecipesByCategory recipesByCategory = new RecipesByCategory();
            String json = recipesByCategory.getInfo(chainId, Integer.toString(id), authToken);
            matchedObjects2 = searchJSON(json, q, matchedObjects2);
        }

        int length = matchedObjects2.length();
        matchedObjects.put( "Total at root", length);
        matchedObjects.put( "Recipes", matchedObjects2);
        return matchedObjects.toString();
    }

    public Categories(){
        this.serviceType = new MWGHeader();
    }

    public JSONArray searchJSON(String jsonString, String query, JSONArray matchedObjects2) throws JSONException{
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONObject(ApplicationConstants.recipeSearch.recipes).getJSONArray(ApplicationConstants.recipeSearch.recipeSummary);
        try{
            for(Object recipe: jsonArray){
                JSONObject jsonRecipe = (JSONObject) recipe;
                String description = jsonRecipe.getString(ApplicationConstants.recipeSearch.description);
                String name = jsonRecipe.getString(ApplicationConstants.recipeSearch.name);

                if(description.toLowerCase().contains(query) || name.toLowerCase().contains(query) ){
                    matchedObjects2.put(matchedObjects2.length(), jsonRecipe);
                }
            }
        } catch (JSONException e){
            System.out.print("Failed to iterate Recipes");
        }
        return matchedObjects2;
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