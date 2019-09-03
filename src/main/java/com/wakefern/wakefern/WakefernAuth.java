package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class WakefernAuth extends BaseService {
	
    public WakefernAuth() {
        this.requestHeader = new MWGHeader();
    }

    public String getInfo(String authToken) throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.authPath;
        
        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
        wkfn.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);

    		String response = HTTPRequest.executeGet(path, wkfn, 10000);
        
        	return response;
    }

    /**
     * Get item location, pass in appCode to get back token response
     * @return
     * @throws Exception
     * @throws IOException
     */
    public String getItemInfo() throws Exception, IOException {
        Map<String, String> wkfn = new HashMap<>();

        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.itemInfo_authPath;
        
        wkfn.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
        wkfn.put(ApplicationConstants.Requests.Header.contentAccept, MWGApplicationConstants.Headers.json);
        wkfn.put(ApplicationConstants.Requests.Header.appCode, WakefernApplicationConstants.ItemLocator.itemInfo_appCode);

    	String response = getToken(HTTPRequest.executePost(path, "{}", wkfn));
    	return response;
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
