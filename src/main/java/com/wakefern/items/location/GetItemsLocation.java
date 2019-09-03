package com.wakefern.items.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.global.ApplicationConstants;
import com.wakefern.global.ApplicationUtils;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.request.HTTPRequest;
import com.wakefern.wakefern.WakefernApplicationConstants;

/**
 * Created by loicao on 08/19/19.
 */
@Path(WakefernApplicationConstants.ItemLocator.prefix)
public class GetItemsLocation extends BaseService {
	private final static Logger logger = Logger.getLogger(GetItemsLocation.class);

	public JSONObject matchedObjects;
	
    @GET
    @Produces(MWGApplicationConstants.Headers.json)
    @Path(WakefernApplicationConstants.ItemLocator.itemLocatorPath)
    public Response getItemLocatorResponse(@HeaderParam(ApplicationConstants.Requests.Header.contentAccept) String acceptType, 
    				@HeaderParam(ApplicationConstants.Requests.Header.contentAuthorization) String authToken,
    				@PathParam(WakefernApplicationConstants.ItemLocator.storeId) String mwgStoreID,
    				@PathParam(WakefernApplicationConstants.ItemLocator.upcs) String upcs) {
        try {
	        String response = this.getItemLocatorResp(mwgStoreID, upcs, acceptType, authToken);
            return this.createValidResponse(response);
        } catch (Exception e){
			String errorData = LogUtil.getRequestData("getItemLocatorResponse::Exception", LogUtil.getRelevantStackTrace(e), "storeId", mwgStoreID, "upcs", upcs);
			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(e);
        }
    }
    
    /**
     * Get item locator response & reformatting the json resp to json object of UPCs.
     * @param mwgStoreID
     * @param upcs
     * @param acceptType
     * @return
     * @throws Exception
     */
    private String getItemLocatorResp(String mwgStoreID, String upcs, String acceptType, String auth) throws Exception{
		// split upcs into batch of 6, for that's the limit for item info api to take in
		List<String> upcList = this.splitUPCs(upcs);
		JSONObject upcsObj = new JSONObject();
		String response = "";
		for(String upcsStr: upcList){
            //Execute POST
            this.requestPath = ApplicationUtils.constructItemLocatorUrl(mwgStoreID, upcsStr);
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put(ApplicationConstants.Requests.Header.contentAccept, acceptType);
            headerMap.put(ApplicationConstants.Requests.Header.contentAuthorization, auth);
            
        	response = HTTPRequest.executeGet(this.requestPath, headerMap, 0);
        	JSONObject rdObj = new JSONObject(response).getJSONObject("returnData");
        	Iterator<String> upcIterator = rdObj.keys();
        	while(upcIterator.hasNext()){
        		String key = upcIterator.next();
        		upcsObj.put(overrideUPCKey(key,upcsStr), rdObj.get(key));
        	}
		}
        return upcsObj.toString();
    }
    
    /**
     * Item Info's UPC key value came in 13 digit format, comparing the key with 
     * the unformatted UPC input & override for consistency.
     * @param upcKey
     * @return
     */
    private String overrideUPCKey(String upcKey, String upcs){
    	String[] upcArr = upcs.split(",");
    	for(String upcStr: upcArr){
    		if(upcKey.contains(upcStr))
    			return upcStr;
    	}
    	return "";
    }
    
    /**
     * Item info api can only take in maximum of 6 upcs, over that and the service will return err,
     * 	so splitting upcs & make call to item locator in batch of 6 upcs.
     * @param upcs
     * @return
     */
    private List<String> splitUPCs(String upcs){
    	List<String> upcList = new ArrayList<String>();
    	StringBuilder sb = new StringBuilder();
    	int upcThreshold = 7;
    	int count=0;
    	String[] upcArr = upcs.split(",");
    	if(upcArr.length < upcThreshold){
    		upcList.add(upcs);
    	}else{
    		for(String upcStr: upcArr){
				sb.append(upcStr); sb.append(",");
				count++;
    			if(count > 5){
    				upcList.add(sb.toString());
    				count=0;
    				sb.setLength(0);
    			}
    		}
    		if(sb.length() > 0)
    		upcList.add(sb.toString());
    	}
    	return upcList;
    }
}
