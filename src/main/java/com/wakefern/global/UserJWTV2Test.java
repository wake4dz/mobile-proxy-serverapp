package com.wakefern.global;

public class UserJWTV2Test {

	public static void main(String[] args) throws Exception {

		final String fsn = "47102818736";
		
		//String jwtToken = UserJWTV2.generate(fsn, 10);
		String jwtToken = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiI0MTg1MDA0MTk3MCIsImlhdCI6MTYzMzk2NTM1MiwiZXhwIjoxNjMzOTY1NDIwfQ.vCxoLMHk6WRRWTlm9TNgpOrtpMs0kS9xVetQGvU_xH1-P0z-3QhVBwt9T1AmE5Y8";
		
		System.out.println( fsn + "\'s JWT token: " + jwtToken);

		System.out.println("Get fsn# from a JWT: " + UserJWTV2.getPpcFromToken(jwtToken));

		Thread.sleep(72000);
		
		System.out.println("IsValid? for " + fsn + ": " + UserJWTV2.isValid(jwtToken, fsn));
		
		System.out.println("IsValid? for 41850041970: " + UserJWTV2.isValid(jwtToken, "41850041970"));
	}

}
