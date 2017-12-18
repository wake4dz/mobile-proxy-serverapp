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

/**
 * Created by zacpuste on 10/31/16.
 */

@Path(ApplicationConstants.Requests.Checkout.Checkout)
public class DistrictDeliveryDate extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/delivery/{zipCode}/district/{districtId}/dates")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("zipCode") String zipCode, @PathParam("districtId") String districtId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, zipCode, districtId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.fulfillment-dates+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), map, 0));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String zipCode, String districtId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(storeId, zipCode, districtId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.fulfillment-dates+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executeGet(secondMapping.getPath(), map, 0);
    }

    public DistrictDeliveryDate(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String storeId, String zipCode, String districtId, String isMember, String authToken){
        this.requestToken = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.Checkout
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.delivery
                + ApplicationConstants.StringConstants.backSlash + zipCode + ApplicationConstants.StringConstants.distrcit
                + ApplicationConstants.StringConstants.backSlash + districtId + ApplicationConstants.StringConstants.dates;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.Checkout
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.delivery
                    + ApplicationConstants.StringConstants.backSlash + zipCode + ApplicationConstants.StringConstants.distrcit
                    + ApplicationConstants.StringConstants.backSlash + districtId + ApplicationConstants.StringConstants.dates
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
//https://shopritemobileapplication7.mybluemix.net/api/checkout/v5/fulfillments/store/070C334/delivery/33330/district/23/dates
//                                                 api/checkout/v5/fulfillments/store/070C334/delivery/33330/district/23/dates
//                        https://api.shoprite.com/api/checkout/v5/fulfillments/store/070C334/delivery/33330/district/23/dates