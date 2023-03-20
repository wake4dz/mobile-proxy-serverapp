package com.wakefern.api.proxy.apim.smsenrollment;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;

import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.annotations.ValidatePPCWithJWTV2;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Proxy endpoints for SRFH SMS enrollments APIs V8 (deployed in APIM)
 */
@Path(ApplicationConstants.Requests.Proxy + "/" + WakefernApplicationConstants.SmsEnrollment.ProxyV8.Path)
public class SmsEnrollment extends BaseService {
    private final static Logger logger = LogManager.getLogger(SmsEnrollment.class);

    private static final String badRequestMessage = "400,Premature rejection,Bad Request: Malformed json.";

    @GET
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path("/")
    @ValidatePPCWithJWTV2
    public Response getEnrollmentInfo(@PathParam("ppc") String fsn, @QueryParam("phoneNumber") String phoneNumber) {
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
        headerMap.put(ApplicationConstants.Requests.Headers.Accept,
                WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
        headerMap.put(WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersionHeaderKey,
                WakefernApplicationConstants.SmsEnrollment.Upstream.ApiVersion);
        headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getApimSmsEnrollmentKey());
        
        headerMap.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
        
        try {
            URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
            builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.enrollmentPath)
                    .setParameter("frequentShopperNumber", fsn).setParameter("phoneNumber", phoneNumber);
            final String requestURI = builder.build().toString();
            final String response = HTTPRequest.executeGet(requestURI, headerMap, EnvManager.getApiLowTimeout());
            logger.debug("Response from apim: " + response);
            JSONObject responseJson = new JSONObject(response);
            JSONObject responseData = responseJson.getJSONObject("data");

            // Return the phone number payload
            return createValidResponse(responseData.toString());
        } catch (Exception e) {
        	if (LogUtil.isLoggable(e)) {
	            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
	            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
        	}
        	
            return createErrorResponse(e);
        }
    }

    /**
     * Create order-based SMS subscription.
     */
    @POST
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
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
            headerMap.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
            headerMap.put(ApplicationConstants.Requests.Headers.Accept,
                    WakefernApplicationConstants.SmsEnrollment.Upstream.MimeType);
            headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getApimSmsEnrollmentKey());

            JSONObject payload = new JSONObject();

            payload.put("storeNumber", storeNumber);
            payload.put("orderNumber", orderNumber);
            payload.put("phoneNumber", phoneNumber);
            payload.put("frequentShopperNumber", fsn);

            URIBuilder builder = new URIBuilder(WakefernApplicationConstants.SmsEnrollment.Upstream.BaseURL);
            builder.setPath(WakefernApplicationConstants.SmsEnrollment.Upstream.orderSubscriptionPath);
            final String requestURI = builder.build().toString();
            final String response = HTTPRequest.executePostJSON(requestURI, payload.toString(), headerMap, EnvManager.getApiLowTimeout());
            return createValidResponse(response);
        } catch (Exception e) {
        	if (LogUtil.isLoggable(e)) {
	            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
	            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
        	}
        	
            return createErrorResponse(e);
        }
    }
}
