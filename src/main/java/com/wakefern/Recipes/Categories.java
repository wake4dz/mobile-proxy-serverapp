package com.wakefern.Recipes;

import com.wakefern.Lists.GetItemsInList;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URI;
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
    public Response getInfoResponse(@PathParam("chainId") String chainId, @DefaultValue("") @QueryParam("q") String q, @DefaultValue("") @QueryParam("listName") String listName,
                                    @DefaultValue("") @QueryParam("storeId") String storeId,
                                    @DefaultValue("") @QueryParam("authUser") String authUser,
                                    @DefaultValue("") @QueryParam("userId") String userId,
                                    @DefaultValue("") @QueryParam("category") String category,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        Set<Integer> ids = new HashSet<>();
        JSONObject matchedObjects = new JSONObject();

        // this is being used for recipes favorites 
        if(listName.isEmpty()){
            //No query parameter case
            if(q == ""){
                try {
                    this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                            + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.categories;

                    ServiceMappings secondMapping = new ServiceMappings();
                    secondMapping.setServiceMappingv1(this, null);

                    String subCategoryXml = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader());
                    XMLtoJSONConverter subCategoryJson = new XMLtoJSONConverter();
                    return this.createValidResponse(subCategoryJson.convert(subCategoryXml));
                } catch (Exception e){
                    return this.createErrorResponse(e);
                }
            }
            matchedObjects2 = new JSONArray();
            q = q.toLowerCase();

            //No category parameter case
            if(category == "") {
                this.path = ApplicationConstants.Requests.Recipes.RecipeChain
                        + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.categories
                        + ApplicationConstants.StringConstants.queryParam + q;

                ServiceMappings secondMapping = new ServiceMappings();
                secondMapping.setServiceMappingv1(this, null);

                String subCategoryXml = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader());
                XMLtoJSONConverter subCategoryJson = new XMLtoJSONConverter();
                String parsedJson = subCategoryJson.convert(subCategoryXml);

                //Make sure there are categories to search, if not return error json back
                try{
                    JSONObject jsonObject = new JSONObject(parsedJson);
                    JSONArray jsonArray = jsonObject.getJSONObject(ApplicationConstants.recipeSearch.categories).getJSONArray(ApplicationConstants.recipeSearch.category);
                    ids = getSubcategories(jsonArray);
                } catch (Exception e) {
                    JSONArray emptyArray = new JSONArray(); //Used to format like a sucessful response, but just with an empty array
                    matchedObjects.put(ApplicationConstants.recipeSearch.RecipeCategories, emptyArray);
                    matchedObjects.put(ApplicationConstants.recipeSearch.totalRecipes, 0);
                    return this.createValidResponse(matchedObjects.toString());
                }

                //All query parameter case
            } else {
                ids.add(Integer.parseInt(category));
            }
        }
        
        if(!listName.isEmpty()&&!storeId.isEmpty()&&!userId.isEmpty()&&!authToken.isEmpty()){
            GetItemsInList getItemsInList = new GetItemsInList();
            String list = getItemsInList.getInfo(storeId, userId, authToken, listName, "", "9999", "0", "");
            JSONObject listItems = new JSONObject(list);
            JSONArray items = listItems.getJSONArray(ApplicationConstants.recipeSearch.items);
            JSONArray response = new JSONArray();
            String responseString = "";
            for (int i = 0, size = items.length(); i < size; i++){
            	JSONObject item = (JSONObject) items.get(i);
            	String note = item.getString(ApplicationConstants.recipeSearch.Note);
            	URI uri = new URI(note);
            	String[] segments = uri.getPath().split("/");
            	String idStr = segments[segments.length - 1];
            	
            	RecipeDetails recipeDetails = new RecipeDetails();
            	String details = recipeDetails.getInfo(chainId, idStr, "");
            	if(!details.isEmpty()){
            		JSONObject newDetails = new JSONObject(details);
            		response.put(newDetails);
            	}
            }

            return this.createValidResponse(response.toString());
        }else if(ids.isEmpty()){
        	return this.createErrorResponse(new Exception(ApplicationConstants.recipeSearch.ListError + this.toString()));
        }
        
        //Get every recipe for each id
        for(Integer id: ids) {
            RecipesByCategory recipesByCategory = new RecipesByCategory();
            String json = recipesByCategory.getInfo(chainId, Integer.toString(id), authToken);
            matchedObjects2 = searchJSON(json, q, matchedObjects2);
        }

        //Format resonse
        matchedObjects = sortRecipesByCategory(matchedObjects2, category);
        matchedObjects.put( ApplicationConstants.recipeSearch.totalRecipes, totalRecipes(matchedObjects));
        return this.createValidResponse(matchedObjects.toString());
    }

    public Categories(){
        this.serviceType = new MWGHeader();
    }

    public JSONArray searchJSON(String jsonString, String query, JSONArray matchedObjects2) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONObject(ApplicationConstants.recipeSearch.recipes).getJSONArray(ApplicationConstants.recipeSearch.recipeSummary);
        try{
            for(Object recipe: jsonArray){
                JSONObject jsonRecipe = (JSONObject) recipe;
                Object descriptionObj = jsonRecipe.get(ApplicationConstants.recipeSearch.description);
                Object nameObj = jsonRecipe.get(ApplicationConstants.recipeSearch.name);

                String name = "";
                String description = "";

                //Make sure that description is of type string
                try{
                    description = descriptionObj.toString();
                } catch (Exception e){
                    ExceptionHandler exceptionHandler = new ExceptionHandler();
                    System.out.print(exceptionHandler.exceptionMessageJson(e));
                    description = "";
                }

                //Make sure that name is of type string
                try{
                    name = nameObj.toString();
                } catch (Exception e){
                    ExceptionHandler exceptionHandler = new ExceptionHandler();
                    System.out.print(exceptionHandler.exceptionMessageJson(e));
                    name = "";
                }

                if(description.toLowerCase().contains(query) || name.toLowerCase().contains(query) ){
                    matchedObjects2.put(matchedObjects2.length(), jsonRecipe);
                }
            }
        } catch (JSONException e){
            System.out.print(ApplicationConstants.recipeSearch.RecipeError + e.getMessage());
        }
        return matchedObjects2;
    }

    public Set<Integer> getSubcategories(JSONArray jsonArray){
        Set<Integer> retval = new HashSet<>();
        try {
            //Search JSONArray for subcategory ids
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
            System.out.print(ApplicationConstants.recipeSearch.CategoryError);
        }
        return retval;
    }

    private JSONObject sortRecipesByCategory( JSONArray matchedObjects2, String categoryQuery ){
        Set<Integer> recipeIds = new HashSet<>();
        JSONObject jsonObject = new JSONObject();

        for (Object recipe: matchedObjects2){
            JSONObject currentRecipes = (JSONObject) recipe;
            try {
                //JSON is of array type
                JSONArray categories = currentRecipes.getJSONObject(ApplicationConstants.recipeSearch.categories).getJSONArray(
                        ApplicationConstants.recipeSearch.category);
                for (Object category : categories) {
                    JSONObject currentCategory = (JSONObject) category;
                    if(categoryQuery == "") { //No query, just add id
                        recipeIds.add(currentCategory.getInt(ApplicationConstants.recipeSearch.id));
                    } else { //Has query verify that it is a valid id
                        int categoryQueryInt = Integer.parseInt(categoryQuery);
                        if(currentCategory.getInt(ApplicationConstants.recipeSearch.id) == categoryQueryInt){
                            recipeIds.add(currentCategory.getInt(ApplicationConstants.recipeSearch.id));
                        }
                    }
                }
            } catch (Exception e){
                //JSON is of object type
                JSONObject categories = currentRecipes.getJSONObject(ApplicationConstants.recipeSearch.categories).getJSONObject(
                        ApplicationConstants.recipeSearch.category);
                if(categoryQuery == "") { //No query, just add id
                    recipeIds.add(categories.getInt(ApplicationConstants.recipeSearch.id));
                } else { //Has query verify that it is a valid id
                    int categoryQueryInt = Integer.parseInt(categoryQuery);
                    if(categories.getInt(ApplicationConstants.recipeSearch.id) == categoryQueryInt){
                        recipeIds.add(categories.getInt(ApplicationConstants.recipeSearch.id));
                    }
                }
            }
        }

        JSONArray retval = new JSONArray();
        for(Integer id: recipeIds){
            JSONObject formatting = new JSONObject();
            JSONArray currentId = new JSONArray();
            String idName = "";
            for(Object recipe: matchedObjects2) {
                JSONObject currentRecipe = (JSONObject) recipe;
                try {
                    //JSON is of array type
                    JSONArray categories = currentRecipe.getJSONObject(ApplicationConstants.recipeSearch.categories).getJSONArray(
                            ApplicationConstants.recipeSearch.category);
                    for (Object category : categories) {
                        JSONObject currentCategory = (JSONObject) category;
                        int thisId = currentCategory.getInt(ApplicationConstants.recipeSearch.id);
                        if (thisId == id) {
                            currentId.put(currentId.length(), currentRecipe);
                            idName = currentCategory.getString(ApplicationConstants.recipeSearch.name);
                        }
                    }
                } catch (Exception e){
                    //JSON is of object type
                    JSONObject categories = currentRecipe.getJSONObject(ApplicationConstants.recipeSearch.categories).getJSONObject(
                            ApplicationConstants.recipeSearch.category);
                    int thisId = categories.getInt(ApplicationConstants.recipeSearch.id);
                    if (thisId == id) {
                        currentId.put(currentId.length(), currentRecipe);
                        idName = categories.getString(ApplicationConstants.recipeSearch.name);
                    }

                }
            }
            //Format recipe then add to array
            formatting.put(ApplicationConstants.recipeSearch.name, idName);
            formatting.put(ApplicationConstants.recipeSearch.items, currentId);
            formatting.put(ApplicationConstants.recipeSearch.id, id);
            retval.put(retval.length(), formatting);
        }
        return jsonObject.put(ApplicationConstants.recipeSearch.RecipeCategories, retval);
    }

    //Counts items in response
    private int totalRecipes(JSONObject matchedObjects){
        int retval = 0;
        JSONArray jsonArray = matchedObjects.getJSONArray(ApplicationConstants.recipeSearch.RecipeCategories);
        for(Object category: jsonArray){
            JSONObject currentCategory = (JSONObject) category;
            retval += currentCategory.getJSONArray(ApplicationConstants.recipeSearch.items).length();
        }
        return retval;
    }
}