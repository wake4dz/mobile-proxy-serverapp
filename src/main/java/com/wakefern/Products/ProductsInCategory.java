package com.wakefern.Products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.Search;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductCategory)
public class ProductsInCategory extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{categoryId}/store/{storeId}")
    public Response getInfoResponse(@PathParam("categoryId") String categoryId, @PathParam("storeId") String storeId,
                                    @QueryParam("take") String take, @QueryParam("skip") String skip,
                                    @DefaultValue("")@QueryParam("fq") final List<String> fqList, @DefaultValue("")@QueryParam("sort") String sort,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        try {
            //Create partial url for passing to functions
            String partialUrl = ApplicationConstants.Requests.baseURLV5 + ApplicationConstants.Requests.Categories.ProductCategory
                    + ApplicationConstants.StringConstants.backSlash + categoryId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId;

            //Calculate takes for error handling
            int maxTake = calcMaxTake(partialUrl, isMember, authToken);
            int currentTake = Integer.parseInt(take);

            if(maxTake == 0){
                JSONObject matchedObjects = new JSONObject();
                JSONArray emptyArray = new JSONArray(); //Used to format like a successful response, but just with an empty array
                matchedObjects.put(ApplicationConstants.ProductSearch.items, emptyArray);
                matchedObjects.put(ApplicationConstants.ProductSearch.itemCount, 0);
                matchedObjects.put(ApplicationConstants.ProductSearch.totalQuantity, 0);
                return this.createValidResponse(matchedObjects.toString());
            }

            if(currentTake > maxTake){
                take = String.valueOf(maxTake);
                this.setMore(false);
            } else {
                this.setMore(true);
            }

            String json = search(partialUrl, take, skip, fqList, sort, isMember, authToken);

            if((json.equals("[null]") || json.equals(null)) && fqList != null){
                Exception e = new Exception("No results for for take");
                return this.createErrorResponse(e);
            }

            JSONArray jsonArray = new JSONArray(json);
            return this.createValidResponse(formatResponse(jsonArray, take, skip));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    private int calcMaxTake(String partialUrl, String isMember, String authToken) throws Exception{
        try {
            String json = search(partialUrl, "1", "0", null, "", isMember, authToken);
            JSONArray jsonArray = new JSONArray(json);
            return jsonArray.getJSONObject(0).getInt(ApplicationConstants.ProductSearch.itemCount);
        } catch (Exception e){
            throw e;
        }
    }

    public ProductsInCategory(){
        this.requestHeader = new MWGHeader();
    }

    private String formatResponse(JSONArray jsonArray, String take, String skip){
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
                //System.out.print(ExceptionHandler.ExceptionMessage(e));
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
        retval.put(ApplicationConstants.ProductSearch.skip, Integer.parseInt(skip));
        retval.put(ApplicationConstants.ProductSearch.take, Integer.parseInt(take));
        retval.put(ApplicationConstants.ProductSearch.totalQuantity, (zeroth.getInt(ApplicationConstants.ProductSearch.totalQuantity)));
        retval.put(ApplicationConstants.ProductSearch.itemCount, (zeroth.getInt(ApplicationConstants.ProductSearch.itemCount)));
        retval.put(ApplicationConstants.ProductSearch.links, zeroth.getJSONArray(ApplicationConstants.ProductSearch.links));
        retval.put(ApplicationConstants.ProductSearch.moreAvailiable, this.isMore);

        return retval.toString();
    }

    private String search(String partialUrl, String take, String skip, List<String> fqList, String sort, String isMember, String authToken) throws Exception{
        this.requestToken = authToken;
        int intTake = Integer.parseInt(take);
        int initSkip = Integer.parseInt(skip);
        int finalLoop = intTake % 20;

        //Logic to determine if < 20 for a take
        if(intTake < 20 && intTake > 0){
            intTake = 0;
            finalLoop = Integer.parseInt(take);
        } else {
            intTake -= finalLoop;
            intTake /= 20;
        }

        String[] jsonArray = new String[intTake + 1];

        for(int i = 0; i < intTake; i++){
            this.requestPath = partialUrl + ApplicationConstants.StringConstants.take
                    + ApplicationConstants.StringConstants.twenty + ApplicationConstants.StringConstants.skip
                    + String.valueOf((20 * i) + initSkip );

            if(!sort.isEmpty()){//sort != ""){
                sort = URLEncoder.encode(sort, "UTF-8");
                this.requestPath = this.requestPath + ApplicationConstants.StringConstants.sort + sort;
            }

            if(fqList != null) {
                for (String query : fqList){
                    query = URLEncoder.encode(query, "UTF-8");
                   this.requestPath = this.requestPath + ApplicationConstants.StringConstants.fq + query;
                }
            }

            if(!isMember.isEmpty()){
                this.requestPath += ApplicationConstants.StringConstants.isMemberAmp;
            }

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setGetMapping(this, null);

            String result = HTTPRequest.executeGetJSON(this.requestPath, secondMapping.getgenericHeader(), 0);
            jsonArray[i] = result;
        }

        if(finalLoop != 0) {
            this.requestPath = partialUrl + ApplicationConstants.StringConstants.take
                    + Integer.toString(finalLoop) + ApplicationConstants.StringConstants.skip
                    + String.valueOf((20 * intTake) + initSkip);

            if(!sort.isEmpty()){//sort != ""){
                sort = URLEncoder.encode(sort, "UTF-8");
                this.requestPath = this.requestPath + ApplicationConstants.StringConstants.sort + sort;
            }

            if(fqList != null) {
                for (String query : fqList){
                    query = URLEncoder.encode(query, "UTF-8");
                    this.requestPath = this.requestPath + ApplicationConstants.StringConstants.fq + query;
                }
            }

            if(!isMember.isEmpty()){
                this.requestPath += ApplicationConstants.StringConstants.isMemberAmp;
            }

            ServiceMappings secondMapping = new ServiceMappings();
            secondMapping.setGetMapping(this, null);

            String result = HTTPRequest.executeGetJSON(this.requestPath, secondMapping.getgenericHeader(), 0);
            jsonArray[intTake] = result;
        }
        return Arrays.deepToString(jsonArray);
    }

    boolean isMore;
    public boolean isMore() {
        return isMore;
    }
    public void setMore(boolean more) {
        isMore = more;
    }
}
