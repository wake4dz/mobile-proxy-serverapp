package com.wakefern.api.wakefern.plasticbagfee;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.wakefern.WakefernApplicationConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.ErrorType;

// TODO: MM -evaluate this, is this necessary still?
@Path(WakefernApplicationConstants.PlasticBagFee.Proxy.prefix)
public class GetStoreIds extends BaseService{
	private final static Logger logger = LogManager.getLogger(GetStoreIds.class);
    
	@GET
    @Consumes(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Produces(ApplicationConstants.Requests.Headers.MIMETypes.generic)
    @Path(WakefernApplicationConstants.PlasticBagFee.Proxy.storeIds)
    public Response getResponse(			
	) {

    	JSONArray jaStores = new JSONArray();
    	String storeIds = null;
    	
		try {
        	storeIds = ApplicationUtils.getVcapValue("plastic_bag_fee");

        	// Defense code for some edge cases
        	// 1. If plastic_bag_fee is not defined
        	// 2. If plastic_bag_fee has the empty value
        	// 3. If plastic_bag_fee contains multiple commas without values
        	// 4. Trim and upper case the return JSON string
        	if (storeIds != null) {
        		String[] strArray = storeIds.split(",");
        		
        		if (strArray.length > 0){
					for (String s : strArray) {
						if ((s != null) && (s.trim().length() > 0)) {
							jaStores.put(StringUtils.upperCase(s.trim()));
						}
					}
        		}
        	}
        			
            return createValidResponse(jaStores.toString());
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, ErrorType.PLASTIC_BAG_FEE_GET_STORE_IDS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeIds", storeIds);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return createErrorResponse(errorData, e);
        }
    }
}
