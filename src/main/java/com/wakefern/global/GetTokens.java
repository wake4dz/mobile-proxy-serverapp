package com.wakefern.global;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by zacpuste on 8/17/16.
 */
public class GetTokens {

//    public String getAuthorizationToken(Object serviceObject){
//        String token = "";
//        BaseService aService = (BaseService) serviceObject;
//
//        /**
//         * Pattern Matches:
//         * Token\":\"SSSSSSSS-SSSS-SSSS-SSSS-SSSSSSSSSSSS
//         */
//        Pattern pattern = Pattern.compile("Token.{3}\\S{8}-\\S{4}-\\S{4}-\\S{4}-\\S{12}");
//        Matcher m = pattern.matcher(aService.returnHeader);
//
//        if(m.find()) {
//            token = m.group(0);
//            return token = token.substring(8, token.length());
//        } else {
//            return token = "Failed to return header";
//        }
//    }

//    public String getAuthToken(Object serviceObject){
//        BaseService aService = (BaseService) serviceObject;
//        //return aService.request.getHeader("Authorization");
//        return "d74d4557-4b53-e611-9e95-d89d6763b1d9";
//    }

    /*
    public String getStoreId(Object serviceObject){
        BaseService aService = (BaseService) serviceObject;
        String url = aService.request.getRequestURL().toString();
        //System.out.println("Testing :: " + aService.request.getPathInfo());
        /**
         * Pattern matches:
         * Group 0:
         *  store/
         * Group 1:
         *  next 10 chars
         *
         *  Group 1:
         *      Match all chars until /
         /

        Pattern pattern = Pattern.compile("(store/)(.*)(/)");
        Matcher m = pattern.matcher(url);

        return m.group(1);

    }
    */
}
