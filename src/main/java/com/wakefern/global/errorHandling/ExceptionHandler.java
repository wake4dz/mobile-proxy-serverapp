package com.wakefern.global.errorHandling;

import com.wakefern.global.ApplicationConstants;
import org.json.JSONObject;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ExceptionHandler {
    public static String fromException(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ApplicationConstants.ErrorMessage, e.getMessage());
        return jsonObject.toString();
    }
    public static String fromMessage(String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ApplicationConstants.ErrorMessage, message);
        return jsonObject.toString();
    }
}
