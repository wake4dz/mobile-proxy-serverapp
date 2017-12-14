package com.wakefern.Circular;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.wakefern.dao.circularsall.CircularsAllDAO;
import com.wakefern.dao.circularsall.ImageLink;
import com.wakefern.dao.circularsall.Item;
import com.wakefern.dao.circularsall.Page;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 8/19/16.
 */

@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularsAll extends BaseService{
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/all")
    public Response getInfoResponse(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        try {
        	String circularResp = HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
        	ObjectMapper mapper = new ObjectMapper();
        	CircularsAllDAO[] circularAllDaoArr = mapper.readValue(circularResp, CircularsAllDAO[].class);
        	
        	for(CircularsAllDAO cirAllDao : circularAllDaoArr){
//        		List<ImageLink> imgLinkList = cirAllDao.getImageLinks();
//        		cirAllDao.setImageLinks(this.ImageLinkScrubber(imgLinkList));
        		
        		List<Page> cirPageList = cirAllDao.getPages();
        		for(Page cirPage : cirPageList){
        			List<ImageLink> imgLnkPageList = cirPage.getImageLinks();
        			cirPage.setImageLinks(this.imageLinkScrubber(imgLnkPageList));
        			
        			List<Item> cirPageItemList = cirPage.getItems();
        			for(Item cirPageItem : cirPageItemList){
        				List<ImageLink> imgLnkCirPageItemList = cirPageItem.getImageLinks();
        				cirPageItem.setImageLinks(this.imageLinkScrubber(imgLnkCirPageItemList));
        			}
        		}
        	}
        	String[] circularValuesArr = {
//        		    "Id", "AreaCoordinates",
        		    "Category",
//        		    "DisplayCategory",
        		    "ItemType",
//        		    "Title",
        		    "Description",
//        		    "PriceText",
        		    "ImageName", "Options",
//        		    "ImageLinks",
        		    "Links", "ChildItems"
        	};
        	
        	FilterProvider filterCirFields = new SimpleFilterProvider()
        			.addFilter("filterByCirPageItemValue", SimpleBeanPropertyFilter.serializeAllExcept(circularValuesArr));
        	ObjectWriter writer = mapper.writer(filterCirFields);
//        	ObjectWriter writer = mapper.writer();
        	
        	String cirAllFiltered = writer.writeValueAsString(circularAllDaoArr);
            return this.createValidResponse(cirAllFiltered);
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }
    
    /**
     * Only take the image with Rel type = "full", since that's the only URL SR app will use @ frontend.
     * @param imgLnkList
     * @return
     */
    private List<ImageLink> imageLinkScrubber(List<ImageLink> imgLnkList){
		List<ImageLink> imgLinkFull = new ArrayList<ImageLink>();
		for(ImageLink imgLnk : imgLnkList){
			if(imgLnk.getRel().equalsIgnoreCase("full") || imgLnk.getRel().equalsIgnoreCase("large")){
				imgLinkFull.add(imgLnk);
				break;
			}
		}
		return imgLinkFull;
    }

    public String getInfo(String chainId, String storeId, String isMember, String authToken) throws Exception, IOException {
        prepareResponse(chainId, storeId, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        return HTTPRequest.executeGetJSON(secondMapping.getPath(), secondMapping.getgenericHeader(), 0);
    }

    public CircularsAll(){
        this.requestHeader = new MWGHeader();
    }

    private void prepareResponse(String chainId, String storeId, String isMember, String authToken){
        this.token = authToken;
        this.requestPath = ApplicationConstants.Requests.Circular.Categories+ ApplicationConstants.StringConstants.backSlash +
                chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                + storeId + ApplicationConstants.StringConstants.circulars + ApplicationConstants.StringConstants.all;
        if(!isMember.isEmpty()){
            this.requestPath = ApplicationConstants.Requests.Circular.Categories+ ApplicationConstants.StringConstants.backSlash +
                    chainId + ApplicationConstants.StringConstants.stores + ApplicationConstants.StringConstants.backSlash
                    + storeId + ApplicationConstants.StringConstants.circulars + ApplicationConstants.StringConstants.all
                    + ApplicationConstants.StringConstants.isMember;
        }
    }
}
