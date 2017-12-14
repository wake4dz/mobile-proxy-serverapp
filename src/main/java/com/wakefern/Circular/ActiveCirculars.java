package com.wakefern.Circular;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wakefern.dao.activecircular.ActiveCircular;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Circular.Categories)
public class ActiveCirculars extends BaseService{
	
	private final static Logger logger = Logger.getLogger("ActiveCirculars");
	
	
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try {
        	String resp = HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
        	ObjectMapper objMapper = new ObjectMapper();
        	ActiveCircular[] acArr = objMapper.readValue(resp, ActiveCircular[].class);
        	for(ActiveCircular ac : acArr){
        		if(ac.getCircularType().equals("weekly")){
        			ac.setCircularType(ac.getCircularType().replace("w", "W"));
                	logger.log(Level.INFO, "[getInfoResponse]::ActiveCirculars circular type: ", ac.getCircularType());
        		}
        	}
        	ObjectWriter writer = objMapper.writer();
        	resp = writer.writeValueAsString(acArr);

            return this.createValidResponse(resp);
        } catch (Exception e){
        	logger.log(Level.SEVERE, "[getInfoResponse]::Exception, path: ", e.getMessage());
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String chainId, String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGetJSON( secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public ActiveCirculars(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String chainId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.circulars;
        if(!isMember.isEmpty()){
            this.path = ApplicationConstants.Requests.Circular.Categories + ApplicationConstants.StringConstants.backSlash
                    + chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                    + storeId + ApplicationConstants.StringConstants.circulars + ApplicationConstants.StringConstants.isMember;
        }
    }
}
