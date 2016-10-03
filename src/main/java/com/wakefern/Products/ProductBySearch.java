package com.wakefern.Products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.Search;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
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
    public String getInfo(@PathParam("storeId") String storeId, @QueryParam("q") String q, @QueryParam("take") String take, @QueryParam("skip") String skip,
                          @DefaultValue("")@QueryParam("fq") String fq, @DefaultValue("")@QueryParam("sort") String sort,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;
        try {
            if(q != "") {
                q = URLEncoder.encode(q, "UTF-8");
            }
        } catch (Exception e){

        }

        String partialUrl = ApplicationConstants.Requests.Categories.ProductsStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.search
                + ApplicationConstants.StringConstants.queryParam + q;

        int maxTake = calcMaxTake(partialUrl, authToken);
        int currentTake = Integer.parseInt(take);
        boolean isMore = true;

        if(maxTake == 0){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Status", 400);
            jsonObject.put("Message", "No results for search parameter");
            return jsonObject.toString();
        }

        if(maxTake == -1){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Status", 401);
            jsonObject.put("Message", "Bad authorization token");
            return jsonObject.toString();
        }
        if(currentTake > maxTake){
            System.out.print("Modified Take Param");
            take = String.valueOf(maxTake);
            isMore = false;
        }

        Search search = new Search();
        String json = search.search(partialUrl, take, skip, fq, sort, authToken);

        if((json.equals("[null]") || json.equals(null)) && fq != ""){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Status", 400);
            jsonObject.put("Message", "No results for for take");
            return jsonObject.toString();
        }

        JSONArray jsonArray = new JSONArray(json);
        return formatResponse(jsonArray, take, skip, isMore);
    }

    private int calcMaxTake(String partialUrl, String authToken){
        Search search = new Search();
        String json = search.search(partialUrl, "1", "0", "", "", authToken);

        if(json.contains("401 for URL")){
            return -1;
        }
        JSONArray jsonArray = new JSONArray(json);
        return jsonArray.getJSONObject(0).getInt(ApplicationConstants.ProductSearch.itemCount);
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