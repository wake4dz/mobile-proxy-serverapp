package com.wakefern.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.Map;

import com.wakefern.global.ErrorHandling.ExceptionHandler;
import com.wakefern.global.ErrorHandling.ResponseHandler;


public class HTTPRequest {
    public static String executePost(String requestType,String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders) throws Exception{
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
                    //sb.append(status);
                    while( (line = br.readLine()) != null ){
                        sb.append(line + "\r");
                    }
                    br.close();
                    break;
                default:
                    //sb.append(status);
                    while( (line = br.readLine()) != null ){
                        sb.append(line + "\r");
                    }
                    br.close();
            }
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }

    public static String executePostJSON(String requestURL, String requestBody, Map<String, String> requestHeaders) throws Exception{
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
                    while( (line = br.readLine()) != null ){
                        sb.append(line + "\r");
                    }
                    br.close();
            }
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }

    public static String executePut(String requestType,String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders) throws Exception {
        HttpURLConnection connection = null;

        try {
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
                    while( (line = br.readLine()) != null ){
                        sb.append(line + "\r");
                    }
                    br.close();
            }
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }

    public static String executeGet(String requestURL, Map<String, String> requestHeaders) throws Exception{
        return executeRequest(requestURL,requestHeaders,null,"GET");
    }

    public static String executeRequest(String requestURL,Map<String,String> requestHeaders,Map<String,String>
            requestParameters,String requestMethod) throws Exception{
        HttpURLConnection connection = null;
        try {
            connection = createConnection(requestURL,requestHeaders,requestParameters,requestMethod);
            return buildResponse(connection);
        } catch (IOException e) {
            throw e;
        } catch (URISyntaxException e) {
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }

    public static String executeGetJSON(String requestURL, Map<String, String> requestHeaders) throws Exception{
        return executeRequest(requestURL,requestHeaders,null,"GET");
    }

    public static String executeDelete(String requestURL, Map<String, String> requestHeaders) throws Exception {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            switch(status){
                case 200:
                case 201:
                case 204:
                    return status + " Success";
                default:
                    Exception e = new Exception("Non-20x response");
                    throw e;
            }
        } catch (MalformedURLException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    throw ex;
                }
            }
        }
    }
/*
    Private Method Section --
 */

    private static HttpURLConnection createConnection(String requestURL,Map<String,String> requestHeaders,Map<String,String> requestParameters,String requestMethod) throws IOException, URISyntaxException {
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
