package com.wakefern.mobileapp.appupdate.models;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public final class AppUpdateInfo {
    private static final Logger logger = LogManager.getLogger(AppUpdateInfo.class);

    /**
     * App Store release notes
     */
    private final String releaseNotes;

    /**
     * App store app version
     */
    private final String storeVersion;

    /**
     * Flag for whether an update is available for the given client.
     */
    private boolean updateAvailable;

    public AppUpdateInfo(String releaseNotes, String storeVersion, AppVersion clientVersion) {
        this.releaseNotes = releaseNotes;
        this.storeVersion = storeVersion;

        setIsUpdateAvailable(clientVersion);
    }

    public String getStoreVersion() {
        return storeVersion;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    /**
     * Determine if an update is available given a client version.
     * @param clientVersion
     */
    private void setIsUpdateAvailable(AppVersion clientVersion) {
        try {
            final AppVersion release = AppVersion.parse(storeVersion);
            updateAvailable = release.compareTo(clientVersion) > 0;
        } catch (Exception e) {
            logger.error("Exception determining app update: " + e.getMessage());
        }
    }
}