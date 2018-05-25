package com.wakefern.account.users;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.wakefern.dao.coupon.CouponDAO;
import com.wakefern.dao.user.Address;
import com.wakefern.dao.user.UserProfileDAO;
import com.wakefern.global.BaseService;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class GetProfile extends BaseService {

	private final static Logger logger = Logger.getLogger("GetProfile");
	
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
    		String jsonBody) {
			
    		try {
    			String jsonResponse = makeRequest(sessionToken, MWGApplicationConstants.Headers.Account.profile, chainID, userID);
    			return this.createValidResponse(assignPrimaryAddress(jsonResponse, userID));
    		
    		} catch (Exception e) {
    			logger.log(Level.SEVERE, "[getFullProfile]::Exception - Get user profile.  Message: " + e.toString());
            return this.createErrorResponse(e);
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
			if(addressList != null && !addressList.isEmpty()) {
				Address firstAddr = addressList.get(0); // get the first address
				for(Address addr : addressList) {
					if(addr.getIsDefaultBilling()) {
						return resp; // if primary addr found, return right away
					}
				}
				// if no primary addr found, set the first addr to be one & return it.
				firstAddr.setIsDefaultBilling(true);
	    			ObjectWriter writer = mapper.writer();
	    			logger.log(Level.SEVERE, "[assignPrimaryAddress]::No primary address" + userID);
	    			return writer.writeValueAsString(userProfileDAO);
			}
    		} catch(Exception e) {
    			logger.log(Level.SEVERE, "[assignPrimaryAddress]::Exception - Get user profile.  Message: " + e.getMessage());
    		}
    		return resp;
    }
}
