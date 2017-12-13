package com.wakefern.stores;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;

import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 9/12/16.
 */
@Path(MWGApplicationConstants.Requests.Stores.StoreLocator)
public class StoreDetails extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/storeid/{storeId}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        try {
            String xmlRequest = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            //return this.createValidResponse(xmLtoJSONConverter.convert(xmlRequest));
            return this.createValidResponse(xmLtoJSONConverter.convertToJsonStr(xmlRequest));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String storeId, String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xmlRequest = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xmlRequest);
    }

    public StoreDetails(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String storeId, String authToken) {

        this.token = ApplicationConstants.Requests.Tokens.planningToken;
        
        this.path = MWGApplicationConstants.Requests.Stores.StoreLocator 
        		+ "/" + chainId + ApplicationConstants.StringConstants.storeid 
        		+ "/" + storeId;
    }
}
