package com.wakefern.api.proxy.apim.smsenrollment;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy endpoints for SRFH SMS enrollments APIs V8 (deployed in APIM)
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.SmsEnrollment.ProxyV8.Path)
public class SmsEnrollment extends BaseService {
    private final static Logger logger = LogManager.getLogger(SmsEnrollment.class);

    private static final String badRequestMessage = "400,Premature rejection,Bad Request: Malformed json.";

    @GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("/")
    @ValidatePPCWithJWTV2
    public Response getEnrollmentInfo(@PathParam("ppc") String fsn, @QueryParam("phoneNumber") String phoneNumber) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
        headerMap.put(ApplicationConstants.Requests.Header.contentAccept,
                WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
        headerMap.put(WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersionHeaderKey,
                WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersion);
        headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, MWGApplicationConstants
                .getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.APIM_SMS_ENROLLMENTS_KEY));
        try {
            URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
            builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.enrollmentPath)
                    .setParameter("frequentShopperNumber", fsn).setParameter("phoneNumber", phoneNumber);
            final String requestURI = builder.build().toString();
            final String response = HTTPRequest.executeGet(requestURI, headerMap, VcapProcessor.getApiLowTimeout());
            logger.info("Response from apim: " + response);
            JSONObject responseJson = new JSONObject(response);
            JSONObject responseData = responseJson.getJSONObject("data");

            // Return the phone number payload
            return createValidResponse(responseData.toString());
        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.PROXY_APIM_SMS_ENROLLMENTS);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return createErrorResponse(e);
        }
    }

    /**
     * Create order-based SMS subscription.
     */
    @POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path("/")
    @ValidatePPCWithJWTV2
    public Response registerOrderWithEnrollment(@PathParam("ppc") String fsn, String jsonBody) {
        try {
            String phoneNumber, orderNumber, storeNumber;
            try {
                JSONObject body = new JSONObject(jsonBody);
                phoneNumber = body.getString("phoneNumber");
                orderNumber = body.getString("orderNumber");
                storeNumber = body.getString("storeNumber");
            } catch (JSONException ex) {
                logger.error(
                        "SmsEnrollment$updateEnrollment::Exception -> JSONException creating json object from request payload: "
                                + ex.getMessage());
                throw new Exception(badRequestMessage);
            }

            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
            headerMap.put(ApplicationConstants.Requests.Header.contentAccept,
                    WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
            headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, MWGApplicationConstants
                    .getSystemPropertyValue(WakefernApplicationConstants.VCAPKeys.APIM_SMS_ENROLLMENTS_KEY));

            JSONObject payload = new JSONObject();

            payload.put("storeNumber", storeNumber);
            payload.put("orderNumber", orderNumber);
            payload.put("phoneNumber", phoneNumber);
            payload.put("frequentShopperNumber", fsn);

            URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
            builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.orderSubscriptionPath);
            final String requestURI = builder.build().toString();
            final String response = HTTPRequest.executePostJSON(requestURI, payload.toString(), headerMap, VcapProcessor.getApiLowTimeout());
            return createValidResponse(response);
        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.PROXY_APIM_SMS_ENROLLMENTS);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return createErrorResponse(e);
        }
    }
}
