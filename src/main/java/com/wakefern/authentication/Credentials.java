package com.wakefern.authentication;

/**
 * Created by brandyn.brosemer on 8/3/16.
 */
public class Credentials {
    private String emailAddress;
    private String password;

    public Credentials(String emailAddress,String password){
        this.emailAddress = emailAddress;
        this.password = password;
    }
}
