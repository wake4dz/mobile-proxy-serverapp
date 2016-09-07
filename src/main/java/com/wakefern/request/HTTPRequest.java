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
    public static String executePost(String requestType, String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            if (requestBody != null) {
                //Set Content length
                connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
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
            String statusText = connection.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
//            if (statusText != null) {
//                sb.append(status + " " + statusText + "\r");
//            } else {
//                sb.append(status + "\r");
//            }
            sb.append(read(sb, br));
            br.close();
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    public static String executePostJSON(String requestURL, String requestBody, Map<String, String> requestHeaders) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            if (requestBody != null) {
                //Set Content length
                connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
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
            String statusText = connection.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            switch (status) {
                case 200:
                case 201:
                case 204:
                case 206:
                case 400:
                case 401:
                case 403:
                case 405:
                case 409:
                case 415:
                case 424:
                    //sb.append(status + " " + statusText + "\r");
                    sb.append(readJson(sb, br));
                    br.close();
                    break;
                default:
                    sb.append(status + "\r");
                    sb.append(read(sb, br));
                    br.close();
            }
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    public static String executePut(String requestType, String requestURL, String requestParameters, String requestBody, Map<String, String> requestHeaders) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");

            if (requestBody != null) {
                //Set Content length
                connection.setRequestProperty("Content-length", requestBody.getBytes().length + "");
                connection.setUseCaches(false);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
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
            String statusText = connection.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            switch (status) {
                case 200:
                case 201:
                case 204:
                case 206:
                case 400:
                case 401:
                case 403:
                case 405:
                case 409:
                case 415:
                case 424:
                    //sb.append(status + " " + statusText + "\r");
                    sb.append(readJson(sb, br));
                    br.close();
                    break;
                default:
                    sb.append(status + "\r");
                    sb.append(read(sb, br));
                    br.close();
                    br.close();
            }
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    public static String executeGet(String requestURL, Map<String, String> requestHeaders) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            //Connect to the server
            connection.connect();

            System.out.print("Headers: \r");
            System.out.print(connection.getHeaderFields());
            int status = connection.getResponseCode();
            String statusText = connection.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();

//            if (statusText != null) {
//                sb.append(status + " " + statusText + "\r");
//            } else {
//                sb.append(status + "\r");
//            }
            sb.append(read(sb, br));
            br.close();

            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (IOException ex) {
            System.out.print("Headers: \r ");
            System.out.print(connection.getHeaderFields());
            ex.printStackTrace();
            return ex.getMessage() + " " + connection.getHeaderFields();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    public static String executeGetJSON(String requestURL, Map<String, String> requestHeaders) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            String statusText = connection.getResponseMessage();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            switch (status) {
                case 200:
                case 201:
                case 204:
                case 206:
                case 400:
                case 401:
                case 403:
                case 405:
                case 409:
                case 415:
                case 424:
                    //sb.append(status + " " + statusText + "\r");
                    sb.append(readJson(sb, br));
                    br.close();
                    break;
                default:
                    sb.append(status + "\r");
                    sb.append(read(sb, br));
                    br.close();
                    br.close();
            }
            //return body to auth
            return sb.toString();

        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    public static String executeDelete(String requestURL, Map<String, String> requestHeaders) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(requestURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");

            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }

            //Connect to the server
            connection.connect();

            int status = connection.getResponseCode();
            String statusText = connection.getResponseMessage();
            switch (status) {
                case 200:
                case 201:
                case 204:
                case 206:
                case 400:
                case 401:
                case 403:
                case 405:
                case 409:
                case 415:
                case 424:
                    return status + " " + statusText;
                default:
                    return status + " Failed to delete";
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (IOException ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.disconnect();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return ex.getMessage();
                }
            }
        }
    }

    public static StringBuilder read(StringBuilder sb, BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\r");
        }
        return sb;
    }

    public static StringBuilder readJson(StringBuilder sb, BufferedReader br) throws IOException {
        int read;
        char[] chars = new char[1024];
        while ((read = br.read(chars)) != -1) {
            sb.append(chars, 0, read);
        }
        return sb;
    }
}
