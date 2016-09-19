package com.wakefern.Products;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.Search;
import org.json.JSONArray;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductsStore)
public class ProductBySearch extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/search")
    public String getInfo(@PathParam("storeId") String storeId,  @QueryParam("q") String q, @QueryParam("take") String take, @QueryParam("skip") String skip,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        String partialUrl = ApplicationConstants.Requests.Categories.ProductsStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.search
                + ApplicationConstants.StringConstants.queryParam + q;

        Search search = new Search();
        String retval = search.search(partialUrl, take, skip, authToken);
        JSONArray jsonArray = new JSONArray(retval);
        int itemCount = jsonArray.getJSONObject(0).getInt("ItemCount");

        if(itemCount == 0){
            return "{}";
        } else {
            return retval;
        }
    }
}