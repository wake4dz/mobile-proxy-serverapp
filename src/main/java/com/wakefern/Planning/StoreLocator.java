package com.wakefern.Planning;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 9/12/16.
 */
@Path(ApplicationConstants.Requests.Planning.StoreLocator)
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
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String zip, String rad, String units, String pageNum, String sizeNum, String authToken){
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }else{
        	this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }

        this.path = ApplicationConstants.Requests.Planning.StoreLocator
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.postalCode
                + ApplicationConstants.StringConstants.backSlash + zip + ApplicationConstants.StringConstants.radius
                + ApplicationConstants.StringConstants.backSlash + rad + ApplicationConstants.StringConstants.unit
                + ApplicationConstants.StringConstants.backSlash + units + ApplicationConstants.StringConstants.page
                + ApplicationConstants.StringConstants.backSlash + pageNum + ApplicationConstants.StringConstants.size
                + ApplicationConstants.StringConstants.backSlash + sizeNum;
    }
}


