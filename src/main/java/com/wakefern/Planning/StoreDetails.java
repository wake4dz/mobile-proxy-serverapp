package com.wakefern.Planning;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.global.XMLtoJSONConverter;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import java.io.IOException;

/**
 * Created by zacpuste on 9/12/16.
 */
@Path(ApplicationConstants.Requests.Planning.StoreLocator)
public class StoreDetails extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/storeid/{storeId}")
    public String getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,
                          @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        this.token = authToken;

        this.path = ApplicationConstants.Requests.Planning.StoreLocator
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.storeid
                + ApplicationConstants.StringConstants.backSlash + storeId ;

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setServiceMapping(this, null);

        String xmlRequest = HTTPRequest.executeGetJSON(secondMapping.getServicePath(), secondMapping.getgenericHeader());
        XMLtoJSONConverter xmLtoJSONConverter = new XMLtoJSONConverter();

        return xmLtoJSONConverter.convert(xmlRequest);
    }

    public StoreDetails(){
        this.serviceType = new MWGHeader();
    }
}
