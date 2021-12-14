package com.wakefern.api.locai.recipes;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
/**  
* A proxy API to access Locai's 'Get User Info API, it has a JWT token and userId in its response data.
*   
* @author  Danny Zheng
*
*/ 

@Path(WakefernApplicationConstants.RecipeLocai.Proxy.path)
public class GetUserInfo extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetUserInfo.class);

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.RecipeLocai.Proxy.getUserInfo)
    public Response getResponse(
    		@QueryParam(WakefernApplicationConstants.RecipeLocai.RequestParamsQuery.sessionToken) String sessionToken,
    		@QueryParam(WakefernApplicationConstants.RecipeLocai.RequestParamsQuery.accountId) String accountId,
    		@QueryParam(WakefernApplicationConstants.RecipeLocai.RequestParamsQuery.clientBrand) String clientBrand,
    		@HeaderParam(WakefernApplicationConstants.RecipeLocai.HeadersParams.contentType) String contentType, 
    		String jsonBody) {

        Map<String, String> headers = new HashMap<>();

        try {
        	String path =  VcapProcessor.getTargetRecipeLocaiServiceEndpoint()  
        			+ "/shoprite/getUserInfo"
        			+ "?sessionToken=" + sessionToken
        			+ "&accountId=" + accountId
        			+ "&clientBrand=" + clientBrand
        			+ "&clientId=" + VcapProcessor.getRecipeClientId()
        			+ "&apiKey=" + VcapProcessor.getTargetRecipeLocaiApiKey();
        			
        	headers.put("Content-Type", contentType);

            return this.createValidResponse(HTTPRequest.executeGet(path, headers, VcapProcessor.getApiHighTimeout()));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.RECIPES_LOCAI_GET_USER_INFO);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"sessionToken", sessionToken, "accountId", accountId, "clientBrand", clientBrand, "contentType", contentType, "HttpBody", jsonBody);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
