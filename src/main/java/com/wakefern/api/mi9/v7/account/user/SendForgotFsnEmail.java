package com.wakefern.api.mi9.v7.account.user;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.wakefern.api.wakefern.apim.account.ObtainFsnByEmail;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.MWGApplicationConstants;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.wakefern.WakefernApplicationConstants;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class SendForgotFsnEmail extends BaseService {
	
	private final static Logger logger = Logger.getLogger(SendForgotFsnEmail.class);
	//-------------------------------------------------------------------------
	// Public Methods
	//-------------------------------------------------------------------------

	/**
	 * Constructor
	 */
    public SendForgotFsnEmail() {
        this.requestPath = MWGApplicationConstants.Requests.Account.prefix + MWGApplicationConstants.Requests.Account.forgotFsn;
    }
    
    /**
     * Send 
     * @param sessionToken
     * @param accept
     * @param contentType
     * @param jsonData data
     * @return
     */
	@POST
    @Consumes(MWGApplicationConstants.Headers.generic)
    @Produces(MWGApplicationConstants.Headers.generic)
    @Path(MWGApplicationConstants.Requests.Account.forgotFsn)
    public Response getResponse(		
    		@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
    		@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType,
    		String jsonData
	) {
		try {

			JSONObject emailObj  = new JSONObject(jsonData);
			String emailStr = emailObj.getString(WakefernApplicationConstants.APIM.email);
			
			String[] fsnStatus = getAPIMFsn(emailStr);
			String ppcStr = fsnStatus[0];
			JSONObject respObj = new JSONObject();
			respObj.put("email", emailStr);
			respObj.put("ppc_status", fsnStatus[1]);
			respObj.put("message", fsnStatus[2]);
			if(!ppcStr.isEmpty()){ //proceed to call MI9/MWG to send email
				this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json, MWGApplicationConstants.Headers.json, sessionToken);
				JSONObject mwgObj = new JSONObject(jsonData);
				mwgObj.put(WakefernApplicationConstants.APIM.mi9_fsn_key, ppcStr);
				mwgObj.put(WakefernApplicationConstants.APIM.mi9_first_name, WakefernApplicationConstants.APIM.mi9_fName_value);
				mwgObj.put(WakefernApplicationConstants.APIM.mi9_last_name, WakefernApplicationConstants.APIM.mi9_lName_value);
				//send email
	            this.mwgRequest(BaseService.ReqType.POST, mwgObj.toString(), "com.wakefern.account.user.SendForgotFsnEmail");
			}
            return this.createValidResponse(respObj.toString());
        } catch (Exception e) {
        	LogUtil.addErrorMaps(e, MwgErrorType.USER_SEND_FORGOT_FSN_EMAIL);
        	String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
        			"sessionToken", sessionToken, "accept", accept, "contentType", contentType );
    		logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));
            return this.createErrorResponse(errorData, e);
        }
    }
	
	
	/**
	 * Calling APIM to get PPC number, returns success & PPC# ONLY when there's 1 PPC in PPC Array, else returns error.
	 * @param emailStr
	 * @return PPC, status, & message String array.
	 */
	private String[] getAPIMFsn(String emailStr){
		String ppcStr="";
		String status = "ERROR";
		String message = WakefernApplicationConstants.APIM.notFoundStatus;
		ObtainFsnByEmail apimFsn = new ObtainFsnByEmail();
		String resp = apimFsn.getInfo(emailStr);
		if(!resp.isEmpty()){
			JSONArray ppcArr = new JSONObject(resp).getJSONArray(WakefernApplicationConstants.APIM.resultSet_output);
			if(ppcArr.length() == 1){
				JSONObject ppcIdObj = ppcArr.getJSONObject(0);
				ppcStr = ppcIdObj.getString(WakefernApplicationConstants.APIM.ppc_acct_id);
				status = "SUCCESS";
				message = WakefernApplicationConstants.APIM.foundStatus;
			}
			/**
			//comment this code out in case we want to use it to return multiple PPCs in the future
			else{
				for(int i=0; i<ppcArr.length(); i++){
					JSONObject ppcObj = ppcArr.getJSONObject(i);
					ppcStr = ppcStr+" "+ppcObj.getString(WakefernApplicationConstants.APIM.ppc_acct_id);
				}
			}
			*/
		}
		String [] msg={ppcStr.trim(), status, message};
		return msg;
	}
}

