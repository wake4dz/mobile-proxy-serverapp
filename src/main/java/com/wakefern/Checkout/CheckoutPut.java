package com.wakefern.Checkout;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/24/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class CheckoutPut extends BaseService {
    @PUT
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}")
    /**
     * Format for json
     {
     "Invoice": [
     {
     "Name": "Product Total",
     "Items": [
     ],
     "Value": "$56.14",
     "DescriptionHtml": null
     },
     {
     "Name": "Total Fees",
     "Items": [
     ],
     "Value": "$19.95",
     "DescriptionHtml": null
     },
     {
     "Name": "Tax Total",
     "Items": [
     ],
     "Value": "$1.96",
     "DescriptionHtml": null
     }
     ],
     "Value": "$78.05",
     "WorkflowLinks": [
     {
     "Rel": "items",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950",
     "Status": "Completed",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "address.billing",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/address/billing",
     "Status": "Completed",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "address.delivery",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/address/delivery",
     "Status": "Completed",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "fulfillment",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/fulfillment",
     "Status": "Completed",
     "StartUri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/fulfillments",
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "substitutions",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/substitutions",
     "Status": "Optional",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "payment",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/payment",
     "Status": "Required",
     "StartUri": "https://api.shoprite.com/api/checkout/v5/payments/store/8BE678950/Delivery",
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "comments",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/comments",
     "Status": "Optional",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "order",
     "Uri": "https://api.shoprite.com/api/checkout/v5/orders/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950",
     "Status": "RequirementsNotSatisfied",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "subscriptions",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/subscription/programs",
     "Status": "Required",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "subscriptionState",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/subscription",
     "Status": "Required",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "promo.code",
     "Uri": "https://api.shoprite.com/api/checkout/v5/user/7603a30d-e3f1-46e7-bc2a-911b1508b22a/store/8BE678950/promocode",
     "Status": "Optional",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     },
     {
     "Rel": "refer.a.friend",
     "Uri": "https://api.shoprite.com/api/checkout/v5/users/7603a30d-e3f1-46e7-bc2a-911b1508b22a/stores/8BE678950/refer/friend",
     "Status": "Optional",
     "StartUri": null,
     "Placeholders": [
     ],
     "Queries": null
     }
     ]
     }
     */
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId,
                            @HeaderParam("Authorization") String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        try {
            return this.createValidResponse(HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader()));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String userId, String storeId, String authToken, String jsonBody) throws Exception, IOException {
        prepareResponse(userId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        return HTTPRequest.executePut("", secondMapping.getPath(), "", secondMapping.getGenericBody(), secondMapping.getgenericHeader());
    }

    public CheckoutPut(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String userId, String storeId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId;
    }
}