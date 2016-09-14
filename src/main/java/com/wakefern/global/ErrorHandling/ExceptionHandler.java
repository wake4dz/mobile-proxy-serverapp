package com.wakefern.global.ErrorHandling;

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
}
