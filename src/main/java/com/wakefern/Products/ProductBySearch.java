package com.wakefern.Products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.Search;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductsStore)
public class ProductBySearch extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/search")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @QueryParam("q") String q, @QueryParam("take") String take, @QueryParam("skip") String skip,
                            @DefaultValue("")@QueryParam("fq") String fq, @DefaultValue("")@QueryParam("sort") String sort,
                            @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        try {
            if(q != "") {
                q = URLEncoder.encode(q, "UTF-8");
            }
        } catch (Exception e){
                Exception ex = new Exception("Invalid encoding scheme, please use UTF-8");
                return this.createErrorResponse(ex);
        }

        String partialUrl = ApplicationConstants.Requests.Categories.ProductsStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.search
                + ApplicationConstants.StringConstants.queryParam + q;

        int maxTake = calcMaxTake(partialUrl, isMember, authToken);
        int currentTake = Integer.parseInt(take);
        boolean isMore = true;

        if(maxTake == 0){
            Exception e = new Exception("No results for search parameter");
            JSONObject matchedObjects = new JSONObject();
            JSONArray emptyArray = new JSONArray(); //Used to format like a successful response, but just with an empty array
            matchedObjects.put(ApplicationConstants.ProductSearch.items, emptyArray);
            matchedObjects.put(ApplicationConstants.ProductSearch.itemCount, 0);
            matchedObjects.put(ApplicationConstants.ProductSearch.totalQuantity, 0);
            return this.createValidResponse(matchedObjects.toString());
        }

        if(maxTake == -1){
            Exception e = new Exception("Bad authorization token");
            return this.createErrorResponse(e);
        }
        if(currentTake > maxTake){
            take = String.valueOf(maxTake);
            isMore = false;
        }

        Search search = new Search();
        String json = search.search(partialUrl, take, skip, fq, sort, isMember, authToken);

        if((json.equals("[null]") || json.equals(null)) && fq != ""){
            Exception e = new Exception("No results for for take");
            return this.createErrorResponse(e);
        }

        JSONArray jsonArray = new JSONArray(json);
        try {
            return this.createValidResponse(formatResponse(jsonArray, take, skip, isMore));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    private int calcMaxTake(String partialUrl, String isMember, String authToken) throws Exception{
        Search search = new Search();
        try {
            String json = search.search(partialUrl, "1", "0", "", "", isMember, authToken);
            if (json.contains("401 for URL")) {
                return -1;
            }
            JSONArray jsonArray = new JSONArray(json);
            return jsonArray.getJSONObject(0).getInt(ApplicationConstants.ProductSearch.itemCount);
        } catch (Exception e){
            throw e;
        }
    }

    private String formatResponse(JSONArray jsonArray, String take, String skip, boolean isMore){
        JSONArray items = new JSONArray();
        JSONArray facets = new JSONArray();
        for(Object match: jsonArray){
            try {
                JSONObject currentMatch = (JSONObject) match;

                JSONArray item = currentMatch.getJSONArray(ApplicationConstants.ProductSearch.items);
                for (int j = 0; j < item.length(); j++) {
                    items.put(item.getJSONObject(j));
                }
                JSONArray facet = currentMatch.getJSONArray(ApplicationConstants.ProductSearch.facets);
                for (int j = 0; j < facet.length(); j++) {
                    facets.put(facet.getJSONObject(j));
                }
            } catch (Exception e) {
                System.out.print(ExceptionHandler.ExceptionMessage(e));
            }
        }

        JSONObject zeroth = jsonArray.getJSONObject(0);
        JSONObject retval = new JSONObject();

        retval.put(ApplicationConstants.ProductSearch.activeFilters, zeroth.getJSONArray(ApplicationConstants.ProductSearch.activeFilters));
        retval.put(ApplicationConstants.ProductSearch.recentFilters, zeroth.getJSONArray(ApplicationConstants.ProductSearch.recentFilters));
        retval.put(ApplicationConstants.ProductSearch.facets, facets);
        retval.put(ApplicationConstants.ProductSearch.sortLinks, zeroth.getJSONArray(ApplicationConstants.ProductSearch.sortLinks));
        retval.put(ApplicationConstants.ProductSearch.pages, zeroth.getJSONArray(ApplicationConstants.ProductSearch.pages));
        retval.put(ApplicationConstants.ProductSearch.pageLinks, zeroth.getJSONArray(ApplicationConstants.ProductSearch.pageLinks));
        retval.put(ApplicationConstants.ProductSearch.items, items);
        retval.put(ApplicationConstants.ProductSearch.skip, skip);
        retval.put(ApplicationConstants.ProductSearch.take, take);
        retval.put(ApplicationConstants.ProductSearch.totalQuantity, Integer.toString(zeroth.getInt(ApplicationConstants.ProductSearch.totalQuantity)));
        retval.put(ApplicationConstants.ProductSearch.itemCount, Integer.toString(zeroth.getInt(ApplicationConstants.ProductSearch.itemCount)));
        retval.put(ApplicationConstants.ProductSearch.links, zeroth.getJSONArray(ApplicationConstants.ProductSearch.links));
        retval.put(ApplicationConstants.ProductSearch.moreAvailiable, isMore);

        return retval.toString();
    }
}