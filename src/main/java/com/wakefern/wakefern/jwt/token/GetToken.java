package com.wakefern.wakefern.jwt.token;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * History:
 * Date:   2019-08-08 
 * Author: Danny Zheng
 * Desc:   Retrieve a valid JWT token from a Wakefern's JWT server
 * 
 */

@Path(WakefernApplicationConstants.JwtToken.JwtToken)
public class GetToken extends BaseService {

	private final static Logger logger = Logger.getLogger(GetToken.class);

	
	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path(WakefernApplicationConstants.JwtToken.TokenGen )
	public Response getInfoResponse() {

		try {
			// This is the JWT token service provided and maintained by Wakefern.
	        Map<String, String> wkfn = new HashMap<>();

	        String path = WakefernApplicationConstants.JwtToken.BaseURL + WakefernApplicationConstants.JwtToken.authPath;
	        
	        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
	        wkfn.put(ApplicationConstants.Requests.Header.contentAuthorization, 
	        		MWGApplicationConstants.getSystemProperytyValue(WakefernApplicationConstants.VCAPKeys.jwt_public_key));
	            
	        String response = HTTPRequest.executeGet(path, wkfn, 10000);
			
			return this.createValidResponse(response);
		
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.JWT_TOKEN_GET_TOKEN);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
		}
	}
}


