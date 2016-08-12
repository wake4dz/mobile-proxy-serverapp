package com.wakefern.mywebgrocer.models;

import java.util.HashMap;
import java.util.Map;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.request.models.Header;

/**
 * Created by brandyn.brosemer on 8/9/16.
 */
public class MWGHeader extends Header {
	
	public Map<String, String> authHeader(){
		Map<String, String> retval = new HashMap<>();
	    
		retval.put(ApplicationConstants.Requests.Header.contentAccept, ApplicationConstants.jsonResponseType);
		retval.put(ApplicationConstants.Requests.Header.contentType, ApplicationConstants.jsonAcceptType);
		retval.put(ApplicationConstants.Requests.Header.contentAuthorization,ApplicationConstants.authToken);
		
		return retval;
	}

}
