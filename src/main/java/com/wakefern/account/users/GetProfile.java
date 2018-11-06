package com.wakefern.account.users;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.wakefern.dao.user.Address;
import com.wakefern.dao.user.PhoneNumber;
import com.wakefern.dao.user.UserProfileDAO;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class GetProfile extends BaseService {

	private final static Logger logger = Logger.getLogger(GetProfile.class);
	
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------
	
	/**
	 * Constructor
	 */
    public GetProfile() {
        this.requestPath = MWGApplicationConstants.Requests.Account.prefix + MWGApplicationConstants.Requests.Account.profile;
    } 

    @GET
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Account.profile)
    public Response getResponse(
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainID, 
    		@PathParam(MWGApplicationConstants.Requests.Params.Path.userID) String userID, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken, 
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonBody) {
			
		try {
			String jsonResponse = makeRequest(sessionToken, MWGApplicationConstants.Headers.Account.profile, chainID, userID);
			
			if(LogUtil.isUserTrackOn) {
				if ((userID != null) && LogUtil.trackedUserIdsMap.containsKey(userID.trim())) {
		        	String trackData = LogUtil.getRequestData("chainId", chainID, "userID", userID, "sessionToken", sessionToken );
					logger.info("Tracking data for " + userID + ": " + trackData + "; jsonResponse: " + jsonResponse);
				}
			}
			
			return this.createValidResponse(assignPrimaryAddress(jsonResponse, userID));
		
		} catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USERS_GET_PROFILE);
        	
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRevelantStackTrace(e), "chainId", chainID, 
        			"userID", userID, "sessionToken", sessionToken, "accept", accept, "contentType", contentType );
        	
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
    		
            return this.createErrorResponse(errorData, e);
		} 
    }
        
	//-------------------------------------------------------------------------
	// Private Methods
	//-------------------------------------------------------------------------

    private String makeRequest(String sessionToken, String acceptType, String chainID, String userID) throws Exception, IOException {
    		this.requestParams = new HashMap<String, String>();
        this.requestHeader = new MWGHeader(acceptType, MWGApplicationConstants.Headers.json, sessionToken);
    	        
        this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainID);
        this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.userID, userID);
        
        return this.mwgRequest(BaseService.ReqType.GET, null, "com.wakefern.account.users.GetProfile");
    }
    
    /**
     * quick fix for no primary address found in user profile, front end needs primary address for pickup order processing.
     * if no primary address found, assign one and return
     * @param resp response from MWG's user profile
     * @return
     */
    private String assignPrimaryAddress(String resp, String userID) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			UserProfileDAO userProfileDAO = mapper.readValue(resp, UserProfileDAO.class);
			List<Address> addressList = userProfileDAO.getAddresses();
			List<PhoneNumber> phoneNoList = userProfileDAO.getPhoneNumbers();
			boolean updateProfileObj = false;
			
			if(addressList != null && !addressList.isEmpty()) {
				Address firstAddr = addressList.get(0); // get the first address
				for(Address addr : addressList) {
					if(addr.getIsDefaultBilling()) {
						return resp; // if primary addr found, return right away
					}
				}
				// if no primary addr found, set the first addr to be one & return it.
				firstAddr.setIsDefaultBilling(true);
					logger.warn("[assignPrimaryAddress]::No primary address " + userID);
				updateProfileObj = true;
			} else {
				/**
				 * To fix scenario where user has no address, address array object is empty,
				 * in this case, assign dummy primary address to remediate the situation
				 */
				Address dummyAddr = new Address();
				dummyAddr.setIsDefaultBilling(true);
				addressList.add(dummyAddr);
				logger.warn("[assignPrimaryAddress]::empty address, assign dummy one " + userID);
				updateProfileObj = true;
			}
			
			// In case the phone number is not there, assign
			if(phoneNoList != null && phoneNoList.isEmpty()) {
				phoneNoList.add(new PhoneNumber());
					logger.warn("[assignPrimaryAddress]::No phone no " + userID);
					updateProfileObj = true;
			}
			
			if(updateProfileObj) {
	    			ObjectWriter writer = mapper.writer();
	    			return writer.writeValueAsString(userProfileDAO);
			}
		} catch(Exception e) {
			logger.error("[assignPrimaryAddress]::Exception - Get user profile.  Message: " + e.getMessage());
		}
		return resp;
    }
}
