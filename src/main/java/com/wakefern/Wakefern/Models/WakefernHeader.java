package com.wakefern.Wakefern.Models;

import com.wakefern.Wakefern.WakefernApplicationConstants;
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

    public void authenticate(){
        Map<String, String> authMap = new HashMap<>();

        authMap.put(ApplicationConstants.Requests.Header.contentAccept, ApplicationConstants.jsonHeaderType);
        authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.jsonHeaderType);
        authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, MWGApplicationConstants.appToken);

        setAllMaps(authMap);
    }

    public void authenticate(String token){
        Map<String, String> authMap = new HashMap<>();

        authMap.put(ApplicationConstants.Requests.Header.contentAccept, ApplicationConstants.jsonHeaderType);
        authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.jsonHeaderType);
        authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

        setAllMaps(authMap);
    }

    public void serviceAuth(String token){
        Map<String, String> authMap = new HashMap<>();
        authMap.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.xmlAcceptType);
        authMap.put(ApplicationConstants.Requests.Header.contentAuthorization, token);

        setAllMaps(authMap);
    }

    public void cuponAuth(String token){
        Map<String, String> authMap = new HashMap<>();
        authMap.put(WakefernApplicationConstants.Requests.Coupons.Headers.CouponAuthenticationTokenHeader,
                token);

        setAllMaps(authMap);
    }
}

