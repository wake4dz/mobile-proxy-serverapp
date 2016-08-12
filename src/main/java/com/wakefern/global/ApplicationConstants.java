package com.wakefern.global;

import com.wakefern.mywebgrocer.MWGApplicationConstants;

public final class ApplicationConstants {
    public static final String testUser				    = "bbrosemer@gmail.com";
    public static final String password				    = "fuzzy2345";
    public static final String jsonResponseType		    = "application/json";
    public static final String jsonAcceptType		    = "application/json";
    public static final String authToken 				= "FE8803F0-D4FA-4AFF-B688-1A3BD5915FAA";

    public static class Requests{

        public static String baseURLV5 = MWGApplicationConstants.baseURL;
        public static String baseURLV1 = MWGApplicationConstants.baseURL;


        public static class Header{
            public static final String contentType	= "Content-Type";
            public static final String contentAccept = "Accept";
            public static final String contentAuthorization	= "Authorization";
        }

        public static class Authentication{
            public static final String Authenticate = MWGApplicationConstants.Requests.Authentication.Authenticate;
        }

    }
}
