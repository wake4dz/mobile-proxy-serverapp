package com.wakefern.api.wakefern.newrelic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationConstants.NewRelic;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ReleaseUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.NewRelic.Applications)
public class AddDeploymentDetail extends BaseService {

	private final static Logger logger = LogManager.getLogger(AddDeploymentDetail.class);

	@POST
	@Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
	@Path(ApplicationConstants.NewRelic.Deployments)
	public Response getInfoResponse(
			@PathParam("appId") String appId,
			@HeaderParam(ApplicationConstants.Requests.Headers.Accept) String accept,
			@HeaderParam(ApplicationConstants.Requests.Headers.contentType) String contentType,
			String jsonData) {

		try {
			final String path = NewRelic.NewRelicURL + ApplicationConstants.NewRelic.Applications +
					appId + ApplicationConstants.NewRelic.NewRelicDeployments;

			Map<String, String> nrHeader = new HashMap<>();
			nrHeader.put(ApplicationConstants.NewRelic.NewRelicHeaderKey, 
					ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.NEW_RELIC_KEY));
			nrHeader.put(ApplicationConstants.Requests.Headers.contentType, ApplicationConstants.Requests.Headers.MIMETypes.json);
			nrHeader.put(ApplicationConstants.Requests.Headers.Accept, ApplicationConstants.Requests.Headers.MIMETypes.json);
			
			JSONObject bodyObj = new JSONObject();
			JSONObject bodyDeployObj = new JSONObject();

			bodyDeployObj.put(NewRelic.Revision, ReleaseUtil.getReleaseLines().get(0).split("=")[1].trim());
			bodyDeployObj.put(NewRelic.Changelog, "Added: "+ReleaseUtil.getReleaseLines().get(2).split("=")[1].trim());
			bodyDeployObj.put(NewRelic.Description, ReleaseUtil.getReleaseLines().get(2).split("=")[1].trim());
			bodyDeployObj.put(NewRelic.User, ApplicationConstants.NewRelic.UserWFCAdmin);
			bodyDeployObj.put(NewRelic.Timestamp, getCurrentTime());
					
			bodyObj.put(NewRelic.Deployment,	bodyDeployObj);

			String jsonResp = HTTPRequest.executePost(path, bodyObj.toString(), nrHeader);

			return createValidResponse(jsonResp);

		} catch (Exception e) {
			logger.error("Exception! " + LogUtil.getExceptionMessage(e));
			return createErrorResponse("Exception!", e);
		}
	}
	
	private String getCurrentTime(){
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // "Z" for UTC indication
		df.setTimeZone(tz);
		return df.format(new Date());
	}
}