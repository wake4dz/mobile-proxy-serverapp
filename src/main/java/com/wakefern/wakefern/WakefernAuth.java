package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.request.HTTPRequest;

import java.util.HashMap;
import java.util.Map;


public class WakefernAuth {
    public static String getInfo(String authToken) throws Exception {
        Map<String, String> wkfn = new HashMap<>();

        String path = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.authPath;

        wkfn.put(ApplicationConstants.Requests.Headers.contentType, "text/plain");
        wkfn.put(ApplicationConstants.Requests.Headers.Authorization, authToken);

        return HTTPRequest.executeGet(path, wkfn, 10000);
    }
}
