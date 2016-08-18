package com.wakefern.mywebgrocer.models;

import java.util.HashMap;
import java.util.Map;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.request.models.Header;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGHeader extends Header {
	private Map<String, String> map = new HashMap<String, String>();

	public Map<String, String> getMap(){
		return map;
	}
	public void setMap( Map aMap){
		this.map = aMap;
	}

	public void setAllMaps(Map<String, String> aMap){
		super.put(aMap);
		setMap( aMap );
	}

	public void authenticate(){
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.contentAccept, ApplicationConstants.jsonResponseType);
		authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.jsonAcceptType);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization,ApplicationConstants.authToken);

		setAllMaps(authMap);
	}

	public void authenticate(String token){
		Map<String, String> authMap = new HashMap<>();

		authMap.put(ApplicationConstants.Requests.Header.contentAccept, ApplicationConstants.jsonResponseType);
		authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.jsonAcceptType);
		authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

		setAllMaps(authMap);
	}







}
