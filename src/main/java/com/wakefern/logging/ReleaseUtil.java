package com.wakefern.logging;

import java.util.ArrayList;
import java.util.List;

public class ReleaseUtil {
	private static List<Release> releaseList = null;
	
	static {
	    releaseList = new ArrayList<Release>();

		Release release3_x = new Release();
		release3_x.setReleaseLevel("3.x");
		release3_x.setReleaseDate("2018-07-13");
		release3_x.setReleaseDescription("Starting release 4.0, the backend app would start tracking the release info");
		releaseList.add(release3_x);
		
		Release release4_0 = new Release();
		release4_0.setReleaseLevel("4.0");
		release4_0.setReleaseDate("2018-10");
		release4_0.setReleaseDescription("Rewrite the logging framework processes with log4j");
		releaseList.add(release4_0);

	}
	
	public static List<Release> getReleases() {	
		return releaseList;
	}
}
