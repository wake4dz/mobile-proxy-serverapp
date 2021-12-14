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
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.ApplicationConstants.NewRelic;
import com.wakefern.global.BaseService;
import com.wakefern.global.ServiceMappings;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.logging.ReleaseUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(ApplicationConstants.NewRelic.Applications)
public class AddDeploymentDetail extends BaseService {

	private final static Logger logger = LogManager.getLogger(AddDeploymentDetail.class);

	@POST
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(ApplicationConstants.NewRelic.Deployments)
	public Response getInfoResponse(
			@PathParam("appId") String appId,
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
			String jsonData) {

		try {

			this.requestHeader = new MWGHeader();
			this.requestPath = ApplicationConstants.NewRelic.Applications +
					appId + ApplicationConstants.NewRelic.NewRelicDeployments;

			ServiceMappings secondMapping = new ServiceMappings();
			secondMapping.setMappingWithURL(this, ApplicationConstants.NewRelic.NewRelicURL);
			String secondMapPath = secondMapping.getPath();
			Map<String, String> nrHeader = new HashMap<String, String>();
			nrHeader.put(ApplicationConstants.NewRelic.NewRelicHeaderKey, 
					ApplicationUtils.getVcapValue(WakefernApplicationConstants.VCAPKeys.NEW_RELIC_KEY));
			nrHeader.put(ApplicationConstants.Requests.Header.contentType, MWGApplicationConstants.Headers.json);
			nrHeader.put(ApplicationConstants.Requests.Header.contentAccept, MWGApplicationConstants.Headers.json);
			secondMapping.setGenericHeader(nrHeader);
			
			JSONObject bodyObj = new JSONObject();
			JSONObject bodyDeployObj = new JSONObject();

			bodyDeployObj.put(NewRelic.Revision, ReleaseUtil.getReleaseLines().get(0).split("=")[1].trim());
			bodyDeployObj.put(NewRelic.Changelog, "Added: "+ReleaseUtil.getReleaseLines().get(2).split("=")[1].trim());
			bodyDeployObj.put(NewRelic.Description, ReleaseUtil.getReleaseLines().get(2).split("=")[1].trim());
			bodyDeployObj.put(NewRelic.User, ApplicationConstants.NewRelic.UserWFCAdmin);
			bodyDeployObj.put(NewRelic.Timestamp, getCurrentTime());
					
			bodyObj.put(NewRelic.Deployment,	bodyDeployObj);

			String jsonResp = HTTPRequest.executePost(secondMapPath, bodyObj.toString(), secondMapping.getGenericHeader());

			return this.createValidResponse(jsonResp);

		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.NEW_RELIC_DEPLOYMENT);

			logger.error("Exception! " + LogUtil.getExceptionMessage(e));

			return this.createErrorResponse("Exception!", e);
		}
	}
	
	private String getCurrentTime(){
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"); // "Z" for UTC indication
		df.setTimeZone(tz);
		return df.format(new Date());
	}
}