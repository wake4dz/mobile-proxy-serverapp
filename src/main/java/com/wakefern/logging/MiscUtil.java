package com.wakefern.logging;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

public class MiscUtil {

	public static boolean checkEnvToken(String authToken) {
		if (authToken.trim().equalsIgnoreCase(MWGApplicationConstants.getAppToken())) {
			return true;
		} else {
			return false;
		}
	}
}
