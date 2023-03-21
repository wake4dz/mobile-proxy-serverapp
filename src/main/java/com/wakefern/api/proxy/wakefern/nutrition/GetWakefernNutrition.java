package com.wakefern.api.proxy.wakefern.nutrition;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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
import com.wakefern.logging.LogUtil;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * This class is used to retrieve any item doesn't have a nutrition description label on its package.
 * It is a Wakefern developed APIM API. It is mainly used to display any high sodium warning in the mobile app.
 * A Prod sample: https://apimprod.wakefern.com/WebAndClient/V1/mwgmenulabeling/00000000045-18E55966
 *
 * @author Danny Zheng
 * @version 1.0
 * @since 2020-03-10
 */
@Path(ApplicationConstants.Requests.Proxy + WakefernApplicationConstants.APIM.apimWakefernProduct)
public class GetWakefernNutrition extends BaseService {

    private final static Logger logger = LogManager.getLogger(GetWakefernNutrition.class);

    @GET
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.APIM.apimNutritionUrl)
    public Response getResponse(@PathParam("skuStoreId") String skuStoreId) {

        Map<String, String> wkfn = new HashMap<>();

        try {
            String path = WakefernApplicationConstants.APIM.apimBaseURL + WakefernApplicationConstants.APIM.nutritionBySkuStoreId + "/" + skuStoreId;

            wkfn.put(WakefernApplicationConstants.APIM.sub_key_header, EnvManager.getApimNutritionKey());
            wkfn.put(Requests.Headers.userAgent, ApplicationConstants.StringConstants.wakefernApplication);

            return this.createValidResponse(HTTPRequest.executeGet(path, wkfn, 0));

        } catch (Exception e) {
            String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e));
            
            if (LogUtil.isLoggable(e)) {
            	logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            }

            return this.createErrorResponse(errorData, e);
        }
    }
}
	