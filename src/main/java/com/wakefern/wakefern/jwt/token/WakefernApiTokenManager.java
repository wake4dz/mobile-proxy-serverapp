package com.wakefern.wakefern.jwt.token;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

public class WakefernApiTokenManager {
    private final static Logger logger = LogManager.getLogger(WakefernApiTokenManager.class);

    private static volatile String sWakefernAPIToken = null;
    private static boolean isRefreshing = false;
    private static final Object WAIT_FOR_NEW_TOKEN_LOCK = new Object();
    private static final Object REFRESH_LOCK = new Object();

    /**
     * Get the current saved token.
     *
     * @return
     */
    public static String getToken() {
        logger.info("Thread requesting token : " + Thread.currentThread().getName());
        synchronized (WAIT_FOR_NEW_TOKEN_LOCK) {
            while (isRefreshing) {
                try {
                    logger.info(Thread.currentThread().getName() + " refresh in progress, waiting...");
                    WAIT_FOR_NEW_TOKEN_LOCK.wait();
                } catch (InterruptedException ex) {
                    logger.error("Exception in wait: " + ex.getMessage());
                }
            }
        }

        logger.info(Thread.currentThread().getName() + " proceeding to check validity...");

        synchronized (REFRESH_LOCK) {
            if (!isValidToken()) {
                logger.info(Thread.currentThread().getName() + " refreshing token...");
                refreshToken();
                logger.info(Thread.currentThread().getName() + " refreshed token.");
            }
        }
        return sWakefernAPIToken;
    }

    /**
     * Validate the existing token.
     *
     * @return true if the token is valid, false otherwise.
     */
    private static boolean isValidToken() {
        if (sWakefernAPIToken == null) {
            return false;
        }
        final long currentUnixTimeMs = System.currentTimeMillis();
        final String jwt = sWakefernAPIToken;
        try {
            String[] split_string = jwt.split("\\.");
            String base64EncodedHeader = split_string[0];
            String base64EncodedBody = split_string[1];

            Base64.Decoder decoder = Base64.getDecoder();
            String header = new String(decoder.decode(base64EncodedHeader));
            logger.debug("JWT Header : " + header);

            String body = new String(decoder.decode(base64EncodedBody));
            logger.debug("JWT body: " + body);

            JSONObject payload = new JSONObject(body);
            long expirationMs = payload.getLong("exp");
            // Expiration check
            if (currentUnixTimeMs > expirationMs) {
                return false;
            }

            // Make sure issuer and sub match
            String issuer = payload.getString("iss");
            if (issuer == null || !issuer.contentEquals("http://www.wakefern.com")) {
                return false;
            }

            String sub = payload.getString("sub");
            return (sub != null && sub.contentEquals("wf_api_user"));
        } catch (Exception e) {
            logger.error("Error validating Wakefern JWT: " + e.getMessage());
        }

        return false;
    }

    /**
     * Refresh the API token.
     */
    private static void refreshToken() {
        logger.info("Thread: " + Thread.currentThread().getName() + " refreshing wakefern token...");
        try {
            isRefreshing = true;

            // This is the JWT token service provided and maintained by Wakefern.
            Map<String, String> wkfn = new HashMap<>();

            String path = WakefernApplicationConstants.JwtToken.BaseURL + WakefernApplicationConstants.JwtToken.authPath;

            wkfn.put(ApplicationConstants.Requests.Headers.contentType, "text/plain");
            wkfn.put(ApplicationConstants.Requests.Headers.Authorization,
                    ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.JWT_PUBLIC_KEY));
            
			// Call APIM Gateway to avoid any foreign IP addresses
			wkfn.put(WakefernApplicationConstants.APIM.sub_key_header, VcapProcessor.getSrMobilePassThruApiKeyProd());

            String response = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiLowTimeout());
            logger.debug("Response from wakefern api: " + response);
            sWakefernAPIToken = response;
        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            sWakefernAPIToken = null;
        }
        isRefreshing = false;
        synchronized (WAIT_FOR_NEW_TOKEN_LOCK) {
            try {
                WAIT_FOR_NEW_TOKEN_LOCK.notifyAll();
            } catch (Exception e) {
                logger.error("Exception in notifyAll: " + e.getClass().toString());
            }
        }
    }
}


