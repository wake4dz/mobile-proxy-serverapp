package com.wakefern.global.middleware;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.mobileapp.appupdate.models.AppVersion;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

/**
 * Filter to map certain client http error codes (4XX) to other status codes
 * that can be used by the ShopRite mobile app client.
 * <p>
 * This is primarily due to MI9 building endpoints that don't follow the HTTP spec
 * for certain status codes (namely 403).
 * <p>
 * Some endpoints will return a 403 status code when 401 (Unauthorized) is more appropriate,
 * and the user's session is actually expired...
 */
@Provider
public class ClientErrorRewriteFilter implements ContainerResponseFilter {
	private static final Logger sLogger = Logger.getLogger(ClientErrorRewriteFilter.class.getSimpleName());

	private static final AppVersion sLastSupportedAppVersion;
	private static final String LAST_SUPPORTED_APP_VERSION_STR = "4.13.0";

	/**
	 * Http status code (429) TooManyRequests
	 */
	private static final int SC_TOO_MANY_REQUESTS = 429;

	static {
		AppVersion appVersion;
		try {
			appVersion = AppVersion.parse(LAST_SUPPORTED_APP_VERSION_STR);
		} catch (Exception e) {
			appVersion = null;
		}
		sLastSupportedAppVersion = appVersion;
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
		// AppVersion header required in request.
		if (requestContext.getHeaderString(ApplicationConstants.Requests.Header.appVersion) == null) {
			sLogger.debug("Skipping 403 to 401 conversion on request missing AppVersion header");
			return;
		}
		final Response.StatusType statusType = responseContext.getStatusInfo();
		sLogger.debug("Checking outgoing response: " + responseContext.getStatus());

		// check if the outgoing response status code is 403.
		if (statusType != null && statusType.getStatusCode() == HttpStatus.SC_FORBIDDEN) {
			AppVersion parsedAppVersion;
			try {
				parsedAppVersion = AppVersion.parse(requestContext.getHeaderString(ApplicationConstants.Requests.Header.appVersion));
			} catch (Exception ex) {
				sLogger.debug("Error parsing client request app version: " + ex.getMessage());
				parsedAppVersion = null;
			}
			final AppVersion clientAppVersion = parsedAppVersion;

			// Skip 403 to 401 conversion if the AppVersion header is newer than last supported app version.
			if (clientAppVersion == null  || clientAppVersion.compareTo(sLastSupportedAppVersion) > 0) {
				sLogger.debug("Skipping 403 to 401 conversion on app version greater than " + sLastSupportedAppVersion + " or unknown app version");
				return;
			}
			if (responseContext.hasEntity()) {
				final Object responseEntity = responseContext.getEntity();
				final String entityString = responseEntity.toString();

				// Check if the response body contains ratelimit message from MI9
				final boolean isRatelimitErrorMessage = responseContainsRatelimitError(entityString);

				// For app versions less than 4.13.0
				// This will hopefully be sunsetted once older versions of the app are no longer supported.
				if (isRatelimitErrorMessage && clientAppVersion.compareTo(sLastSupportedAppVersion) < 0) {
					responseContext.setStatus(SC_TOO_MANY_REQUESTS);
				} else if (!isRatelimitErrorMessage) {
					responseContext.setStatus(HttpStatus.SC_UNAUTHORIZED);
				}
			}
		}
	}

	/**
	 * Parse the response body of the proxied request to determine if this is a ratelimited response.
	 *
	 * @param entityString String
	 * @return boolean
	 */
	private static boolean responseContainsRatelimitError(String entityString) {
		try {
			final JSONArray errorMsgArray = new JSONArray(entityString);
			final int messagesSize = errorMsgArray.length();
			for (int i = 0; i < messagesSize; i++) {
				if (errorMsgArray.get(i) instanceof JSONObject) {
					JSONObject errorObj = errorMsgArray.getJSONObject(i);
					if (errorObj.getString("Code").equalsIgnoreCase(MWGApplicationConstants.ErrorCodes.TOO_MANY_REQUESTS)) {
						return true;
					}
				}
			}
		} catch (Exception ex) {
			sLogger.debug("Exception parsing ratelimit response: " + ex.getMessage());
		}
		return false;
	}
}
