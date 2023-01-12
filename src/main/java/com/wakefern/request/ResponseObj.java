package com.wakefern.request;


import com.wakefern.global.errorHandling.ResponseHandler;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class ResponseObj {
    private String responseBody;

    private Map<String, List<String>> responseHeaders;

    private ResponseObj(String body, Map<String, List<String>> headers) {
        this.responseBody = body;
        this.responseHeaders = headers;
    }

    public static ResponseObj fromConnection(HttpURLConnection connection) throws Exception {
        String body = ResponseHandler.getResponse(connection);
        return new ResponseObj(body, connection.getHeaderFields());
    }

    public String getResponseBody() {
        return responseBody;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }
}
