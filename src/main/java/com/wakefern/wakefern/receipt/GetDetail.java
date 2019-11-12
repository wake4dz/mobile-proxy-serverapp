package com.wakefern.wakefern.receipt;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.wakefern.jwt.token.WakefernApiTokenManager;
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
 * Date:   2019-07-25
 * Author: Danny Zheng
 * Desc:   Retrieve a receipt detail info based on a receipt id from Wakefern's Digital Receipt service
 */
@Path(WakefernApplicationConstants.Receipt.Proxy.Path)
public class GetDetail extends BaseService {

    private final static Logger logger = Logger.getLogger(GetDetail.class);

    @GET
    @ValidatePPC
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path(WakefernApplicationConstants.Receipt.Proxy.Detail)
    public Response getInfoResponse(@PathParam("ppc") String ppc,
                                    @PathParam("receiptId") String receiptId) {
        logger.info("Thread " + Thread.currentThread().getName() + " starting request (ppc: " + ppc + ") for GetDetail");
        String jwt = null;
        try {
            jwt = WakefernApiTokenManager.getToken();
            // We are not going to a MWG endpoint with this request.
            // This is the Digital Receipt service provided and maintained by Wakefern.
            Map<String, String> wkfn = new HashMap<>();

            String path = WakefernApplicationConstants.Receipt.Upstream.BaseURL
                    + WakefernApplicationConstants.Receipt.Upstream.User + "/" + ppc + "/receipts/" + receiptId;

            wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
            wkfn.put(ApplicationConstants.Requests.Header.jwtToken, jwt);

            String response = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());

            return this.createValidResponse(response);

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.RECEIPT_GET_DETAIL);

            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc,
                    "receiptId", receiptId, "jwtToken", jwt);

            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}



