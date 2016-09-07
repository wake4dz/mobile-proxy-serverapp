package com.wakefern.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.wakefern.global.ApplicationConstants;

public class HTTPRequest {
    public static String executePost(String requestType,String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders) {
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

    public static String executePostJSON(String requestURL, String requestBody, Map<String, String> requestHeaders) {
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

    public static String executePut(String requestType,String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders) {
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

    public static String executeGet(String requestURL, Map<String, String> requestHeaders){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                connection.addRequestProperty(entry.getKey(), entry.getValue());
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

    public static String executeGetJSON(String requestURL, Map<String, String> requestHeaders){
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            for(Map.Entry<String, String> entry: requestHeaders.entrySet()){
                connection.addRequestProperty(entry.getKey(), entry.getValue());
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

    public static String executeDelete(String requestURL, Map<String, String> requestHeaders){
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
                    return status + " Failed to delete";
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
    }
}
