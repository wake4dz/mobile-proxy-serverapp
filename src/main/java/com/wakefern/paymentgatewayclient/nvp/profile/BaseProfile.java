/*
 *  Copyright (C) 2015 - Wakefern Food Corporation
 */
package com.wakefern.paymentgatewayclient.nvp.profile;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is for the OAuth User Profile: USER, PWD, and SIGNATURE
 *
 * @author Wakefern Food Corp.
 * @version 1.0.0
 * @date 10/05/2015
 */
public final class BaseProfile implements Profile {

	private static final long serialVersionUID = 2720367273743771505L;

	// userName, password, and signature from OAuth client program 
    private final String userName;
    private final String password;
    private final String signature;

    /**
     * Builder class that constructs instance of BaseProfile class.
     */
    public static class Builder {
        private final String userName;
        private final String password;
        private final String signature;

        /**
         * Required parameters
         * 
         * @param userName user name
         * @param password
         * @param signature
         */
        public Builder(String userName, String password, String signature) {
            this.userName  = userName;
            this.password  = password;
            this.signature = signature;
        }

        /**
         * Returns instance of BaseProfile class
         * 
         * @return 
         */
        public BaseProfile build() {
            return new BaseProfile(this);
        }
    }

    /**
     * Private constructor invoked by builder static class
     * 
     * @param builder
     */
    private BaseProfile(Builder builder) {
        userName  = builder.userName;
        password  = builder.password;
        signature = builder.signature;
    }

    public Map<String, String> getNVPMap() {

        /* create and return map */
        Map<String, String> nvpMap = new HashMap<String, String>();
        nvpMap.put("USER", userName);
        nvpMap.put("PWD", password);
        nvpMap.put("SIGNATURE", signature);
        return nvpMap;
    }

    @Override
    public String toString() {
		return "Instance of OAuth's BaseProfile class with values. userName: "
				+ userName + ", password: " + password + ", signature: " + signature;
    }
    
}
