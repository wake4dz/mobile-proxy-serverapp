package com.wakefern.global.ErrorHandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ResponseHandler {
    public static String Response(HttpURLConnection connection) {
        try {
            int status = connection.getResponseCode();

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            switch (status) {
                case 200:
                    //Valid Status Code
                case 201:
                    //Valid Status code
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\r");
                    }
                    br.close();
                    break;
                default:
                    //sb.append(status);
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\r");
                    }
                    br.close();
            }
            return sb.toString();
        } catch (IOException e) {
           return ExceptionHandler.Exception(e).toString();
        }
    }
}
