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
import java.net.URLEncoder;

/**
 * Created by zacpuste on 11/14/16.
 */
@Path(ApplicationConstants.Requests.Planning.StoreLocator)
public class StoresByRegion extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/region/{regionId}/page/{pageNumber}/size/{pageSize}")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("regionId") String regionId,
                                    @PathParam("pageNumber") String pageNumber, @PathParam("pageSize") String pageSize,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        regionId = URLEncoder.encode(regionId, "UTF-8");
        regionId = regionId.replaceAll("\\+", "%20");
        prepareResponse(chainId, regionId, pageNumber, pageSize, authToken);

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

    public String getInfo(String chainId, String regionId, String pageNumber, String pageSize, String authToken)
            throws Exception, IOException {
        regionId = URLEncoder.encode(regionId, "UTF-8");
        regionId = regionId.replaceAll("\\+", "%20");
        prepareResponse(chainId, regionId, pageNumber, pageSize, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMappingv1(this, null);

        String xmlRequest = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader(), 0);
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();
        return xmLtoJSONConverter.convert(xmlRequest);
    }

    public StoresByRegion(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String regionId, String pageNumber, String pageSize, String authToken){
        if(authToken.equals(ApplicationConstants.Requests.Tokens.RosettaToken)){
            this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }else{
            this.token = ApplicationConstants.Requests.Tokens.planningToken;
        }

        this.path = ApplicationConstants.Requests.Planning.StoreLocator
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.region
                + ApplicationConstants.StringConstants.backSlash + regionId + ApplicationConstants.StringConstants.page
                + ApplicationConstants.StringConstants.backSlash + pageNumber + ApplicationConstants.StringConstants.size
                + ApplicationConstants.StringConstants.backSlash + pageSize;
    }
}
