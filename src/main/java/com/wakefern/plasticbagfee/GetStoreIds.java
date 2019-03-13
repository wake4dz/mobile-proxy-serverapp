package com.wakefern.plasticbagfee;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;

import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(MWGApplicationConstants.Requests.PlasticBagFee.prefix)
public class GetStoreIds extends BaseService{
	private final static Logger logger = Logger.getLogger(GetStoreIds.class);

	/**
	 * Constructor
	 */
    public GetStoreIds() {
        this.requestPath = MWGApplicationConstants.Requests.PlasticBagFee.prefix + MWGApplicationConstants.Requests.PlasticBagFee.storeIds;
    }
    
	@GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.PlasticBagFee.storeIds)
    public Response getResponse(			
	) {

    	JSONArray jaStores = new JSONArray();
    	String strArray[] = null;
    	String storeIds = null;
    	
		try {	

        	storeIds = MWGApplicationConstants.getSystemProperytyValue("plastic_bag_fee");
        	
        	// Defense code for some edge cases
        	// 1. If plastic_bag_fee is not defined
        	// 2. If plastic_bag_fee has the empty value
        	// 3. If plastic_bag_fee contains multiple commas without values
        	// 4. Trim and upper case the return JSON string
        	if (storeIds != null) {
        		strArray = storeIds.split(",");
        		
        		if ((strArray != null) && strArray.length > 0){
        			
            		for(int i=0; i < strArray.length; i++){
            			
            			if ((strArray[i] != null) && (strArray[i].trim().length() > 0)) {
            				jaStores.put(StringUtils.upperCase(strArray[i].trim()));
            			}
            		}
        		}
        	}
        			
            return this.createValidResponse(jaStores.toString());
        
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.PLASTIC_BAG_FEE_GET_STORE_IDS);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e), "storeIds", storeIds);
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

            return this.createErrorResponse(errorData, e);
        }
    }
}
