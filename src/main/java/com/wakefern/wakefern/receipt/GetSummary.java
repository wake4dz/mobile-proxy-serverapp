package com.wakefern.wakefern.receipt;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
 * Date:   2019-07-25 
 * Author: Danny Zheng
 * Desc:   Retrieve a receipt summary list from Wakefern's Digital Receipt service
 * 
 */

@Path(WakefernApplicationConstants.Receipt.DigitalReceipt)
public class GetSummary extends BaseService {

	private final static Logger logger = Logger.getLogger(GetSummary.class);
	
	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path("/{ppc}" + WakefernApplicationConstants.Receipt.Summary )
	public Response getInfoResponse(@PathParam("ppc") String ppc, 
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.startDate) String startDate,
			@QueryParam(MWGApplicationConstants.Requests.Params.Query.endDate) String endDate,
			@HeaderParam(MWGApplicationConstants.Headers.Params.jwtToken) String jwtToken) {

		try {
			// This is the Digital Receipt service provided and maintained by Wakefern.	        
	        Map<String, String> wkfn = new HashMap<>();

	        String path = WakefernApplicationConstants.Receipt.BaseURL + WakefernApplicationConstants.Receipt.User + "/" + ppc + "/receipts?startdate=" + startDate.trim() + "&enddate=" + endDate.trim();
	        
	        wkfn.put(ApplicationConstants.Requests.Header.contentType, "text/plain");
	        wkfn.put(ApplicationConstants.Requests.Header.jwtToken, jwtToken);
	        
	        String response = HTTPRequest.executeGet(path, wkfn, 10000);
			
			return this.createValidResponse(response);
	        
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.RECEIPT_GET_SUMMARY);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc, 
        			"startDate", startDate, "endDate", endDate, "jwtToken", jwtToken);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
		}
	}
}