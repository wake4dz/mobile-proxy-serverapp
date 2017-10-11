package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zacpuste on 10/4/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserOrder)
public class CreateOrChangeOrder extends BaseService {
	
	private final static Logger logger = Logger.getLogger("CreateOrChangeOrder");

    /**
     *
     * @param userId
     * @param storeId
     * @param authToken NOTE This auth is the regular user auth
     * @param jsonBody
     * @return
     * @throws Exception
     * @throws IOException
     */
    @POST
    @Consumes("*/*")
    @Produces("*/*")
    @Path("/{userId}/store/{storeId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-results+json");
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-summary+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
        	String response = HTTPRequest.executePostJSON(mapping.getPath(), "", map, 30000);
            logger.log(Level.INFO, "[getInfoResponse]::URL: {0}, response: {1}", new Object[]{this.path, response});

            return this.createValidResponse(response);
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String isMember, String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings mapping = new ServiceMappings();
        mapping.setPutMapping(this, jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-results+json");
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.order-summary+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return (HTTPRequest.executePostJSON(mapping.getPath(), mapping.getGenericBody(), map, 0));
    }

    public CreateOrChangeOrder(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.UserOrder
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Checkout.UserOrder
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.isMember;
        }
    }
}
