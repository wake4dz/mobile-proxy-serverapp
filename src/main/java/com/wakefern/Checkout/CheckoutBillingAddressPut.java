package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zacpuste on 8/24/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class CheckoutBillingAddressPut extends BaseService {
    @PUT
    @Produces("application/*")
    /**
     * {
     * Format for passed in json
     "FirstName": "Brandyn",
     "LastName": "Ngo",
     "Phone1": {
     "Number": "7322334775",
     "IsMobile": true
     },
     "Phone2": null,
     "Street1": "2513 Autumn drive",
     "Line2": "",
     "Line3": "",
     "City": "Manasquan",
     "State": "NJ",
     "PostalCode": "08736",
     "CountryCode": "USA",
     "NeighborhoodId": "",
     "DeliveryPointId": null,
     "Validated": false
     }
     */
    @Path("/{userId}/store/{storeId}/address/billing")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        JSONObject jsonObject = new JSONObject(jsonBody);
        try{
            jsonObject.getString("FirstName");
        } catch (Exception e){
            jsonObject.put("FirstName", "_");
        }
        try{
            jsonObject.getString("LastName");
        } catch (Exception e){
            jsonObject.put("LastName", "_");
        }
        String path = prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        try {
            return this.createValidResponse(HTTPRequest.executePut("", path, "", secondMapping.getGenericBody(), secondMapping.getgenericHeader(), 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String authToken, String isMember, String jsonBody) throws Exception, IOException {
        String path = prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        return HTTPRequest.executePut("", path, "", secondMapping.getGenericBody(), secondMapping.getgenericHeader(), 0);
    }

    public CheckoutBillingAddressPut(){
        this.serviceType = new MWGHeader();
    }

    private String prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.address
                + ApplicationConstants.StringConstants.billing;
        if(!isMember.isEmpty()){
            this.path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                    + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.address
                    + ApplicationConstants.StringConstants.billing + ApplicationConstants.StringConstants.isMember;
        }
        return path;
    }
}