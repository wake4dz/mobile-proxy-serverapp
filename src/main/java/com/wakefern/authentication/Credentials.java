package com.wakefern.authentication;

/**
 * Created by brandyn.brosemer on 8/3/16.
 */
public class Credentials {
    private String emailAddress;
    private String password;    
    private String storeGroupId;
	
	private String authHeaderType;
	private String authHeaderAccept;
	private String authHeaderToken;

    public Credentials(String emailAddress, String password){
    	
    	this.emailAddress = emailAddress;
        this.password = password;
        //this.storeGroupId = Constants.storeGroupId;
    }
    
    public Credentials(){
//    	this.authHeaderType = buildAuths(Constants.contentType, Constants.headerJson);
//        this.authHeaderAccept = buildAuths(Constants.contentAccept, Constants.authenticateAccept);
//        this.authHeaderToken = buildAuths(Constants.contentAuthorization, Constants.authToken);
    }
    
    public static String buildAuths (String a , String b){
    	return a + ":" + b;
    }

	public String getEmailAddress() {
		return emailAddress;
	}
	public String getPassword() {
		return password;
	}
	
	public String getStoreGroupId(){
		return storeGroupId;
	}
	
	public String getAuthHeaderType(){
		return authHeaderType;
	}
	
	public String getAuthHeaderAccept(){
		return authHeaderAccept;
	}

	public String getAuthHeaderToken(){
		return authHeaderToken;
	}
}
