package com.wakefern.api.wakefern.push2device;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * History:
 * Date:   2020-2-4
 * Author: Matthew Miller
 * Desc:   Unregister a Price Plus Card members' device with Push2Device service.
 */
@Path(WakefernApplicationConstants.Push2Device.Proxy.unregisterPath)
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

            String apiKey= VcapProcessor.getPush2DeviceApiKey();

            Map<String, String> headers = new HashMap<>();
            headers.put(ApplicationConstants.Requests.Headers.contentType, "application/json");
            headers.put(ApplicationConstants.Requests.Headers.Authorization, "basic " + apiKey);

            JSONObject args = parseBodyIntoArgs(jsonData);

            String path = VcapProcessor.getPush2DeviceEndpoint()
                    + WakefernApplicationConstants.Push2Device.Upstream.devicesPath;
            String jsonResponse = HTTPRequest.executePostJSON(path, args.toString(), headers, VcapProcessor.getApiLowTimeout());
            logger.debug("[Push2Device::UnregisterDevice] upstream response: " + jsonResponse);
            // Return empty response
            return this.createResponse(204);

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, ErrorType.PUSH2DEVICE_UNREGISTER);
            String errorData = LogUtil.getRequestData("exceptionLocation",
                    LogUtil.getRelevantStackTrace(e));
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            Response response = createErrorResponse(errorData, e);
            return UpstreamErrorHandler.handleResponse(response);
        }
    }
}