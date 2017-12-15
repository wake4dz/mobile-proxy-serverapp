package com.wakefern.Circular;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.Search;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularSearch extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/items")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,
                            @QueryParam("q") String q, @QueryParam("take") String take, @QueryParam("skip") String skip,
                            @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.requestToken = authToken;
        String partialUrl = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.items + ApplicationConstants.StringConstants.queryParam + q;

        Search search = new Search();
        try {
            return this.createValidResponse(search.search(partialUrl, take, skip, "", "", isMember, authToken));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
//      todo can currently take more than max need to modify max take
    }
}