package com.wakefern.api.proxy.wakefern.apim.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/*
 * 2022-08-01
 * TODO: carried over from the V7, remove it if it is for sure this API won't be used in the V8 mobile app
 */

public class ObtainFsnByEmail extends BaseService{

	private final static Logger logger = LogManager.getLogger(ObtainFsnByEmail.class);
	
	public String getInfo(String email){

	    this.requestPath = WakefernApplicationConstants.APIM.apimBaseURL + WakefernApplicationConstants.APIM.ppcByEmail;
	    
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
        headerMap.put(WakefernApplicationConstants.APIM.sub_key_header,
        		ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.APIM_PPC_EMAIL_KEY));
        JSONObject apimJsonStr = new JSONObject();
        apimJsonStr.put("email_addr", email);
	    String response = "";
	    
		try {
			response = HTTPRequest.executePostJSON(this.requestPath, apimJsonStr.toString(), headerMap, 0);
		} catch (Exception e) {
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return response;
		}

        return response;
	}
}
