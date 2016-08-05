package com.wakefern.global;

public final class ApplicationConfig {
    public static final String baseURL 				= "https://api.shoprite.com/api/";
    public static final String testUser				= "bbrosemer@gmail.com";
    public static final String password				= "fuzzy2345";
    public static final String authToken			= "FE8803F0-D4FA-4AFF-B688-1A3BD5915FAA";
    public static final String storeGroupId			= "3601";
    public static final String jsonResponse		    = "application/json";
    
    public static class Requests{

        public static class Authentication{
            public static final String Authenticate = "/authorization/v5/authorization";
        }

    }
}
