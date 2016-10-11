package com.wakefern.global.ErrorHandling;

import com.wakefern.global.ApplicationConstants;
import org.json.JSONObject;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ExceptionHandler {
    public static Exception Exception(Exception e){
        return e;
    }
    public static String ExceptionMessage(Exception e){
        return e.getMessage();
    }
    public String exceptionMessageJson(Exception e){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ApplicationConstants.ErrorMessage, e.getMessage());
        return jsonObject.toString();
    }
}
