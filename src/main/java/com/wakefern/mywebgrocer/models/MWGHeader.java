package com.wakefern.mywebgrocer.models;

import java.util.HashMap;
import java.util.Map;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.models.Header;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGHeader extends Header {
	private Map<String, String> map = new HashMap<String, String>();

	/**
	 * Construct a MyWebGrocer Header pre-populated with header data for an upcoming request.
	 * 
	 * @param accepts
	 * @param contentType
	 * @param sessionToken
	 */
	public MWGHeader(String accepts, String contentType, String sessionToken) {
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
		
		authMap.put(ApplicationConstants.Requests.Header.contentAccept, accepts);
		authMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, sessionToken);

		setAllMaps(authMap);		
	}

	/**
	 * Construct a MyWebGrocer Header pre-populated with header data for an upcoming request.
	 * 
	 * @param accepts
	 * @param contentType
	 * @param sessionToken
	 */
	public MWGHeader(String sessionToken) {
		Map<String, String> authMap = new HashMap<>();
		
		authMap.put(ApplicationConstants.Requests.Header.userAgent, ApplicationConstants.StringConstants.wakefernApplication);
		
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, sessionToken);

		setAllMaps(authMap);		
	}
	
	/**
	 * Construct an empty MyWebGrocer Header.
	 */
	public MWGHeader() {
		// Nothing to do here
	}
	
	public Map<String, String> getMap(){
		return map;
	}
	
	public void setMap( Map<String, String> aMap) {
		this.map = aMap;
	}

	public void setAllMaps(Map<String, String> aMap){
		super.put(aMap);
		setMap( aMap );
	}

	public void authenticate(){
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.contentAccept, MWGApplicationConstants.Headers.json);
		authMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, MWGApplicationConstants.getAppToken());

		setAllMaps(authMap);
	}

	public void authenticate(String token){
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.contentAccept, MWGApplicationConstants.Headers.json);
		authMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.Account.login);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

		setAllMaps(authMap);
	}

	public void authenticate(String token, String content, String accept){
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.contentAccept, accept);
		authMap.put(ApplicationConstants.Requests.Header.contentType, content);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

		setAllMaps(authMap);
	}

	public void serviceAuth(String token){
		Map<String, String> authMap = new HashMap<>();
		authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.xmlAcceptType);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

		setAllMaps(authMap);
	}

	public void v1Authentication(String token){
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

		setAllMaps(authMap);
	}
}
