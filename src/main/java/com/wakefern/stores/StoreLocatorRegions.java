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
 * Created by zacpuste on 11/14/16.
 */
@Path(MWGApplicationConstants.Requests.Stores.StoreLocator)
public class StoreLocatorRegions extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/regions")
    public Response getInfoResponse(@PathParam("chainId") String chainId,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        try {
            String xmlRequest = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            return this.createValidResponse(xmLtoJSONConverter.convert(xmlRequest));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String authToken) throws Exception, IOException {
        prepareResponse(chainId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xmlRequest = HTTPRequest.executeGet(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xmlRequest);
    }

    public StoreLocatorRegions(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String authToken){
    	
    		this.requestToken = ApplicationConstants.Requests.Tokens.planningToken;

        this.requestPath = MWGApplicationConstants.Requests.Stores.StoreLocator
                + "/" + chainId + ApplicationConstants.StringConstants.regions;
    }
}

