package com.wakefern.apim.account;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.account.user.SendForgotFsnEmail;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

public class ObtainFsnByEmail extends BaseService{

	private final static Logger logger = Logger.getLogger(ObtainFsnByEmail.class);
	
	public String getInfo(String email){

	    this.requestPath = WakefernApplicationConstants.APIM.apimBaseURL + WakefernApplicationConstants.APIM.ppcByEmail;
	    
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
        headerMap.put(WakefernApplicationConstants.APIM.sub_key_header, 
        		MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.APIM_PPC_EMAIL_KEY));
        JSONObject apimJsonStr = new JSONObject();
        apimJsonStr.put("email_addr", email);
	    String response = "";
	    
		try {
			response = HTTPRequest.executePostJSON(this.requestPath, apimJsonStr.toString(), headerMap, 0);
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.APIM_FORGOT_FSN_EMAIL);
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return response;
		};
	    
	    return response;
	}
}
