package com.wakefern.api.wakefern.wallet;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
* A proxy API to access a home-grown Wallet API
*   
* @author  Danny Zheng
*
*/ 

@Path(WakefernApplicationConstants.Wallet.Proxy.Path)
public class GetWallet extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetWallet.class);

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.Wallet.Proxy.GetWallet)
    public Response getResponse(
    		@PathParam(WakefernApplicationConstants.Wallet.RequestParamsPath.Device) String device, 
    		@PathParam(WakefernApplicationConstants.Wallet.RequestParamsPath.AccountId) String accountId,
    		@PathParam(WakefernApplicationConstants.Wallet.RequestParamsPath.SessionToken) String sessionToken,
    		@HeaderParam(WakefernApplicationConstants.Wallet.HeadersParams.ContentType) String contentType) {

        Map<String, String> headers = new HashMap<>();
        
        try {
        	String path =  VcapProcessor.getTargetWalletServiceEndpoint() 
        			+ "/" + device
        			+ "/" + accountId
        			+ "/" + sessionToken;

            headers.put("Content-Type", contentType);
            headers.put("Authorization", VcapProcessor.getTargetWalletAuthorizationKey() );

            //note: first-time call for each user would take some time. After cache in the upstream system, calls are much faster.
            return this.createValidResponse(HTTPRequest.executeGet(path, headers, VcapProcessor.getApiMediumTimeout()));

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.WALLET_GET_WALLET);
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
            		"contentType", contentType, "device", device, "accountId", accountId, "sessionToken", sessionToken);
            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
