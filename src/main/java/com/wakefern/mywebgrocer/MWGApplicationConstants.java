package com.wakefern.mywebgrocer;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGApplicationConstants {
    public static final String baseURL              = "https://api.shoprite.com/api";
    public static final String authToken			= "FE8803F0-D4FA-4AFF-B688-1A3BD5915FAA";
    public static final String storeGroupId			= "3601";

    public static class Requests{
        public static class Authentication{
            public static final String Authenticate     = "/authorization/v5/authorization";
            public static final String AuthenticateV1   = "/authorization/v5/authorization";

        }
    }
}
