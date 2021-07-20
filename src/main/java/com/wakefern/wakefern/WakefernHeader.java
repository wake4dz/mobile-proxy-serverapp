package com.wakefern.wakefern;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.models.Header;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by brandyn.brosemer on 9/13/16.
 */
public class WakefernHeader extends Header {
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

    // TODO: all of this is unused. Think about removing in a separate branch.
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
        authMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
        authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

        setAllMaps(authMap);
    }

    public void serviceAuth(String token){
        Map<String, String> authMap = new HashMap<>();
        authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.xmlAcceptType);
        authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

        setAllMaps(authMap);
    }
}

