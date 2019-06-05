package com.wakefern.coupons.v2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wakefern.dao.couponv2.CouponDAOV2;
import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;

/**
 * Created by loicao on 10/15/18.
 */
@Path(ApplicationConstants.Requests.CouponsV2.CouponMetadata)
public class GetCouponMetadata extends BaseService {
	private final static Logger logger = Logger.getLogger(GetCouponMetadata.class);

	public JSONObject matchedObjects;
	private long startTime, endTime;
	
    @POST
    @Consumes(MWGApplicationConstants.Headers.json)
    @Produces(MWGApplicationConstants.Headers.json)
    public Response getInfoResponse(@HeaderParam("Authorization") String authToken,
    									@HeaderParam(ApplicationConstants.Requests.Header.contentType) String contentType, 
    									@QueryParam(ApplicationConstants.Requests.CouponsV2.fsn) String fsn, 
    									String jsonString) throws Exception, IOException {
        //Execute POST
        this.requestPath = ApplicationUtils.constructCouponUrl(ApplicationConstants.Requests.CouponsV2.CouponMetadata, fsn).toString();
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put(ApplicationConstants.Requests.Header.contentType, contentType);
        headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, authToken);
        
        try {
			startTime = System.currentTimeMillis();
			matchedObjects = new JSONObject();
			
        	String response = HTTPRequest.executePostJSON(this.requestPath, jsonString, headerMap, 0);

			endTime = System.currentTimeMillis();
			logger.trace("[Coupons]::Total process time (ms): " + (endTime - startTime));

			ObjectMapper mapper = new ObjectMapper();
			CouponDAOV2[] couponDaoArr = mapper.readValue(response, CouponDAOV2[].class);

			ObjectWriter writer = mapper.writer();
			
			for(CouponDAOV2 couponDao : couponDaoArr){
				if(couponDao.getExternalId().equalsIgnoreCase("992632")){
					List<String> upcList = new ArrayList<String>() {
						{
							add("04119006152");
							add("04119006154");
							add("04119006153");
							add("04119005584");
						}
					};
					couponDao.getRequirementUpcs().addAll(upcList);
				}
			}
			
			String processedCouponResp = writer.writeValueAsString(couponDaoArr);
            return this.createValidResponse(processedCouponResp);
        } catch (Exception e){
			String errorData = LogUtil.getRequestData("GetCouponMetadata::Exception", LogUtil.getRelevantStackTrace(e), "fsn", fsn);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }
}
