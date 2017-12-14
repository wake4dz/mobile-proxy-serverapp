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
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zacpuste on 8/24/16.
 */

@Path(ApplicationConstants.Requests.Checkout.Checkout)
public class FulfillmentDeliveryDateTimes extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/delivery/{zipCode}/{year}/{month}/{day}/times")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("zipCode") String zipCode,
                            @PathParam("year") String year, @PathParam("month") String month, @PathParam("day") String day,
                            @DefaultValue("")@QueryParam("districtId") String distId,
                            @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, zipCode, year, month, day, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.fulfillment-times+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        try {
            if(distId.isEmpty()) {
                return this.createValidResponse(HTTPRequest.executeGet(secondMapping.getPath(), map, 0));
            } else {
                String regular = HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
                DistrictDeliveryDateTimes districtDeliveryDateTimes = new DistrictDeliveryDateTimes();
                String district = districtDeliveryDateTimes.getInfo(storeId, zipCode, distId, year, month, day, isMember, authToken);
                return this.createValidResponse(format(regular, district));
            }
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String zipCode, String year, String month, String day, String isMember,
                          String authToken) throws Exception, IOException {
        prepareResponse(storeId, zipCode, year, month, day, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentAccept, "application/vnd.mywebgrocer.fulfillment-times+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executeGet(secondMapping.getPath(), map, 0);
    }

    public FulfillmentDeliveryDateTimes(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String storeId, String zipCode, String year, String month, String day,
                                 String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash
                + storeId +  ApplicationConstants.StringConstants.delivery + ApplicationConstants.StringConstants.backSlash
                + zipCode + ApplicationConstants.StringConstants.backSlash + year + ApplicationConstants.StringConstants.backSlash
                + month + ApplicationConstants.StringConstants.backSlash + day + ApplicationConstants.StringConstants.times;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Checkout.Checkout + ApplicationConstants.StringConstants.backSlash
                    + storeId +  ApplicationConstants.StringConstants.delivery + ApplicationConstants.StringConstants.backSlash
                    + zipCode + ApplicationConstants.StringConstants.backSlash + year + ApplicationConstants.StringConstants.backSlash
                    + month + ApplicationConstants.StringConstants.backSlash + day + ApplicationConstants.StringConstants.times
                    + ApplicationConstants.StringConstants.isMember;
        }
    }

    private String format(String regular, String district){
        JSONObject jsonObject = new JSONObject();
        JSONObject regularJSON = new JSONObject(regular);
        JSONObject districtJSON = new JSONObject(district);
        jsonObject.put(ApplicationConstants.StringConstants.FulfillmentJSON, regularJSON);
        jsonObject.put(ApplicationConstants.StringConstants.DistrictJSON, districtJSON);
        return jsonObject.toString();
    }
}
