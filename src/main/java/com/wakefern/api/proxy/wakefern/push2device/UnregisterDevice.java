package com.wakefern.api.proxy.wakefern.push2device;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * History:
 * Date:   2022-07-13
 * Author: Matthew Miller
 * Desc:   Unregister a Price Plus Card members' device with Push2Device service.
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.Push2Device.Proxy.unregisterPath)
public class UnregisterDevice extends BaseService {
    private final static Logger logger = LogManager.getLogger(RegisterDevice.class);

    /**
     * Helper method to parse request payload into Push2Device json args.
     * @param String jsonPayload encoded json string
     * @return the JSONObject of the Push2Device request arguments
     */
    private static JSONObject parseBodyIntoArgs(String jsonPayload) {
        JSONObject body = new JSONObject(jsonPayload);
        JSONObject args = new JSONObject();
        args.put("token", body.getString("token"));
        return args;
    }

    @DELETE
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path("/")
    public Response unregisterDevice(String jsonData) {
        try {
            logger.info("[Push2Device::UnregisterDevice] jsonData: " + jsonData);

            final String apiKey = VcapProcessor.getPush2DeviceApiKey();

            Map<String, String> headers = new HashMap<>();
            headers.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
            headers.put(ApplicationConstants.Requests.Headers.Authorization, "basic " + apiKey);

            JSONObject args = parseBodyIntoArgs(jsonData);

            String path = VcapProcessor.getPush2DeviceUrl()
                    + WakefernApplicationConstants.Push2Device.Upstream.devicesPath;
            String jsonResponse = HTTPRequest.executePostJSON(path, args.toString(), headers, VcapProcessor.getApiLowTimeout());
            logger.debug("[Push2Device::UnregisterDevice] upstream response: " + jsonResponse);
            // Return empty response
            return this.createResponse(Response.Status.NO_CONTENT);

        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation",
                    LogUtil.getRelevantStackTrace(e));
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            Response response = createErrorResponse(errorData, e);
            return UpstreamErrorHandler.handleResponse(response);
        }
    }
}