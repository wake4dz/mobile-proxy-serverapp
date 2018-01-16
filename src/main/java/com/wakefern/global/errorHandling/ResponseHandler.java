package com.wakefern.global.errorHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ResponseHandler {
    
	public static String Response(HttpURLConnection connection) {
        try {
            BufferedReader reader;
            StringBuilder sb = new StringBuilder();
            String line;
            
            try {
            		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            } catch (IOException e) {
            		reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }
            
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\r");
            }
            
            reader.close();            
            return sb.toString();
            
        } catch (IOException e) {
           return ExceptionHandler.Exception(e).toString();
        }
    }
}
