package com.wakefern.Circular;

import java.io.IOException;

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
import com.wakefern.dao.circular.pages.CircularPagesDetail;
import com.wakefern.dao.circular.pages.ImageLink_;
import com.wakefern.dao.circular.pages.Item;
import com.wakefern.dao.circular.pages.Page;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

/**
 * Created by zacpuste on 10/6/16.
 */
@Path(ApplicationConstants.Requests.Circular.Categories)
public class CircularPages extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{chainId}/stores/{storeId}/circulars/{circId}/pages")
    public Response getInfo(@PathParam("chainId") String chainId, @PathParam("storeId") String storeId, @PathParam("circId") String circId,
                            @DefaultValue("0") @QueryParam("skip") String skip, @DefaultValue("9999") @QueryParam("take") String take,
                            @DefaultValue("")@QueryParam("isMember") String isMember,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        String path = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.Circular.Categories
                + ApplicationConstants.StringConstants.backSlash + chainId + ApplicationConstants.StringConstants.stores
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.circulars
                + ApplicationConstants.StringConstants.backSlash + circId + ApplicationConstants.StringConstants.pages
                + ApplicationConstants.StringConstants.take + take + ApplicationConstants.StringConstants.skip + skip;

        if(!isMember.isEmpty()){
            path += ApplicationConstants.StringConstants.isMemberAmp;
        }

        MWGHeader mwgHeader = new MWGHeader();
        mwgHeader.authenticate(authToken, "application/vnd.mywebgrocer.circular-pages-full+json", "application/vnd.mywebgrocer.circular-pages-full+json");

        try {
        	String respStr = HTTPRequest.executeGet(path, mwgHeader.getMap(), 0);
        	
        	ObjectMapper objMapper = new ObjectMapper();
        	CircularPagesDetail cpd = objMapper.readValue(respStr, CircularPagesDetail.class);
        	String[] valueInJsonPagesMain = {"Pages"};
        	String[] valueInJsonItem = {"Id", "AreaCoordinates", "Category",
//        		    "DisplayCategory",
        		    "ItemType", "Title", "Description", "PriceText", "ImageName",
//        		    "Options",
        		    "ImageLinks",
//        		    "Links",
//        		    "ChildItems"
        	};
        	FilterProvider filProd = new SimpleFilterProvider()
        			.addFilter("filterByCircularPagesValue", SimpleBeanPropertyFilter.filterOutAllExcept(valueInJsonPagesMain))
        			.addFilter("filterByCircularItemValue", SimpleBeanPropertyFilter.filterOutAllExcept(valueInJsonItem));
        	
        	for(Page page: cpd.getPages()){
        		for(Item item : page.getItems()){
        			for(int i = 0; i < item.getImageLinks().size(); i++){
        				ImageLink_ imgLnk = item.getImageLinks().get(i);
        				if(imgLnk.getRel() != null && !imgLnk.getRel().equalsIgnoreCase("full")){
        					item.getImageLinks().remove(i);
        				}
        			}
        		}
        	}
        	ObjectWriter writer = objMapper.writer(filProd);
        	String jsonRespStr = writer.writeValueAsString(cpd);
        	
            return this.createValidResponse(jsonRespStr);//respStr);
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public CircularPages() {
        this.requestHeader = new MWGHeader();
    }
}
