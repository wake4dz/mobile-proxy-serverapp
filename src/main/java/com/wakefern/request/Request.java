package com.wakefern.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.wakefern.Constants;

public class Request {
	public static String executePost(String targetURL, String urlParameters) {
		  HttpURLConnection connection = null;

		  try {
		    //Create connection
		    URL url = new URL(targetURL);
		    connection = (HttpURLConnection) url.openConnection();
		    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		    
		    connection.setRequestProperty("Content-Language", "en-US");
		    connection.setRequestMethod("POST");
		    connection.setRequestProperty("Content-Type", 
		        "application/x-www-form-urlencoded");
		    connection.setRequestProperty("Content-Type", "application/json");
		    
		    if(urlParameters != null){
		    	//Set Content length
		    	connection.setRequestProperty("Content-Length", 
				        Integer.toString(postData.length));
			    connection.setUseCaches(false);
			    connection.setDoOutput(true);
			    connection.setDoInput(true);
			    
			    connection.addRequestProperty(Constants.contentAccept, Constants.headerJson);
			    connection.addRequestProperty(Constants.contentType, Constants.headerJson);
			    connection.addRequestProperty(Constants.contentAuthorization, Constants.authToken);
    
			    //Set JSON as body of request
			    OutputStream oStream = connection.getOutputStream();
			    oStream.write(postData);
			    oStream.close();
		    }    

		    //Connect to the server
		    connection.connect();
		    
		    int status = connection.getResponseCode();
		    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    	StringBuilder sb = new StringBuilder();	
	    	String line;
		    switch(status){
			    case 200:
			    case 201:	
				    sb.append(status);
			    	while( (line = br.readLine()) != null ){
			    		sb.append(line + "\r");
			    	}
			    	br.close();
			    	break;
		    	default:
		    		sb.append(status);
			    	while( (line = br.readLine()) != null ){
			    		sb.append(line + "\r");
			    	}
		    		br.close();
		    }
		    //return body to auth
	    	return sb.toString();
	    	
		  } catch (MalformedURLException ex) {
		        ex.printStackTrace();//("HTTP Client", "Error in http connection" + ex.toString());
		    } catch (IOException ex) {
		    	ex.printStackTrace();//Log.e("HTTP Client", "Error in http connection" + ex.toString());
		    } catch (Exception ex) {
		    	ex.printStackTrace();//Log.e("HTTP Client", "Error in http connection" + ex.toString());
		    } finally {
		        if (connection != null) {
		            try {
		                connection.disconnect();
		            } catch (Exception ex) {
		            	ex.printStackTrace();//Log.e("HTTP Client", "Error in http connection" + ex.toString());
		            }
		        }
		    }
	    return null;
	}
}
