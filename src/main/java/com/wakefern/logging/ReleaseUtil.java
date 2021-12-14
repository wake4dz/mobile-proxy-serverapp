package com.wakefern.logging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReleaseUtil {
	
	private final static Logger logger = LogManager.getLogger(ReleaseUtil.class);
	
	private static InputStream input = null;
	
	private static BufferedReader reader = null;
	
	private static List<String> releaseLines = new ArrayList<String>();
	
	static {

		try {

			String fileName = "RELEASE_NOTE.properties";
			input = ReleaseUtil.class.getClassLoader().getResourceAsStream(fileName);	
			reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
			
			if (reader == null) {
				logger.error("Sorry, unable to find " + fileName);
			}

			String line = reader.readLine();
			while (line != null) {
				
				if ((line == null) || (line.trim().length() == 0) || (line.trim().startsWith("#"))) {
					//skip populating
				} else {
					releaseLines.add("The current " + line);
				}
				
				// read next line
				line = reader.readLine();
			}
			
			
			if ( ((releaseLines.size() / 3) < 1 ) || ((releaseLines.size() % 3) != 0 ) ) {

				logger.error("releaseList.size() = " + releaseLines.size());
				logger.error("Release info is not in the format of 3 lines");
				
				throw new Exception("Release info is not in the format of 3 lines");
			}
			
			

		} catch (Exception ex) {
			releaseLines.clear();
			
			releaseLines.add("The current release_version = something went wrong...");
			releaseLines.add("The current release_date    = something went wrong...");
			releaseLines.add("The current release_branch  = something went wrong...");
			
			logger.error(LogUtil.getExceptionMessage(ex));
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LogUtil.getExceptionMessage(e);
				}
			}
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					LogUtil.getExceptionMessage(e);
				}
			}
		}

	}
	
	
	public static List<String> getReleaseLines() {	
		return releaseLines;
	}	
	

}
