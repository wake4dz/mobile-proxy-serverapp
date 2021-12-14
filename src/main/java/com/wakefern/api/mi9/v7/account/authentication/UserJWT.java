/**
 * 
 */
package com.wakefern.api.mi9.v7.account.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.SecretKey;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.VcapProcessor;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * @author sfk1j
 */
public class UserJWT {

	private final static Logger logger = LogManager.getLogger(UserJWT.class);
	private final static String userJwtSecret = VcapProcessor.getUserJwtSecret();
	private final static SecretKey key = Keys.hmacShaKeyFor(userJwtSecret.getBytes(StandardCharsets.UTF_8));

	/**
	 * Generate JWT with PPC as subject. Expiration is set to 4 days. This
	 * expiration time is higher than the mi9 session expiration of 3 days to
	 * prevent a case of this JWT expiring before the mi9 session expiration.
	 * 
	 * This should only be used to generate a token when a user's PPC has been
	 * verified.
	 * 
	 * @return JWS string with a sub claim of provided ppc
	 */
	public static String generate(String ppc) {
		Date now = new Date(System.currentTimeMillis());

		String jws = Jwts.builder().setSubject(ppc).setIssuedAt(now).setExpiration(getFourDaysFromDate(now))
				.signWith(key).compact();

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
		if (ppc == null) {
			return false;
		}

		String jws = bearerToken.replaceAll("Bearer ", "");

		try {
			Jwts.parserBuilder().requireSubject(ppc).setSigningKey(key).build().parseClaimsJws(jws);
			return true;
		} catch (JwtException e) {
			logger.info(String.format("PPC: %s, JWS: %s, %s", ppc, jws, e.getMessage()));
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
			throw new Exception("Failed to extract PPC");
		}
	}

	/**
	 * Returns a date instance 4 days later than the provided date
	 * 
	 * @param date
	 * @return date 4 days later
	 */
	private static Date getFourDaysFromDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, 4);
		return calendar.getTime();
	}
}
