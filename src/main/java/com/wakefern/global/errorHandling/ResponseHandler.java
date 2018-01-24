package com.wakefern.global.errorHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ResponseHandler {
    
	public static String Response(HttpURLConnection connection) {
        BufferedReader reader;
        InputStreamReader streamReader;
        InputStream stream;

        StringBuilder sb = new StringBuilder();
        String line;
        
        try {
        		stream = connection.getInputStream();
        } catch (IOException e) {
        		stream = connection.getErrorStream();
        }
        
        try {
    			streamReader = new InputStreamReader(stream);
            reader = new BufferedReader(streamReader);
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r");
            }
            
            reader.close();            
            return sb.toString();
       
        } catch (Exception e) {
        		return "";
        } 
    }
}
