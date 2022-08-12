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

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.WynshopAuth.Proxy.Path)
public class WynshopAuth extends BaseService {
    private final static Logger logger = LogManager.getLogger(WynshopAuth.class);

    @POST
    public Response getAccessToken(String body) {
        try {
            JSONObject payload = new JSONObject(body);
            final String username = payload.getString("username");
            final String password = payload.getString("password");

            final String url = WakefernApplicationConstants.WynshopAuth.Upstream.BaseURL + WakefernApplicationConstants.WynshopAuth.Upstream.tokenPath;

            Map<String, String> headerMap = new HashMap<>();
            //for the Cloudflare pass-thru
            headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
            headerMap.put(Requests.Headers.Authorization, "Basic " + VcapProcessor.getWynshopAuthKey());
            headerMap.put(Requests.Headers.Accept, WakefernApplicationConstants.WynshopAuth.Upstream.accept);
            headerMap.put(Requests.Headers.contentType, WakefernApplicationConstants.WynshopAuth.Upstream.contentType);

            Map<String, String> parameters = new HashMap<>();
            parameters.put("scope", "mwg.ecm.storefrontmobile:all offline_access");
            parameters.put("grant_type", "password");
            parameters.put("username", username);
            parameters.put("password", password);

            StringJoiner joiner = new StringJoiner("&");
            for (Map.Entry<String, String> e : parameters.entrySet()) {
                String s = e.getKey() + "=" + URLEncoder.encode(e.getValue(), "UTF-8");
                joiner.add(s);
            }
            final String form = joiner.toString();

            String response = HTTPRequest.executePost(url, form, headerMap, VcapProcessor.getApiMediumTimeout());
            return this.createValidResponse(response);
        } catch (Exception e) {

            if (LogUtil.isLoggable(e)) {
                String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
                logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            }

            return this.createErrorResponse(e);
        }
    }
}