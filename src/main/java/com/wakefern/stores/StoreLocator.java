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
public class StoreLocator extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/postalCode/{zip}/radius/{radius}/unit/{units}/page/{pageNum}/size/{sizeNum}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("zip") String zip, @PathParam("radius") String rad,
                            @PathParam("units") String units, @PathParam("pageNum") String pageNum, @PathParam("sizeNum") String sizeNum,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, zip, rad, units, pageNum, sizeNum, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        try {
            String xmlRequest = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
            XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
            return this.createValidResponse(xmLtoJSONConverter.convert(xmlRequest));
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String zip, String rad, String units, String pageNum, String sizeNum, String authToken) throws Exception, IOException {
        prepareResponse(chainId, zip, rad, units, pageNum, sizeNum, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xmlRequest = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xmlRequest);
    }

    public StoreLocator(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String zip, String rad, String units, String pageNum, String sizeNum, String authToken) {
    
    		this.token = ApplicationConstants.Requests.Tokens.planningToken;

        this.requestPath = MWGApplicationConstants.Requests.Stores.StoreLocator
                + "/" + chainId + ApplicationConstants.StringConstants.postalCode
                + "/" + zip + ApplicationConstants.StringConstants.radius
                + "/" + rad + ApplicationConstants.StringConstants.unit
                + "/" + units + ApplicationConstants.StringConstants.page
                + "/" + pageNum + ApplicationConstants.StringConstants.size
                + "/" + sizeNum;
    }
}


