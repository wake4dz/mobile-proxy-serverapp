package com.wakefern.request;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.ErrorHandling.ResponseHandler;


public class HTTPRequest {
	

	private final static Logger logger = Logger.getLogger("HTTPRequest");
	private static int timeOutInt = 20000; // 20 seconds time out
	
    public static String executePost(String requestType,String requestURL, String requestParameters, String requestBody,
                                     Map<String, String> requestHeaders, int timeOut) throws Exception{
        HttpURLConnection connection = null;
    	long startTime, endTime;
        startTime = System.currentTimeMillis();
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
                timeOut = timeOutInt;
                connection.setConnectTimeout(timeOut);
                connection.setReadTimeout(timeOut);

                for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }

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
                case 204:
                    //sb.append(status);
                    while( (line = br.readLine()) != null ){
                        sb.append(line + "\r");
                    }
                    br.close();
                    break;
                default:
                    //sb.append(status);
                    throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
            }
            endTime = System.currentTimeMillis();
            
            logger.log(Level.INFO, "[executePost]::Total process time: {0} ms, path: {1}", new Object[]{(endTime-startTime), requestURL});
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
        	logger.log(Level.SEVERE, "[executePost]::MalformedURLException, path: ", requestURL);
            throw ex;
        } catch (IOException ex) {
        	logger.log(Level.SEVERE, "[executePost]::IOException, path: ", requestURL);
            throw ex;
        } catch (Exception ex) {
        	logger.log(Level.SEVERE, "[executePost]::Exception, path: {0}, message: ", new Object[]{requestURL, ex.getMessage()});
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                	logger.log(Level.SEVERE, "[executePost]::Exception closing connection, path: ", requestURL);
                    throw ex;
                }
            }
        }
    }

    public static String executePostJSON(String requestURL, String requestBody, Map<String, String> requestHeaders, int timeOut) throws Exception{
        HttpURLConnection connection = null;
    	long startTime, endTime;

        try {
        	startTime = System.currentTimeMillis();
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
                timeOut = timeOutInt;
                connection.setConnectTimeout(timeOut);
                connection.setReadTimeout(timeOut);

                for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }

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

            switch(status){
                case 200:
                case 201:
                case 204:
                    //sb.append(status);
                    int read;
                    char[] chars = new char[1024];
                    while( (read = br.read(chars)) != -1 ){
                        sb.append(chars, 0, read);
                    }
                    br.close();
                    break;
                default:
                    //sb.append(status);
                    throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
            }
            endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "[executePostJSON]::Total process time: {0} ms, path: {1}", new Object[]{(endTime-startTime), requestURL});
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
        	logger.log(Level.SEVERE, "[executePostJSON]::MalformedURLException, path: ", requestURL);
            throw ex;
        } catch (IOException ex) {
        	logger.log(Level.SEVERE, "[executePostJSON]::IOException, path: ", requestURL);
            throw ex;
        } catch (Exception ex) {
        	logger.log(Level.SEVERE, "[executePostJSON]::Exception, path: {0}, message: ", new Object[]{requestURL, ex.getMessage()});
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                	logger.log(Level.SEVERE, "[executePostJSON]::Exception closing connection, path: ", requestURL);
                    throw ex;
                }
            }
        }
    }

    public static String executePut(String requestType,String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders
            , int timeOut) throws Exception {
        HttpURLConnection connection = null;
    	long startTime, endTime;

        try {
        	startTime = System.currentTimeMillis();
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");

            if(requestBody != null){
                //Set Content length
                connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                timeOut = timeOutInt;
                connection.setConnectTimeout(timeOut);
                connection.setReadTimeout(timeOut);

                for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                    connection.addRequestProperty(entry.getKey(), entry.getValue());
                }

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

            switch(status){
                case 200:
                case 201:
                case 204:
                    //sb.append(status);
                    int read;
                    char[] chars = new char[1024];
                    while( (read = br.read(chars)) != -1 ){
                        sb.append(chars, 0, read);
                    }
                    br.close();
                    break;
                default:
                    throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
            }
            endTime= System.currentTimeMillis();
            logger.log(Level.INFO, "[executePut]::Total process time: {0} ms, path: {1}", new Object[]{(endTime-startTime), requestURL});
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
        	logger.log(Level.SEVERE, "[executePut]::MalformedURLException, path: ", requestURL);
            throw ex;
        } catch (IOException ex) {
        	logger.log(Level.SEVERE, "[executePut]::IOException, path: ", requestURL);
            throw ex;
        } catch (Exception ex) {
        	logger.log(Level.SEVERE, "[executePut]::Exception, path: {0}, message: ", new Object[]{requestURL, ex.getMessage()});
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                	logger.log(Level.SEVERE, "[executePut]::Exception closing connection, path: ", requestURL);
                    throw ex;
                }
            }
        }
    }

    public static String executeGet(String requestURL, Map<String, String> requestHeaders, int timeOut) throws Exception{
        return executeRequest(requestURL,requestHeaders,null,"GET", timeOut);
    }

    public static String executeRequest(String requestURL,Map<String,String> requestHeaders,Map<String,String>
            requestParameters,String requestMethod, int timeOut) throws Exception{
        HttpURLConnection connection = null;
    	long startTime, endTime;
    	
        try {
        	startTime = System.currentTimeMillis();
            connection = createConnection(requestURL,requestHeaders,requestParameters,requestMethod, timeOut);
            int responseCode = connection.getResponseCode();
            endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "[executeRequest]::Total process time: {0} ms, path: {1}", new Object[]{(endTime-startTime), requestURL});

            if(responseCode == 200 || responseCode == 201 || responseCode == 204 || responseCode == 205 || responseCode == 206){
                return buildResponse(connection);
            } else {
                //System.out.print("Response " + buildResponse(connection));
                //System.out.print("Connection URL " + connection.getURL());
                //System.out.print("Response Message " + connection.getResponseMessage());
                ////System.out.print("Request Method " + connection.getReq);
//                for(Map.Entry<String, List<String>> entry: connection.getRequestProperties().entrySet()){
//                        //System.out.print(entry.getKey().toString() + ": " + entry.getValue().toString());
//                }

                //System.out.print("A");
//                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    sb.append(line + "\r");
//                }
//                br.close();
//                //System.out.print("Input Stream " + line);
//                //System.out.print("B");


//                BufferedReader br2 = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
//                StringBuilder sb2 = new StringBuilder();
//                String line2;
//                while ((line2 = br2.readLine()) != null) {
//                    sb2.append(line2 + "\r");
//                }
//                br2.close();

                //System.out.print("Error Stream " + line2);
                //System.out.print("C");

                throw new Exception(responseCode + "," + connection.getResponseMessage());
            }
        } catch (IOException e) {
        	logger.log(Level.SEVERE, "[executeRequest]::IOException, path: ", requestURL);
            throw e;
        } catch (URISyntaxException e) {
        	logger.log(Level.SEVERE, "[executeRequest]::URISyntaxException, path: ", requestURL);
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                	logger.log(Level.SEVERE, "[executeRequest]::Exception closing connection, path: ", requestURL);
                    throw ex;
                }
            }
        }
    }

    public static String executeGetJSON(String requestURL, Map<String, String> requestHeaders, int timeOut) throws Exception{
        return executeRequest(requestURL,requestHeaders,null,"GET", timeOut);
    }

    public static String executeDelete(String requestURL, Map<String, String> requestHeaders, int timeOut) throws Exception {
        HttpURLConnection connection = null;
    	long startTime, endTime;

        try {
        	startTime = System.currentTimeMillis();
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            timeOut = timeOutInt;
            connection.setConnectTimeout(timeOut);
            connection.setReadTimeout(timeOut);

            for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            
            endTime = System.currentTimeMillis();
            logger.log(Level.INFO, "[executeDelete]::Total process time: {0} ms, path: {1}", new Object[]{(endTime-startTime), requestURL});
            
            switch(status){
                case 200:
                case 201:
                case 204:
                    return status + " Success";
                default:
                    throw new Exception(connection.getResponseCode() + "," + connection.getResponseMessage());
            }
        } catch (MalformedURLException ex) {
        	logger.log(Level.SEVERE, "[executeDelete]::MalformedURLException, path: ", requestURL);
            throw ex;
        } catch (IOException ex) {
        	logger.log(Level.SEVERE, "[executeDelete]::IOException, path: ", requestURL);
            throw ex;
        } catch (Exception ex) {
        	logger.log(Level.SEVERE, "[executeDelete]::Exception, path: {0}, message: ", new Object[]{requestURL, ex.getMessage()});
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                	logger.log(Level.SEVERE, "[executeDelete]::Exception closing connection, path: ", requestURL);
                    throw ex;
                }
            }
        }
    }
/*
    Private Method Section --
 */

    private static HttpURLConnection createConnection(String requestURL,Map<String,String> requestHeaders,Map<String,String> requestParameters, String requestMethod, int timeOut) throws IOException, URISyntaxException {
        HttpURLConnection connection = null;
        URI uri = new URI(requestURL);
        if(requestParameters!=null) {
            for (Map.Entry<String, String> entry : requestParameters.entrySet()) {
                uri = appendUri(uri.toString(), entry.getKey() + "=" + entry.getValue());
            }
        }

        URL url = uri.toURL();
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod);
        if(timeOut == 0){
        	timeOut = timeOutInt;
        }
        connection.setConnectTimeout(timeOut);
        connection.setReadTimeout(timeOut);


        if(requestHeaders != null) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        //Connect to the server
        connection.connect();

        return connection;
    }

    public static URI appendUri(String uri, String appendQuery) throws URISyntaxException {
        URI oldUri = new URI(uri);

        String newQuery = oldUri.getQuery();
        if (newQuery == null) {
            newQuery = appendQuery;
        } else {
            newQuery += "&" + appendQuery;
        }

        URI newUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                oldUri.getPath(), newQuery, oldUri.getFragment());

        return newUri;
    }

    private static String buildResponse(HttpURLConnection connection){
        return ResponseHandler.Response(connection);
    }
}
