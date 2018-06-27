package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
}
