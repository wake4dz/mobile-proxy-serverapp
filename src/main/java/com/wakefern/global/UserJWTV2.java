package com.wakefern.global;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author Danny Zheng
 * @date   2021-10-07
 */
public class UserJWTV2 {

	private final static Logger logger = LogManager.getLogger(UserJWTV2.class);
	private final static String userJwtSecret = EnvManager.getUserJwtSecret();
	private final static SecretKey key = Keys.hmacShaKeyFor(userJwtSecret.getBytes(StandardCharsets.UTF_8));

  /**
	 * Validate JWT (is it valid and signed correctly?)
	 * @param jwt String
	 * @return true if the JWT is valid, false otherwise.
	 */
	public static boolean isValid(String jwt) {

		String jws = jwt.replaceAll("Bearer ", "").trim();

		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws);
			return true;
		} catch (JwtException e) {
			logger.debug(String.format("JWS: %s, %s", jwt, e.getMessage()));
			return false;
		}
	}
	
	/**
	 * Generate JWT with PPC as subject. 
	 * 
	 * This should only be used to generate a token when a user's PPC has been
	 * verified.
	 * 
	 * @return JWS string with a sub claim of provided ppc
	 */
	public static String generate(String ppc, int expiresInSeconds) {
		Date now = new Date(System.currentTimeMillis());

		String jws = null;
		
		if (expiresInSeconds <= 0) {
			jws = Jwts.builder().setSubject(ppc).setIssuedAt(now).setExpiration(getDaysFromDate(now))
				.signWith(key).compact();
		} else { // we have a Restful API query parameter pass in
			expiresInSeconds = expiresInSeconds + 90; // add 90 seconds for buffer
			jws = Jwts.builder().setSubject(ppc).setIssuedAt(now).setExpiration(getSecondsFromDate(now, expiresInSeconds))
			.signWith(key).compact();			
		}

		return jws;
	}

	/**
	 * Checks a provided Bearer token for validity and sub claim against a ppc
	 * 
	 * @param bearerToken "Bearer ..."
	 * @param ppc         user's ppc
	 * @return if the bearer token jws is valid
	 */
	public static Boolean isValid(String bearerToken, String ppc) {
		if (ppc == null || bearerToken == null) {
			return false;
		}

		String jws = bearerToken.replaceAll("Bearer ", "");

		// Check and return false if the JWS is empty.
		if (jws.isEmpty() || jws.trim().isEmpty()) {
			return false;
		}

		try {
			Jwts.parserBuilder().requireSubject(ppc).setSigningKey(key).build().parseClaimsJws(jws);
			return true;
		} catch (JwtException e) {
			logger.debug(String.format("PPC: %s, JWS: %s, %s", ppc, jws, e.getMessage()));
			return false;
		}
	}

	/**
	 * Checks validity of JWT and returns PPC number (subject)
	 */
	public static String getPpcFromToken(String bearerToken) throws Exception {
		String jws = bearerToken.replaceAll("Bearer ", "");

		try {
			String ppc = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jws).getBody().getSubject();
			return ppc;
		} catch (Exception e) {
			throw new Exception("Failed to extract PPC from a PPC JWT token");
		}
	}

	/**
	 * Returns a date instance 4 days later than the provided date
	 * 
	 * @param date
	 * @return date 4 days later
	 */
	private static Date getDaysFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 4);
		return calendar.getTime();
	}
	
	/**
	 * Returns a date instance X seconds later than the provided date
	 * 
	 * @param date
	 * @return date X seconds later
	 */
	private static Date getSecondsFromDate(Date date, int seconds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, seconds);
		return calendar.getTime();
	}
}
