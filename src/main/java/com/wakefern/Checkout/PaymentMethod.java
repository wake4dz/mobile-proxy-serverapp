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
 * Created by zacpuste on 10/21/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class PaymentMethod extends BaseService {
	

	private final static Logger logger = Logger.getLogger("PaymentMethod");
	
    @PUT
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/payment")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        String path = preparedResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);
    	logger.log(Level.INFO, "[getInfoResponse]::jsonBody: ", jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.links+json");
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.payments+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
        	String resp = HTTPRequest.executePut(path, secondMapping.getGenericBody(), map);
        	logger.log(Level.INFO, "[getInfoResponse]::response: ", resp);
            return this.createValidResponse(resp);
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String authToken, String isMember, String jsonBody) throws Exception, IOException {
        String path = preparedResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.links+json");
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.payments+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        String resp = HTTPRequest.executePut(path, secondMapping.getGenericBody(), map);

        logger.log(Level.INFO, "[preparedResponse]::response: ", resp);
        return resp;
    }

    public PaymentMethod(){
        this.requestHeader = new MWGHeader();
    }

    private String preparedResponse(String userId, String storeId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.payment;
        if(!isMember.isEmpty()){
            this.requestPath = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout
                    + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.payment
                    + ApplicationConstants.StringConstants.isMember;
        }
        return requestPath;
    }
}