package com.wakefern.Checkout;

import com.wakefern.Lists.ListHelpers;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zacpuste on 8/24/16.
 */
@Path(ApplicationConstants.Requests.Checkout.UserCheckout)
public class CheckoutBillingAddressPut extends BaseService {
	
	private final static Logger logger = Logger.getLogger("CheckoutBillingAddressPut");
    @PUT
    @Produces("application/*")
    /*****
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
        String path = "";
        String resp = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonBody);
	        try{
	        	String firstName = jsonObject.get("FirstName").toString();
	        	jsonObject.put("FirstName", firstName);
	        	if(firstName.isEmpty()){
	            	jsonObject.put("FirstName", "_");
	            }
	        } catch (Exception e){
	            jsonObject.put("FirstName", "_");
	        }
	        
	        try{
	        	String lastName = jsonObject.get("LastName").toString();
	        	jsonObject.put("LastName", lastName);
	        	if(lastName.isEmpty()){
	            	jsonObject.put("LastName", "_");
	            }
	        } catch (Exception e){
	            jsonObject.put("LastName", "_");
	        }
	        
	        ServiceMappings secondMapping = new ServiceMappings();
	        path = prepareResponse(userId, storeId, isMember, authToken);
	
	        secondMapping.setPutMapping(this, jsonObject.toString());
	
	        Map<String, String> map = new HashMap();
	        map.put(ApplicationConstants.Requests.Header.contentType, "application/json");
	        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
	        resp = HTTPRequest.executePut("", path, "", secondMapping.getGenericBody(), map, 0);
	        return this.createValidResponse(resp);
        } catch(Exception ex){
        	logger.log(Level.SEVERE, ListHelpers.errorMsgStr("[getInfoResponse]::BILLING ADDR EXCEPTION! "+ex.getMessage()
        			+", path: "+path, userId, storeId, "", "", jsonBody, resp));
        	return this.createErrorResponse(ex);
        }
    }
    


    public String getInfo(String userId, String storeId, String authToken, String isMember, String jsonBody) throws Exception, IOException {
        JSONObject jsonObject = new JSONObject(jsonBody);
        try{
        	String firstName = jsonObject.get("FirstName").toString();
        	jsonObject.put("FirstName", firstName);
            jsonObject.getString("FirstName");
        } catch (Exception e){
            jsonObject.put("FirstName", "_");
        }
        try{
        	String lastName = jsonObject.get("LastName").toString();
        	jsonObject.put("LastName", lastName);
            jsonObject.getString("LastName");
        } catch (Exception e){
            jsonObject.put("LastName", "_");
        }

        String path = prepareResponse(userId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setPutMapping(this, jsonBody);

        Map<String, String> map = new HashMap();
        map.put(ApplicationConstants.Requests.Header.contentType, "application/vnd.mywebgrocer.address+json");
        map.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executePut("", path, "", secondMapping.getGenericBody(), map, 0);
    }

    public CheckoutBillingAddressPut(){
        this.serviceType = new MWGHeader();
    }

    private String prepareResponse(String userId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.path = "https://api.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.address
                + ApplicationConstants.StringConstants.billing;
        if(!isMember.isEmpty()){
            this.path = "https://api.shoprite.com/api" + ApplicationConstants.Requests.Checkout.UserCheckout + ApplicationConstants.StringConstants.backSlash
                    + userId + ApplicationConstants.StringConstants.store
                    + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.address
                    + ApplicationConstants.StringConstants.billing + ApplicationConstants.StringConstants.isMember;
        }
        return path;
    }
}