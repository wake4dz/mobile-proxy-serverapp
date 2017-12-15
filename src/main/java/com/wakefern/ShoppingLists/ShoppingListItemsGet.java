package com.wakefern.ShoppingLists;

import com.wakefern.Wakefern.ItemLocatorArray;
import com.wakefern.Wakefern.WakefernAuth;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 *
 * Created by zacpuste on 9/9/16.
 */
@Path(ApplicationConstants.Requests.ShoppingLists.slItemsUser)
public class ShoppingListItemsGet extends BaseService {
	

    String responseString = "";
    JSONArray items = null;
    JSONObject searchAble = null;
    JSONObject retval = null;
    double totalPrice = 0.0;
    
    @GET
    @Produces("application/*")
    @Path("/{userId}/store/{storeId}/list/{listId}")
    public Response getInfoResponse(@PathParam("userId") String userId, @PathParam("storeId") String storeId , @PathParam("listId") String listId,
                                    @QueryParam("take") String take, @QueryParam("skip") String skip, @QueryParam("shortStoreId") String shortStoreId,
                                    @DefaultValue("")@QueryParam("fq") String fq,
                                    @DefaultValue("")@QueryParam("isMember") String isMember,
                                    @HeaderParam("Authorization") String authToken) throws Exception, IOException {
        
        String path = prepareResponse(userId, storeId, listId, take, skip, fq, isMember, authToken);

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);

        String listResp = HTTPRequest.executeGetJSON(path, secondMapping.getgenericHeader(), 0);
        //return this.createValidResponse(HTTPRequest.executeGetJSON(path, secondMapping.getgenericHeader(), 0));
        return getItemLocatorForNonSRFHList(shortStoreId, listResp);
    }

    private Response getItemLocatorForNonSRFHList(String shortStoreId, String listResp){
        try {
            if (shortStoreId == null) {
                return this.createValidResponse(listResp);
            }
            JSONObject list = new JSONObject(listResp);
            items = list.getJSONArray(ApplicationConstants.AisleItemLocator.Items);
            //Object totalPrice = list.get(ApplicationConstants.AisleItemLocator.TotalPrice);
            searchAble = new JSONObject();
            retval = new JSONObject();
//            double totalPrice = 0.0;
            // Set up retval with all non-items data
            for (Object key : list.keySet()) {
                String keyStr = (String) key;
                if (!keyStr.equals(ApplicationConstants.AisleItemLocator.Items)) {
                    Object keyvalue = list.get(keyStr);
                    retval.put(keyStr, keyvalue);
                }
            }

            if (!items.isNull(0)) {
                WakefernAuth auth = new WakefernAuth();
                String authString = auth.getInfo(ApplicationConstants.AisleItemLocator.WakefernAuth);
                if (!authString.isEmpty()) {
                    // return without AISLE Data
                    StringBuilder sb = new StringBuilder();
                    
                    //run price calculation for shopping list
                    this.shoppingListPriceCalculation();

                    if(totalPrice != 0){
                        String[] totalPriceArr = String.valueOf(totalPrice).split("\\.");
                        sb.append("$"); sb.append(totalPriceArr[0]); 
                        sb.append("."); sb.append(totalPriceArr[1].length() > 1 ? totalPriceArr[1].substring(0, 2) : totalPriceArr[1]);
                        sb.append(totalPriceArr[1].length() == 1 ? "0" : "");
                    } else{
                        sb.append("--");
                    }
                    
                    items = (JSONArray) searchAble.get(ApplicationConstants.AisleItemLocator.Items);
                    responseString = responseString.substring(0, responseString.length() - 1); //remove trailing comma
                    ItemLocatorArray itemLocatorArray = new ItemLocatorArray();
                    String locatorArray = itemLocatorArray.getInfo(shortStoreId, responseString, authString);
                    HashMap<Long, Object> itemLocatorData = new HashMap<>();
                    HashMap<Long, Object> areaSeqNumData = new HashMap<>();

                    try {
                        JSONArray jsonArray = new JSONArray(locatorArray);
                        int size = jsonArray.length();
                        for (int i = 0; i < size; i++) {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            Object areaSeqNum = jsonObject.get(ApplicationConstants.AisleItemLocator.area_seq_num);
                            Object areaDesc = jsonObject.get(ApplicationConstants.AisleItemLocator.area_desc);
                            JSONArray itemLocations = jsonObject.getJSONArray(ApplicationConstants.AisleItemLocator.item_locations);
                            
                            for (int j = 0; j < itemLocations.length(); j++) {
                                Object upc13 = itemLocations.getJSONObject(j).get(ApplicationConstants.AisleItemLocator.upc_13_num);
                                try{ //if wf_area_code is found from item locator response
                                    Object wfAreaCode = itemLocations.getJSONObject(j).get(ApplicationConstants.AisleItemLocator.wf_area_code);
                                    areaSeqNumData.put(Long.parseLong(upc13.toString()), 
                                            (wfAreaCode != null && wfAreaCode.toString().trim().equals("0") ? "0" : areaSeqNum));
                                } catch(Exception e){
                                    areaSeqNumData.put(Long.parseLong(upc13.toString()), areaSeqNum);
                                }
                                itemLocatorData.put(Long.parseLong(upc13.toString()), (areaDesc != null && !areaDesc.toString().equals("null"))
                                        ? areaDesc : ApplicationConstants.AisleItemLocator.Other);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
//                        throw e;
                    }

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String itemId = item.get(ApplicationConstants.AisleItemLocator.Sku).toString();
                        String upc = this.updateUPC(itemId);

//                      while (upc.charAt(0) == '0') {
//                          upc = upc.substring(0, upc.length());
//                      }
                        Object areaSeqNumValue = areaSeqNumData.get(Long.parseLong(upc));//itemLocatorData.get(upc);
                        int areaSeqInt = Integer.parseInt(areaSeqNumValue.toString());
                        if(areaSeqInt > 0){
                            item.put(ApplicationConstants.AisleItemLocator.Aisle, itemLocatorData.get(Long.parseLong(upc)).toString());
                        } else { // area_seq_num = 0, -1, or -999 - INVALID
                            item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
                        }
                        
                        retval.append(ApplicationConstants.AisleItemLocator.Items, item);
                    }
                    retval.remove(ApplicationConstants.AisleItemLocator.TotalPrice);
                    retval.put(ApplicationConstants.AisleItemLocator.TotalPrice, sb.toString());
                    return this.createValidResponse(retval.toString());
                }
            } else { // Return without anything
                for (Object item : items) {
                    JSONObject currentItem = (JSONObject) item;
                    currentItem.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
                    retval.append(ApplicationConstants.AisleItemLocator.Items, currentItem);
                }
                return this.createValidResponse(retval.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
            return this.createValidResponse(listResp);
        }
        return this.createValidResponse(listResp);
    }
    
    /**
     * This method provides price calculation for 4 pricing format scenarios in Shopping List
     * 1) "$7.47 (avg.) / each"
     * 		i) "Min Qty: 1, Limit: 4 Items" if limit is 4, then sales price @ 4, 5th item will be regular price
     * 2) "$6.99/lb"
     * 3) "n for $2.99"
     * 		i) if quantity > n, then price per item [(current Price) $2.99 / n] * quantity
     *      ii) if quantity < n, then regular price * quantity
     * 4) "$0.69"
     * 		i) if current sale Price is '$0.69', then quantity * current Price
     * @param totalPrice
     * @param searchAble
     * @param retval
     * @param items
     * @param responseString
     */
    private void shoppingListPriceCalculation(){

        boolean isAvgPricing = false; // for item pricing "$7.47 (avg.) / each"
        boolean isLbPricing = false; // for item pricing "$6.99/lb"
        for (int i = 0, size = items.length(); i < size; i++) {
            // Get the items in the array and make a comma separated string of them as well trim the first and last digit
            JSONObject item = (JSONObject) items.get(i);
            String itemId = item.get(ApplicationConstants.AisleItemLocator.Sku).toString();
            String regularPrice = item.get(ApplicationConstants.AisleItemLocator.RegularPrice).toString();
            String currentPrice = item.get(ApplicationConstants.AisleItemLocator.CurrentPrice).toString();
            int quantity = Integer.parseInt(item.get(ApplicationConstants.AisleItemLocator.Quantity).toString());
            try{
            	String[] currentPriceArr;
            	if(currentPrice.contains("lb")){ 
            		// "$3.99/lb" pricing
            		currentPriceArr = currentPrice.split("/");
            		isLbPricing = true;
            	} else if(currentPrice.contains("avg")){ 
            		//"$7.47 (avg.) / each" pricing
            		currentPriceArr = currentPrice.split("\\(");
            		isAvgPricing = true;
            	} else{	
            		// "n for $2.99", "$0.69" pricing
            		currentPriceArr = currentPrice.split(" ");	
            	}
            	
            	if(isAvgPricing || currentPriceArr.length == 1){ 
            		// "$1.99" or "$7.47 (avg.) / each"
            		totalPrice += quantity * Double.parseDouble(currentPriceArr[0].replace("$", ""));
                	isAvgPricing = false;
            	} else{
            		// "$3.99/lb" or "n for $2.99"
                    JSONObject sale = item.getJSONObject(ApplicationConstants.AisleItemLocator.Sale);
            		if(isLbPricing){
            			// "$3.99/lb"
                        String[] quantityPerLbArr = item.get(ApplicationConstants.AisleItemLocator.Size).toString().split(" ");
                        if(sale != null){ //check for limit
                            String limitText = sale.get(ApplicationConstants.AisleItemLocator.LimitText).toString();
                            if(limitText.contains("Limit")){
                            	//LimitText : "Min Qty: 1, Limit: 4 Items"
                            	int limitQty = Integer.parseInt(limitText.split("\\:")[2].trim().split(" ")[0]); // returns 4
                            	int aboveLimitInt = quantity - limitQty;
                            	if(aboveLimitInt > 0){
                            		totalPrice += aboveLimitInt * Double.parseDouble(quantityPerLbArr[0]) * Double.parseDouble(regularPrice.replace("$", ""))
                            				+ limitQty * Double.parseDouble(quantityPerLbArr[0]) * Double.parseDouble(currentPriceArr[0].replace("$", ""));
                            	} else {
                            		totalPrice += quantity * Double.parseDouble(quantityPerLbArr[0]) * Double.parseDouble(currentPriceArr[0].replace("$", ""));
                            	}
                            } else{
                            	totalPrice += quantity * Double.parseDouble(quantityPerLbArr[0]) * Double.parseDouble(currentPriceArr[0].replace("$", ""));
                            }
                        } else{
                        	totalPrice += quantity * Double.parseDouble(quantityPerLbArr[0]) * Double.parseDouble(currentPriceArr[0].replace("$", ""));
                        }
                		isLbPricing = false;
                    } else{
                    	// "n for $2.99"
                        int currentPriceQty = Integer.parseInt(currentPriceArr[0]);
                        double currentPriceStr = Double.parseDouble(currentPriceArr[currentPriceArr.length - 1].replace("$", ""));
                        double pricePerItem = currentPriceStr / currentPriceQty;
                    	if(sale != null){
                            String limitText = sale.get(ApplicationConstants.AisleItemLocator.LimitText).toString();
                            if(limitText.contains("Limit")){
                            	int limitQty = Integer.parseInt(limitText.split("\\:")[2].trim().split(" ")[0]); // returns 4
                            	int aboveLimitInt = quantity - limitQty;
                            	if(aboveLimitInt > 0){
                            		totalPrice += aboveLimitInt * Double.parseDouble(regularPrice.replace("$", ""))
                            				+ limitQty * pricePerItem;//Double.parseDouble(currentPriceArr[0].replace("$", ""));
                            	} else if(quantity >= currentPriceQty){
    	                            totalPrice += quantity * pricePerItem;
    	                        } else{
    	                            totalPrice += quantity * Double.parseDouble(regularPrice.replace("$", ""));
    	                        }
                            } else{
                            	if(quantity >= currentPriceQty){
    	                            totalPrice += quantity * pricePerItem;
    	                        } else{
    	                            totalPrice += quantity * Double.parseDouble(regularPrice.replace("$", ""));
    	                        }
                            }
                    	} else {
	                        if(quantity >= currentPriceQty){
	                            totalPrice += quantity * pricePerItem;
	                        } else{
	                            totalPrice += quantity * Double.parseDouble(regularPrice.replace("$", ""));
	                        }
                    	}
                    }
            	}
            } catch(Exception ex){
                totalPrice = 0.0;
                System.out.println("[ShoppingListItemsGet]::::Error calculating price in Shopping List");
                ex.printStackTrace();
            }
            /*
            if(regularPrice.isEmpty()){
                String[] currentPriceArr = currentPrice.split(" ");
                regularPrice = currentPriceArr[currentPriceArr.length-1];
            }
            regularPrice = regularPrice.replace("$", "");
            for(int j = 0; j < quantity; j++){
                totalPrice += Double.parseDouble(regularPrice);
            }
            */
            String sku = this.updateUPC(itemId);
            if (sku.matches("[0-9]+")) {
                responseString += sku + ",";
                searchAble.append(ApplicationConstants.AisleItemLocator.Items, item);
            } else {
                item.put(ApplicationConstants.AisleItemLocator.Aisle, ApplicationConstants.AisleItemLocator.Other);
                retval.append(ApplicationConstants.AisleItemLocator.Items, item);
            }
        }
    }

    private String updateUPC(String sku) {    return sku.substring(0, sku.length() - 1);    }
    
    public String getInfo(String userId, String storeId , String listId, String take, String skip, String fq, String isMember, String authToken) throws Exception, IOException {
        return this.getInfoFilter(userId, storeId, listId, take, skip, isMember, authToken, "", fq);
    }
    
    public String getInfoFilter(String userId, String storeId , String listId, String take, 
            String skip, String isMember, String authToken, String filter, String fq) throws Exception, IOException {
        String path = prepareResponse(userId, storeId, listId, take, skip, fq, isMember, authToken);
        
        //Fix filter 
        if(!filter.isEmpty()){
            path += "&fq=" + URLEncoder.encode(filter, "UTF-8");
            //System.out.println("Filter With Path :: " + path);
        }else{
            //System.out.println("No Filter With Path :: " + path);

        }

        ServiceMappings secondMapping = new ServiceMappings();
        secondMapping.setGetMapping(this, null);
        
        return HTTPRequest.executeGetJSON(path, secondMapping.getgenericHeader(), 0);
    }

    public String prepareResponse(String userId, String storeId, String listId, String take, String skip, String fq, String isMember, String authToken){
        this.requestToken = authToken;

        this.requestPath = "https://shop.shoprite.com/api" + ApplicationConstants.Requests.ShoppingLists.slItemsUser
                + ApplicationConstants.StringConstants.backSlash + userId + ApplicationConstants.StringConstants.store
                + ApplicationConstants.StringConstants.backSlash + storeId + ApplicationConstants.StringConstants.list
                + ApplicationConstants.StringConstants.backSlash + listId + ApplicationConstants.StringConstants.take
                + take + ApplicationConstants.StringConstants.skip + skip;

        if(!fq.isEmpty()){
            try {
                String fqEncoded = URLEncoder.encode(fq, "UTF-8");
                this.requestPath += ApplicationConstants.StringConstants.fq + fqEncoded;
            } catch (Exception e){
                System.out.print("ShoppingListItemGet:: Error Encoding fq " + e.getMessage());
            }
        }

        if(!isMember.isEmpty()){
            this.requestPath += ApplicationConstants.StringConstants.isMemberAmp;
        }
        return requestPath;
    }

    public ShoppingListItemsGet(){
        this.requestHeader = new MWGHeader();
    }
}

