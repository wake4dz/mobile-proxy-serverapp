package com.wakefern.api.proxy.wakefern.push2device;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.global.errorHandling.UpstreamErrorHandler;
import com.wakefern.logging.LogUtil;
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
 * Date:   2023-07-13
 * Author: Matthew Miller
 * Desc:   Register a Price Plus Card members' device with Push2Device service (an internally managed Wakefern API
 * that maps PPCs to unique push/device tokens/identifiers.
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.Push2Device.Proxy.registerPath)
public class RegisterDevice extends BaseService {
    private final static Logger logger = LogManager.getLogger(RegisterDevice.class);

    /**
     * Helper method to parse request payload into Push2Device json args.
     * @param jsonPayload encoded json string
     * @return the JSONObject of the Push2Device request arguments
     */
    private static JSONObject parseBodyIntoArgs(String jsonPayload) {
        JSONObject body = new JSONObject(jsonPayload);
        JSONObject args = new JSONObject();
        args.put("contact", body.getJSONObject("contact"));
        args.put("platform", body.getString("platform"));
        args.put("token", body.getString("token"));
        args.put("app", body.getString("app"));
        args.put("acceptsNotifications", body.optBoolean("acceptsNotifications", true));
        return args;
    }

    @POST
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path("/")
    @ValidatePPCWithJWTV2
    public Response registerDevice(
            @PathParam("ppc") String ppc,
            String jsonData)
    {
        try {
            logger.debug("[Push2Device::RegisterDevice] ppc: " + ppc + " jsonData: " + jsonData);
            Map<String, String> headers = new HashMap<>();

            final String path = VcapProcessor.getPush2DeviceUrl()
                    + WakefernApplicationConstants.Push2Device.Upstream.devicesPath;

            final String apiKey = VcapProcessor.getPush2DeviceApiKey();

            headers.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
            headers.put(ApplicationConstants.Requests.Headers.Authorization, "basic " + apiKey);

            JSONObject args = parseBodyIntoArgs(jsonData);
            String jsonResponse = HTTPRequest.executePostJSON(path, args.toString(), headers, VcapProcessor.getApiLowTimeout());

            logger.debug("[Push2Device::RegisterDevice] ppc: " + ppc + " upstream response: " + jsonResponse);
            return this.createResponse(Response.Status.ACCEPTED);
        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            Response response = createErrorResponse(errorData, e);
            return UpstreamErrorHandler.handleResponse(response);
        }
    }
}