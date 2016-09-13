package com.wakefern.global;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class ApplicationUtils {
    public static Boolean isEmpty(String str){
        if(str.isEmpty() || str == null){
            return true;
        }
        return false;
    }
}
