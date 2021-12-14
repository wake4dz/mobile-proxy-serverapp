package com.wakefern.global.errorHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResponseHandler {

	private final static Logger logger = LogManager.getLogger(ResponseHandler.class);
	
	public static String getResponse(HttpURLConnection connection) throws Exception {
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
                sb.append(line + "\r");  // this '/r' will create an extra blank line in the top-level REST code in log output
            }
            return sb.toString();
       
        } catch (Exception e) {
        	// As discussed with Loi and Mark on 2018-07-26, we decided to re-visit return "" in the future.
        	//        
        	// 2018-07-27 update:
        	//  Did some test with line #31 above, it seems fine for return "" because HTTPRequest.java on lines 396-407 would take care of it.
        	//  But lines# 396-397 with error (originally it was INFO level) log may be redundant since line #419 would log it.
            // 	
            //  Same goes for lines #102-112, 109-119, 
        	logger.error("[ResponseHandler]::Response::Exception: " + e.getMessage());
        	return "";
        } finally {
    		if(reader != null) {
    			try {
    				reader.close();
    			} catch(Exception readerEx) {
    				logger.error("[ResponseHandler]::Response::readerException: " + readerEx.getMessage());
					// no need to throw ex since there is nothing we can do about it, it is very rare anyway
    			}
    		}
    		if(streamReader != null) {
    			try {
    				streamReader.close();
    			} catch(Exception srEx) {
    				logger.error("[ResponseHandler]::Response::streamReaderException: " + srEx.getMessage());
					// no need to throw srEx since there is nothing we can do about it, it is very rare anyway
    			}
    		}
    		if(stream != null) {
    			try {
    				stream.close();
    			} catch(Exception strEx) {
    				logger.error("[ResponseHandler]::Response::streamException: " + strEx.getMessage());
					// no need to throw strEx since there is nothing we can do about it, it is very rare anyway
    				
    			}
    		}
        }
    }
}
