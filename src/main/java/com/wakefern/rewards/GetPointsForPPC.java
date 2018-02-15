package com.wakefern.rewards;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Response;

import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(MWGApplicationConstants.Requests.Rewards.Points)
public class GetPointsForPPC extends BaseService {

	@GET
	@Produces(MWGApplicationConstants.Headers.generic)
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Path("/{ppc}")
	public Response getInfoResponse(@PathParam("ppc") String ppc, @HeaderParam("Authorization") String authToken) throws Exception, IOException {

		// We are not going to a MWG endpoint with this request.
		// This is a service provided and maintained by Wakefern.
		// So it requires a different Authorization Header Token than the one provided by the UI.
		this.requestToken = WakefernApplicationConstants.Requests.authToken;
		this.requestPath  = MWGApplicationConstants.Requests.Rewards.Points + "/" + ppc;
		
		ServiceMappings srvMap = new ServiceMappings();
		
		srvMap.setMappingWithURL(this, MWGApplicationConstants.Requests.Rewards.baseURL);
		
		String srvPath = srvMap.getPath();
		
		Map<String, String> srvHead = srvMap.getgenericHeader();

		try {
			String response = HTTPRequest.executeGet(srvPath, srvHead, 0);
			return this.createValidResponse(response);
		
		} catch (Exception e) {
			return this.createErrorResponse(e);
		}
	}
}
