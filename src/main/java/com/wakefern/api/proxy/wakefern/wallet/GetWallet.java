package com.wakefern.api.proxy.wakefern.wallet;

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

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.Requests;
import com.wakefern.global.BaseService;
import com.wakefern.global.EnvManager;

import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
/**  
* A proxy API to access a home-grown Wallet API
*   
* @author  Danny Zheng
*
*/ 

/*
 * 2022-08-01
 * TODO: re-test after Sherry's back-end is ready
 */
@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.Wallet.Proxy.Path)
public class GetWallet extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetWallet.class);

    @GET
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.Wallet.Proxy.GetWallet)
    public Response getResponse(
    		@PathParam(WakefernApplicationConstants.Wallet.RequestParamsPath.Device) String device, // android or iOs
    		@PathParam(WakefernApplicationConstants.Wallet.RequestParamsPath.CustomerId) String customerId,
    		@PathParam(WakefernApplicationConstants.Wallet.RequestParamsPath.SessionToken) String sessionToken,
    		@HeaderParam(WakefernApplicationConstants.Wallet.HeadersParams.ContentType) String contentType) {

        Map<String, String> headers = new HashMap<>();
        
        try {
        	String path =  EnvManager.getTargetWalletServiceEndpoint()
        			+ "/" + device 
        			+ "/" + customerId
        			+ "/" + sessionToken;

            headers.put("Content-Type", contentType);
            headers.put("Authorization", EnvManager.getTargetWalletAuthorizationKey() );
            headers.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

            //note: first-time call for each user would take some time. After cache in the upstream system, calls are much faster.
            return this.createValidResponse(HTTPRequest.executeGet(path, headers, EnvManager.getApiMediumTimeout()));

        } catch (Exception e) {

			String errorData = parseAndLogException(logger, e, 
					WakefernApplicationConstants.Wallet.HeadersParams.ContentType, contentType,
					WakefernApplicationConstants.Wallet.RequestParamsPath.SessionToken, sessionToken,
					WakefernApplicationConstants.Wallet.RequestParamsPath.Device, device,
					WakefernApplicationConstants.Wallet.RequestParamsPath.CustomerId, customerId,
					"Authorization", EnvManager.getTargetWalletAuthorizationKey());
			
            return this.createErrorResponse(errorData, e);
        }
    }
}
