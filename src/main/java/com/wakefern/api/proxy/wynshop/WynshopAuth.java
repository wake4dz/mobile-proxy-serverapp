package com.wakefern.api.proxy.wynshop;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.WynshopAuth.Proxy.Path)
public class WynshopAuth extends BaseService {
    private static final Logger logger = LogManager.getLogger(WynshopAuth.class);

    private static final String REQUEST_URL = WakefernApplicationConstants.WynshopAuth.Upstream.BaseURL + WakefernApplicationConstants.WynshopAuth.Upstream.tokenPath;

    private static final String SCOPES = "mwg.ecm.storefrontmobile:all offline_access";

    @POST
    public Response getAccessToken(String body) {
        try {
            JSONObject payload = new JSONObject(body);
            final String username = payload.optString("username", null);
            final String password = payload.optString("password", null);
            final String refreshToken = payload.optString("refreshToken", null);

            if (username == null || password == null) {
                if (refreshToken != null) {
                    return makeRefreshRequest(refreshToken);
                } else {
                    throw new BadRequestException("Missing arguments");
                }
            }
            return makePasswordRequest(username, password);
        } catch (Exception e) {
            if (LogUtil.isLoggable(e)) {
                String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
                logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            }
            return this.createErrorResponse(e);
        }
    }

    private static HashMap<String, String> getReqHeaders() {
        HashMap<String, String> headerMap = new HashMap<>(4);
        //for the Cloudflare pass-thru
        headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
        headerMap.put(Requests.Headers.Authorization, "Basic " + VcapProcessor.getWynshopAuthKey());
        headerMap.put(Requests.Headers.Accept, WakefernApplicationConstants.WynshopAuth.Upstream.accept);
        headerMap.put(Requests.Headers.contentType, WakefernApplicationConstants.WynshopAuth.Upstream.contentType);

        return headerMap;
    }

    private static String encodeParams(Map<String, String> parameters) throws UnsupportedEncodingException {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> e : parameters.entrySet()) {
            String s = e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8");
            joiner.add(s);
        }
        return joiner.toString();
    }

    private Response makePasswordRequest(String username, String password) throws Exception {
        Map<String, String> headerMap = getReqHeaders();
        Map<String, String> parameters = new HashMap<>(4);
        parameters.put("scope", SCOPES);
        parameters.put("grant_type", "password");
        parameters.put("username", username);
        parameters.put("password", password);
        final String encodedBody = encodeParams(parameters);
        String response = HTTPRequest.executePost(REQUEST_URL, encodedBody, headerMap, VcapProcessor.getApiMediumTimeout());
        return createValidResponse(response);
    }

    private Response makeRefreshRequest(String refreshToken) throws Exception {
        Map<String, String> headerMap = getReqHeaders();
        Map<String, String> parameters = new HashMap<>(3);
        parameters.put("scope", SCOPES);
        parameters.put("grant_type", "refresh_token");
        parameters.put("refresh_token", refreshToken);
        final String encodedBody = encodeParams(parameters);
        String response = HTTPRequest.executePost(REQUEST_URL, encodedBody, headerMap, VcapProcessor.getApiMediumTimeout());
        return createValidResponse(response);
    }
}