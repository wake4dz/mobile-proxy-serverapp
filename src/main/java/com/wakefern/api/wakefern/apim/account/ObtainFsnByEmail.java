package com.wakefern.api.wakefern.apim.account;

import java.util.HashMap;
import java.util.Map;

import com.wakefern.global.ApplicationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;
import com.wakefern.wynshop.WynshopApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

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
        	LogUtil.addErrorMaps(e, ErrorType.APIM_FORGOT_FSN_EMAIL);
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return response;
		}

        return response;
	}
}
