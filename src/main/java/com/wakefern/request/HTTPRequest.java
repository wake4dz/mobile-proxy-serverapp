package com.wakefern.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.wakefern.global.ApplicationConstants;

public class HTTPRequest {
	public static String executePost(String requestType,String requestURL, String requestParameters, String requestBody, String requestHeaders) {
		  HttpURLConnection connection = null;

		  try {
		    //Create connection
		    URL url = new URL(requestURL);
		    connection = (HttpURLConnection) url.openConnection();
		    connection.setRequestMethod("POST");

		    if(requestBody != null){
		    	//Set Content length
		    	connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
			    connection.setUseCaches(false);
			    connection.setDoOutput(true);
			    connection.setDoInput(true);
			    
			    connection.addRequestProperty(ApplicationConstants.Requests.Header.contentAccept, 
			    			ApplicationConstants.jsonResponseType);
			    connection.addRequestProperty(ApplicationConstants.Requests.Header.contentType, 
			    			ApplicationConstants.jsonAcceptType);
			    connection.addRequestProperty(ApplicationConstants.Requests.Header.contentAuthorization,
			    		ApplicationConstants.authToken);
    
			    //Set JSON as body of request
			    OutputStream oStream = connection.getOutputStream();
			    oStream.write(requestBody.getBytes("UTF-8"));
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
