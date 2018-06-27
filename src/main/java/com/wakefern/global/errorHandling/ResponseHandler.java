package com.wakefern.global.errorHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseHandler {

	private final static Logger logger = Logger.getLogger("com.wakefern.global.errorHandling.ResponseHandler");
	
	public static String Response(HttpURLConnection connection) {
        BufferedReader reader = null;
        InputStreamReader streamReader = null;
        InputStream stream = null;

        StringBuilder sb = new StringBuilder();
        String line;
        
        try {
	        try {
	        		stream = connection.getInputStream();
	        } catch (IOException e) {
	        		stream = connection.getErrorStream();
	        }
	        
    			streamReader = new InputStreamReader(stream);
            reader = new BufferedReader(streamReader);
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r");
            }
            return sb.toString();
       
        } catch (Exception e) {
        		return "";
        } finally {
        		if(reader != null) {
        			try {
        				reader.close();
        			} catch(Exception readerEx) {
    					logger.log(Level.SEVERE, "[ResponseHandler]::Response::readerException: " + readerEx.getMessage());
    					// no need to throw ex since there is nothing we can do about it, it is very rare anyway
        			}
        		}
        		if(streamReader != null) {
        			try {
        				streamReader.close();
        			} catch(Exception srEx) {
    					logger.log(Level.SEVERE, "[ResponseHandler]::Response::streamReaderException: " + srEx.getMessage());
    					// no need to throw srEx since there is nothing we can do about it, it is very rare anyway
        			}
        		}
        		if(stream != null) {
        			try {
        				stream.close();
        			} catch(Exception strEx) {
    					logger.log(Level.SEVERE, "[ResponseHandler]::Response::streamException: " + strEx.getMessage());
    					// no need to throw strEx since there is nothing we can do about it, it is very rare anyway
        				
        			}
        		}
        }
    }
}
