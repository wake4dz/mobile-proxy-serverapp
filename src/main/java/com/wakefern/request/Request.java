package com.wakefern.request;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

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
//		    connection.setRequestProperty("Content-Type", 
//		        "application/x-www-form-urlencoded");
		    connection.setRequestProperty("Content-Type", "application/json");
		    connection.setRequestProperty("Accept", "application/json");
		    if(urlParameters != null){
		    	//Set Content length
		    	connection.setRequestProperty("Content-Length", 
				        Integer.toString(postData.length));
			    connection.setUseCaches(false);
			    connection.setDoOutput(true);
			    connection.setDoInput(true);
			    
			    //Set JSON as body of request
			    OutputStream oStream = connection.getOutputStream();
			    oStream.write(postData);
			    oStream.close();
		    }    
//			    connection.setRequestProperty("charset", "UTF-8");
//			    connection.addRequestProperty(key, value);
		    
		    //Connect to the server
		    connection.connect();
		    
		    int status = connection.getResponseCode();
		    switch(status){
			    case 200:
			    case 201:
			    	BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			    	StringBuilder sb = new StringBuilder();
			    	String line = null;
			    	while( (line = br.readLine()) != null ){
			    		sb.append(line + "\n");
			    	}
			    	br.close();
			    	
			    	//return body to auth
			    	return sb.toString();
		    }
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
		   
/*
		    //Send request
		    DataOutputStream wr = new DataOutputStream (
		        connection.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.close();

		    //Get Response  
		    InputStream is = connection.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
		    String line;
		    while ((line = rd.readLine()) != null) {
		      response.append(line);
		      response.append('\r');
		    }
			// return body to auth
			rd.close();
			return response.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
*/
	}
}
