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
    public static String ExceptionMessageJson(Exception e){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ApplicationConstants.ErrorMessage, e.getLocalizedMessage());
        return jsonObject.toString();
    }
}
