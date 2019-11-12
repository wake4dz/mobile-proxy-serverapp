package com.wakefern.wakefern.receipt;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.VcapProcessor;
import com.wakefern.global.annotations.ValidatePPC;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.wakefern.jwt.token.WakefernApiTokenManager;
import org.apache.log4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * History:
 * Date:   2019-07-25
 * Author: Danny Zheng
 * Desc:   Retrieve a receipt summary list from Wakefern's Digital Receipt service
 */
@Path(WakefernApplicationConstants.Receipt.Proxy.Path)
public class GetSummary extends BaseService {
    private final static Logger logger = Logger.getLogger(GetSummary.class);

    @GET
    @Produces(MWGApplicationConstants.Headers.generic)
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Path("/")
    @ValidatePPC
    public Response getInfoResponse(@PathParam("ppc") String ppc,
                                    @QueryParam(MWGApplicationConstants.Requests.Params.Query.startDate) String startDate,
                                    @QueryParam(MWGApplicationConstants.Requests.Params.Query.endDate) String endDate) {
        String jwt = null;
        try {
            logger.info("Thread " + Thread.currentThread().getName() + " starting request (ppc: " + ppc + ") for GetSummary");

            jwt = WakefernApiTokenManager.getToken();

            // This is the Digital Receipt service provided and maintained by Wakefern.
            Map<String, String> wkfn = new HashMap<>();

            String path = WakefernApplicationConstants.Receipt.Upstream.BaseURL
                    + WakefernApplicationConstants.Receipt.Upstream.User
                    + "/" + ppc + "/receipts?startdate=" + startDate.trim() + "&enddate=" + endDate.trim();

            wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
            wkfn.put(ApplicationConstants.Requests.Header.jwtToken, jwt);

            String response = HTTPRequest.executeGet(path, wkfn, VcapProcessor.getApiMediumTimeout());

            return this.createValidResponse(response);

        } catch (Exception e) {
            LogUtil.addErrorMaps(e, MwgErrorType.RECEIPT_GET_SUMMARY);

            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc,
                    "startDate", startDate, "endDate", endDate, "jwtToken", jwt);

            logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}