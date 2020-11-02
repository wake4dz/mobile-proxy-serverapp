package com.wakefern.mobileapp.appupdate.models;

import org.apache.log4j.Logger;

/**
 * Pojo for App Version
 */
public final class AppVersion implements Comparable<AppVersion> {
    private static Logger logger = Logger.getLogger(AppVersion.class.getName());
    private AppVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    /**
     * Factory method to parse app version header strings into POJO
     * @param appVersionStr
     * @return
     * @throws Exception if the app version header cannot be parsed.
     */
    public static AppVersion parse(final String appVersionStr) throws Exception {
        try {
            String[] split = appVersionStr.split("\\.");
            int major = Integer.parseInt(split[0]);
            int minor = Integer.parseInt(split[1]);
            int patch = Integer.parseInt(split[2]);
            return new AppVersion(major, minor, patch);
        } catch (Exception ex) {
            logger.error("Exception parsing app version string: " + appVersionStr + " exception: " + ex.getMessage());
            throw ex;
        }
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public int compareTo(AppVersion o) {
        if (o == null) {
            return 1;
        }
        if (o.major > major) {
            return -1;
        }
        else if (o.major < major) {
            return 1;
        }

        if (o.minor > minor) {
            return -1;
        }
        else if (o.minor < minor) {
            return 1;
        }

        if (o.patch > patch) {
            return -1;
        }
        else if (o.patch < patch) {
            return 1;
        }
        return 0;
    }

    private final int major;
    private final int minor;
    private final int patch;
}