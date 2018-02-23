package com.wakefern.wakefern.itemLocator;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zacpuste on 10/18/16.
 */

public class ItemLocatorArray extends BaseService {

    public String getInfo( String storeId, String upc, String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.locationPath + "/" + storeId + "/" + upc;
        
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "application/json");
        wkfn.put("Authentication", authToken);

        try {
            return HTTPRequest.executeGet(path, wkfn, 0);
        } catch (Exception e){
            return null;
        }
    }

    public ItemLocatorArray(){
        this.requestHeader = new MWGHeader();
    }
}