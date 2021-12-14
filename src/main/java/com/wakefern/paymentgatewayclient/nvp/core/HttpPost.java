/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URLConnection;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import com.wakefern.paymentgatewayclient.nvp.domain.Constants;
import com.wakefern.paymentgatewayclient.nvp.util.LogToFile;
import com.wakefern.paymentgatewayclient.nvp.util.Validator;

/**
 * Class for sending request using HTTP POST method and returning response.
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015
 */
final class HttpPost implements Transport {
	
	private static final long serialVersionUID = 3534076801303923906L;

	/**
	 * Sends request (requestMsg attribute) to the specified URL and returns response as a string
	 *
	 * @param	urlString:	URL where to send the request
	 * @param	requestMsg:	request message to be sent
	 * @return	response message
	 * @throws	MalformedURLException
	 * @throws  IOException
	 */
	public String getResponse(final String urlString, final String requestMsg)
												throws MalformedURLException {
		String methodName = "getResponse(...)";
		
		// URL validation...
		if (urlString != null && (urlString.indexOf("https://dev-pc") >= 0 
									    || urlString.indexOf("https://localhost") >= 0)) {
			//--- Bypassing Localhost
		} else {
			if (urlString == null || !Validator.isValidURL(urlString)) {
				return methodName + ": Failed due to URL for POST is invalid";
			}
		}
		
		if (requestMsg == null || requestMsg.trim().length() < 3) {
			return methodName + ": Failed due to request message is invalid";
		}
		
		// Creating the URL object from the urlString 
		StringBuffer response = new StringBuffer();
        try {
        	LogToFile.dateLog(HttpPost.class.getName() + methodName + ": "
									+ "Opening connection to URL=" + urlString);
			URL url = new URL(urlString);
			HttpURLConnection conn;
        	conn = (HttpURLConnection) url.openConnection();      
            conn.setDoOutput(true); 
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", Constants.CONTENT_TYPE); 
            conn.setRequestProperty("charset", Constants.ENCODING);

            // Write the request onto the end-point URL 
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
        	LogToFile.dateLog(HttpPost.class.getName() + methodName + ": "
        							+ "POSTing the request to URL...");
            writer.write(requestMsg);
            writer.flush();
            writer.close();

            // Read the response 
        	LogToFile.dateLog(HttpPost.class.getName() + methodName + ": "
												+ "Reading the Response from the POST...");
            BufferedReader bufReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = bufReader.readLine()) != null) {
                response.append(line);
            }
            bufReader.close(); 

        } catch(MalformedURLException ex) {
        	LogToFile.dateLog(HttpPost.class.getName() + methodName + ": "
									+ "MalformedURLException was thrown - " + ex.getMessage());
        } catch (IOException ex) {
            //LogManager.getLogger(HttpPost.class.getName()).log(Level.SEVERE, null, ex);
        	LogToFile.dateLog(HttpPost.class.getName() + methodName + ": "
        							+ "IOException was thrown - " + ex.getMessage());
        }
		// Return the response to the caller in String format
    	LogToFile.dateLog(HttpPost.class.getName() + methodName 
    								+ ": The response from the POST was - "	+ response.toString());
		return response.toString();
	}

    @Override
    public String toString() {
        return "Instance of HttpPost class";
    }
}
