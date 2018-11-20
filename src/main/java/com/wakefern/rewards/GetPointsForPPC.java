package com.wakefern.rewards;


import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

import com.wakefern.request.HTTPRequest;

@Path(MWGApplicationConstants.Requests.Rewards.Points)
public class GetPointsForPPC extends BaseService {

	private final static Logger logger = Logger.getLogger(GetPointsForPPC.class);
	
	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path("/{ppc}")
	public Response getInfoResponse(@PathParam("ppc") String ppc, 
			
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			@HeaderParam("Authorization") String authToken) {

		try {
			// We are not going to a MWG endpoint with this request.
			// This is a service provided and maintained by Wakefern.
			// So it requires a different Authorization Header Token than the one provided by the UI.
			this.requestToken = MWGApplicationConstants.getProductRecmdAuthToken();
			this.requestPath  = MWGApplicationConstants.Requests.Rewards.Points + "/" + ppc;
			
			ServiceMappings srvMap = new ServiceMappings();
			
			srvMap.setMappingWithURL(this, MWGApplicationConstants.Requests.Rewards.baseURL);
			
			String srvPath = srvMap.getPath();
			
			Map<String, String> srvHead = srvMap.getgenericHeader();
			srvHead.remove(ApplicationConstants.Requests.Header.contentType);

		
			String response = HTTPRequest.executeGet(srvPath, srvHead, 0);
			return this.createValidResponse(response);
		
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.REWARDS_GET_POINTS_FOR_PPC);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "ppc", ppc, 
        			"authToken", authToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
		}
	}
}
