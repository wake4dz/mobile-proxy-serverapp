package com.wakefern.Products;

import com.wakefern.Wakefern.ItemLocatorArray;
import com.wakefern.Wakefern.WakefernAuth;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by zacpuste on 8/25/16.
 */
@Path(ApplicationConstants.Requests.Categories.ProductStore)
public class ProductBySku extends BaseService {
    @GET
    @Produces("application/*")
    @Path("/{storeId}/sku/{skuId}")
    public Response getInfoResponse(@PathParam("storeId") String storeId, @PathParam("skuId") String skuId,
                                    @DefaultValue("") @QueryParam("shortStoreId") String shortStoreId,
                            @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        prepareResponse(storeId, skuId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        try {
            String skuData = HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
            if(shortStoreId.isEmpty()){
                return this.createValidResponse(skuData);
            }
            JSONObject retval = formatAisle(skuData, shortStoreId);
            return this.createValidResponse(retval.toString());
        } catch (Exception e){
            return this.createErrorResponse(e);
        }
    }

    public String getInfo(String storeId, String skuId, String authToken) throws Exception, IOException {
        prepareResponse(storeId, skuId, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setMapping(this);

        return HTTPRequest.executeGet(secondMapping.getPath(), secondMapping.getgenericHeader());
    }

    public ProductBySku(){
        this.serviceType = new MWGHeader();
    }

    private void prepareResponse(String storeId, String skuId, String authToken){
        this.token = authToken;
        this.path = ApplicationConstants.Requests.Categories.ProductStore
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.sku
                + ApplicationConstants.StringConstants.backSlash + skuId;
    }

    private JSONObject formatAisle(String skuData, String shortStoreId) throws Exception {
        JSONObject jsonObject = new JSONObject(skuData);
        String aisle = jsonObject.getString(ApplicationConstants.AisleItemLocator.Aisle);
        if(aisle == null || aisle == ""){
            return jsonObject;
        }
        WakefernAuth wakefernAuth = new WakefernAuth();
        String authString = wakefernAuth.getInfo(ApplicationConstants.AisleItemLocator.WakefernAuth);
        if(!authString.isEmpty()){
            String sku = jsonObject.getString(ApplicationConstants.AisleItemLocator.Sku);
            sku = sku.substring(0, sku.length()-1);
            ItemLocatorArray itemLocatorArray = new ItemLocatorArray();
            String itemLocation = itemLocatorArray.getInfo(shortStoreId, sku, authString);
            try{
                JSONArray jsonArray = new JSONArray(itemLocation);
                for (int i = 0, size = jsonArray.length(); i < size; i++){
                    JSONObject locationItems = (JSONObject)jsonArray.get(i);
                    JSONArray locations = locationItems.getJSONArray(ApplicationConstants.AisleItemLocator.item_locations);
                    //Iterate through these too - inner excluded UPCs
                    for(int z = 0, sizez = locations.length(); z<sizez;z++) {
                        JSONObject aItem = (JSONObject) locations.get(z);
                        Long itemNum = aItem.getLong(ApplicationConstants.AisleItemLocator.upc_13_num);
                        String itemString = itemNum.toString();
                        if (sku.contains(itemString)) {
                            if (aItem.has(ApplicationConstants.AisleItemLocator.area_desc)) {
                                jsonObject.put(ApplicationConstants.AisleItemLocator.Aisle, aItem.get(ApplicationConstants.AisleItemLocator.area_desc));
                                return jsonObject;
                            } else {
                                jsonObject.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
                                break;
                            }
                        } else if (z + 1 == locations.length()) {
                            jsonObject.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
                        }
                    }

                }
                return jsonObject;
            }catch(Exception e){
                //Error casting
                throw e;
            }
        }
        return jsonObject;
    }
}
