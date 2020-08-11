package com.wakefern.global.middleware;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.annotations.AppVersions;
import com.wakefern.mobileapp.appupdate.models.AppVersion;
import org.apache.log4j.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Middleware (Request Filter) to allow to define a filter of app version(s) requests.
 */
@Provider
@AppVersions
public class AppVersionFilter implements ContainerRequestFilter {
    private final static Logger logger = Logger.getLogger(AppVersionFilter.class);

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(final ContainerRequestContext requestContext) {
        // Read the "AppVersion" header value
        String appVersionStr = requestContext.getHeaders().getFirst(ApplicationConstants.Requests.Header.appVersion);
        logger.debug("AppVersion filter, requesting version: " + appVersionStr);

        // Get the annotated resource method and filter according to the annotation values.
        Method method = resourceInfo.getResourceMethod();

        if (method != null) {
            AppVersions annotation = method.getAnnotation(AppVersions.class);
            boolean hasAppVersionHeader = appVersionStr != null && !appVersionStr.trim().equalsIgnoreCase("");

            // If the header is not required and the incoming request doesn't have a header, proceed.
            if (!annotation.requireHeader() && !hasAppVersionHeader) {
                logger.debug("Header not required, missing AppVersion Header, proceeding...");
                return;
            } else if (annotation.requireHeader() && !hasAppVersionHeader) {
                logger.debug("Rejecting request, missing required AppVersion header");
                throw new BadRequestException("Missing required header");
            }

            AppVersion minVersion = null, maxVersion = null;
            AppVersion clientAppVersion;
            ArrayList<AppVersion> acceptedVersions = new ArrayList<>();

            // read the method's annotation and parse (min, max, set) versions.
            try {
                clientAppVersion = AppVersion.parse(appVersionStr);

                if (!annotation.minVersion().isEmpty()) {
                    minVersion = AppVersion.parse(annotation.minVersion());
                }

                if (!annotation.maxVersion().equalsIgnoreCase("current")) {
                    maxVersion = AppVersion.parse(annotation.maxVersion());
                }

                if (annotation.versionsList().length > 0) {
                    for (String versionStr : annotation.versionsList()) {
                        acceptedVersions.add(AppVersion.parse(versionStr));
                    }
                }
            } catch (Exception e) {
                logger.error(e);
                throw new ServiceUnavailableException();
            }

            // filter based on versions list or min/max combination
            if (acceptedVersions.size() > 0) {
                boolean found = false;
                for (AppVersion appVersion : acceptedVersions) {
                    if (clientAppVersion.compareTo(appVersion) == 0) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    throw new BadRequestException("Invalid app version");
                }
            } else if (minVersion != null || maxVersion != null) {
                if (minVersion != null && clientAppVersion.compareTo(minVersion) < 0) {
                    throw new BadRequestException("Invalid app version");
                }

                if (maxVersion != null && clientAppVersion.compareTo(maxVersion) > 0) {
                    throw new BadRequestException("Invalid app version");
                }
            }

            logger.debug("proceed app version: " + appVersionStr + " - " + resourceInfo.getResourceMethod().getName());
        }
    }
}
