package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 8/24/16.
 *
 * Format for JSON (Would recommend getting json from checkout -> deliver/pickup -> Network inspect -> select timespot and look for fulfillment PUT packet)
 {
 "AbsoluteExpirationTime":"0001-01-01T00:00:00+00:00",
 "AvailableSlotCount": 6,
 "EndTime": "2016-08-30T17:00:00",
 "ExpiresTime": "2016-08-30T12:30:00",
 "FulfillmentType":"Pickup",
 "IsAvailable":true,
 "Links":[],
 "ReservedSlotCount":0,
 "StartTime":"2016-08-30T16:30:00",
 "TimeslotId":6865697,
 "TotalSlotCount":6,
 "day":
 {
 "date": "2016-08-30T14:30:00",
 "dayAndMonth":" Aug 30",
 "dayAndMonthComplete":" Aug 30  Tue",
 "dayOfTheWeek":" Tue",
 "dayStatus":"",
 "prettyDate":"Tuesday, August 30, 2016",
 "prettyDateNoYear":"Tuesday, August 30"
 },
 "startTime":"2016-08-30T20:30:00.000Z",
 "timeText":"4:30 PM - 5:00 PM"
 }
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class CheckoutFulfillmentPut extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/fulfillment")
    public String getInfo(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.UserCheckout
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.fulfillment;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        return HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());
    }

    public CheckoutFulfillmentPut(){
        this.serviceType = new MWGHeader();
    }
}