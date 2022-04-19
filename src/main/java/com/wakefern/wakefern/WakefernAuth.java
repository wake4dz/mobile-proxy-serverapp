package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class WakefernAuth {

    public static String getInfo(String authToken) throws Exception {
        Map<String, String> wkfn = new HashMap<>();

        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.authPath;
        
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
        wkfn.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

        return HTTPRequest.executeGet(path, wkfn, 10000);
    }

    /**
     * Get item location, pass in appCode to get back token response
     * @return
     * @throws Exception
     * @throws IOException
     */
    public static String getItemInfo(String appCode) throws Exception {
        Map<String, String> wkfn = new HashMap<>();

        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.prefix + WakefernApplicationConstants.ItemLocator.itemInfo_tokenPath;
        
        wkfn.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
        wkfn.put(ApplicationConstants.Requests.Header.contentAccept, MWGApplicationConstants.Headers.json);
//        wkfn.put(ApplicationConstants.Requests.Header.appCode, WakefernApplicationConstants.ItemLocator.itemInfo_appCode);
        wkfn.put(ApplicationConstants.Requests.Header.appCode, appCode); //appCode = KPC1

//    	String response = getToken(HTTPRequest.executePost(path, "{}", wkfn)); //leave the getToken implementation for future interal cart impl to get item locator..
        return HTTPRequest.executePost(path, "{}", wkfn);
    }
    
    /**
     * Extract the token from item info's response
     * @param itemInfoResp
     * @return
     */
    private String getToken(String itemInfoResp){

    	String returnDataKey = "returnData";
    	String authKey = "Authorization";
    	
        JSONObject jsonData = new JSONObject(itemInfoResp);
        JSONObject returnDataObj = (jsonData.has(returnDataKey)) ? jsonData.getJSONObject(returnDataKey) : null;
        return (returnDataObj != null && returnDataObj.has(authKey)) ? returnDataObj.getString(authKey) : "";
    }
}
