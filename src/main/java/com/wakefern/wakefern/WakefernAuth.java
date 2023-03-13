package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.EnvManager;
import com.wakefern.request.HTTPRequest;

import java.util.HashMap;
import java.util.Map;


public class WakefernAuth {
    private static final int READ_TIMEOUT_MS = 10000;

    private static final String PATH = WakefernApplicationConstants.ItemLocator.baseURL + WakefernApplicationConstants.ItemLocator.authPath;

    public static String getAuthToken(String authToken) throws Exception {
        Map<String, String> headers = new HashMap<>();

        headers.put(ApplicationConstants.Requests.Headers.contentType, "text/plain");
        headers.put(ApplicationConstants.Requests.Headers.Authorization, authToken);
        
		// Call APIM Gateway to avoid any foreign IP addresses
		headers.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getSrMobilePassThruApiKeyProd());

        return HTTPRequest.executeGet(PATH, headers, READ_TIMEOUT_MS);
    }
}
