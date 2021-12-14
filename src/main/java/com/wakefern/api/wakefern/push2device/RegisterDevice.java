package com.wakefern.api.wakefern.push2device;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWT;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
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
 * Desc:   Register a Price Plus Card members' device with Push2Device service (an internally managed Wakefern API
 * that maps MI9 userIds to unique push/device tokens/identifiers.
 */
@Path(WakefernApplicationConstants.Push2Device.Proxy.registerPath)
public class RegisterDevice extends BaseService {
    private final static Logger logger = LogManager.getLogger(RegisterDevice.class);

    /**
     * Helper method to parse request payload into Push2Device json args.
     * @param jsonPayload encoded json string
     * @param appVersion - Version of the app that made the request.
     * @return the JSONObject of the Push2Device request arguments
     */
    private static JSONObject parseBodyIntoArgs(String jsonPayload, String appVersion) {
        JSONObject body = new JSONObject(jsonPayload);
        JSONObject args = new JSONObject();
        args.put("contact", body.getJSONObject("contact"));
        args.put("platform", body.getString("platform"));
        args.put("token", body.getString("token"));
        args.put("app", appVersion);
        args.put("acceptsNotifications", body.optBoolean("acceptsNotifications", true));
        return args;
    }

    @POST
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path("/")
    @ValidatePPCWithJWT
    public Response registerDevice(
            @HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion,
            @PathParam("userId") String userId,
            @PathParam("ppc") String ppc,
            String jsonData)
    {
        try {
            logger.debug("[Push2Device::RegisterDevice] ppc: " + ppc + " jsonData: " + jsonData);
            Map<String, String> headers = new HashMap<>();

            String path = VcapProcessor.getPush2DeviceEndpoint()
                    + WakefernApplicationConstants.Push2Device.Upstream.devicesPath;

            String apiKey = VcapProcessor.getPush2DeviceApiKey();

            headers.put(ApplicationConstants.Requests.Header.contentType, "application/json");
            headers.put(ApplicationConstants.Requests.Header.contentAuthorization, "basic " + apiKey);

            JSONObject args = parseBodyIntoArgs(jsonData, appVersion);
            String jsonResponse = HTTPRequest.executePostJSON(path, args.toString(), headers, VcapProcessor.getApiLowTimeout());

            logger.debug("[Push2Device::RegisterDevice] ppc: " + ppc + " upstream response: " + jsonResponse);
            return this.createResponse(202);
        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.PUSH2DEVICE_REGISTER);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            Response response = createErrorResponse(errorData, e);
            return UpstreamErrorHandler.handleResponse(response);
        }
    }
}