package com.wakefern.mobileapp.appupdate;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.mobileapp.appupdate.models.AppUpdateInfo;
import com.wakefern.mobileapp.appupdate.models.AppVersion;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * Endpoint to check if an iOS app update is available for the requesting app version.
 */
@Path(WakefernApplicationConstants.MobileApp.AppUpdate.Path)
public class CheckForAppUpdate extends BaseService {
    private final static Logger logger = Logger.getLogger(CheckForAppUpdate.class);

    @GET
    public Response getResponse(@HeaderParam(ApplicationConstants.Requests.Header.appVersion) String appVersion) {
        try {
            logger.debug("Checking for app update, client version: " + appVersion);
            JSONObject responseJson = new JSONObject();
            final AppUpdateInfo updateInfo = checkForAppUpdate(appVersion);

            if (updateInfo != null) {
                responseJson.putOpt("update_available", updateInfo.isUpdateAvailable());
                responseJson.putOpt("store_version", updateInfo.getStoreVersion());
                responseJson.putOpt("release_notes", updateInfo.getReleaseNotes());
            } else {
                responseJson.putOpt("update_available", false);
                responseJson.putOpt("store_version", "");
                responseJson.putOpt("release_notes", "");
            }

            return this.createValidResponse(responseJson.toString());
        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("CheckForAppUpdate$getResponse::Exception",
                    LogUtil.getRelevantStackTrace(e), ApplicationConstants.Requests.Header.appVersion, appVersion);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }

    /**
     * Check if an app update is available for a given app version.
     *
     * @param String clientAppVersionStr
     * @return bool true if update available, false otherwise.
     */
    private static AppUpdateInfo checkForAppUpdate(final String clientAppVersionStr) throws Exception {
        final AppVersion clientAppVersion = AppVersion.parse(clientAppVersionStr);

        //  Make call to itunes and fetch app details.
        String path = WakefernApplicationConstants.MobileApp.AppUpdate.Upstream.BaseURL
                + WakefernApplicationConstants.MobileApp.AppUpdate.Upstream.AppBundleID;
        String response = HTTPRequest.executeGet(path, null, VcapProcessor.getApiLowTimeout());
        logger.debug("Response from iTunes: " + response);

        JSONObject responseJson = new JSONObject(response);
        final int resultCount = responseJson.optInt("resultCount", 0);
        final JSONArray results = responseJson.optJSONArray("results");
        // Use the first result as the latest app
        // TODO: this may not work with A/B tests or rolling out of versions incrementally
        if (resultCount > 0 && results != null && results.length() > 0) {
            JSONObject result = results.getJSONObject(0);
            final String storeVersion = result.optString("version", "");
            final String releaseNotes = result.optString("releaseNotes", "");
            return new AppUpdateInfo(releaseNotes, storeVersion, clientAppVersion);
        }
        return null;
    }
}
