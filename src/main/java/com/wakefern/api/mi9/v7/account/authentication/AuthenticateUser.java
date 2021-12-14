package com.wakefern.api.mi9.v7.account.authentication;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.wakefern.api.mi9.v7.account.users.GetProfile;
import com.wakefern.global.BaseService;
import com.wakefern.logging.LogUtil;
import com.wakefern.logging.MwgApiWarnTime;
import com.wakefern.logging.MwgErrorType;
import com.wakefern.mywebgrocer.models.MWGHeader;
import com.wakefern.wakefern.WakefernApplicationConstants;
import com.wakefern.mywebgrocer.MWGApplicationConstants;

@Path(MWGApplicationConstants.Requests.Account.prefix)
public class AuthenticateUser extends BaseService {

	private final static Logger logger = LogManager.getLogger(AuthenticateUser.class);
	private final static GetProfile getProfile = new GetProfile();
	private final static String emailKey = "Email";
	private final static String pwKey = "Password";
	private final static String appVerKey = "AppVersion";
	private final static int apiWarnTime = MwgApiWarnTime.AUTHENTICATION_AUTHENTICATE_USER.getWarnTime();

	// -------------------------------------------------------------------------
	// Public Methods
	// -------------------------------------------------------------------------

	/**
	 * Constructor
	 */
	public AuthenticateUser() {
		this.requestPath = MWGApplicationConstants.Requests.Account.prefix
				+ MWGApplicationConstants.Requests.Account.login;
	}

	/**
	 * Improved login via custom accept which returns the user profile and PPC JWT
	 * in addition to the user guid. This replaces the login function.
	 * 
	 * @param chainId
	 * @param sessionToken
	 * @param accept
	 * @param contentType
	 * @param jsonBody
	 * @return user guid, profile, and PPC JWT
	 */
	@PUT
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(WakefernApplicationConstants.Headers.Accept.v1)
	@Path(MWGApplicationConstants.Requests.Account.login)
	public Response loginV2(@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainId,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType, String jsonBody) {

		Response loginResponse = null;

		// login as below
		try {
			loginResponse = this.sendRequest(chainId, sessionToken, accept, contentType, jsonBody);
		} catch (Exception e) {
			return this.createErrorResponse(e);
		}

		// {"AccountId":"...","FirstName":"..."}
		final JSONObject loginObj = new JSONObject(loginResponse.getEntity().toString());
		logger.info(loginObj.toString() + ", sessionToken=" + sessionToken);

		String profile = null;

		// with returned GUID (AccountId), get user profile
		try {
			profile = getProfile.getProfile(sessionToken, "application/vnd.mywebgrocer.account-profile-full+json",
					chainId, loginObj.getString("AccountId"));
		} catch (Exception e) {
			return this.createErrorResponse(e);
		}

		final JSONObject profileObj = new JSONObject(profile);

		// generate JWT with user profile PPC
		String ppc = null;

		try {
			ppc = profileObj.getString("FrequentShopperNumber");
		} catch (Exception e) {
			logger.error("User profile is missing Price Plus Card number: " + profileObj.getString("email"));
		}

		// if the user profile doesn't have a ppc, assign an invalid JWT and still allow
		// login knowing that endpoints requiring ppc will fail
		String jws = ppc != null ? UserJWT.generate(ppc) : "";

		// data to be returned to client
		final JSONObject responseJson = new JSONObject();

		for (String key : JSONObject.getNames(loginObj)) {
			responseJson.put(key, loginObj.get(key));
		}

		for (String key : JSONObject.getNames(profileObj)) {
			responseJson.put(key, profileObj.get(key));
		}

		responseJson.put("WakefernPPCToken", jws);

		// return json of guid, profile, and jwt
		return this.createValidResponse(responseJson.toString());
	}

	/**
	 * Logs in a user and binds the user to the session token.
	 * 
	 * @deprecated loginV2 should be used as it provides a PPC-securing JWT. This
	 *             endpoint remains to support users that have not updated their
	 *             mobile apps to make use of loginV2
	 * 
	 * @param chainId
	 * @param sessionToken
	 * @param accept
	 * @param contentType
	 * @param jsonBody
	 * @return JSON object with user guid
	 */
	@PUT
	@Consumes(MWGApplicationConstants.Headers.generic)
	@Produces(MWGApplicationConstants.Headers.generic)
	@Path(MWGApplicationConstants.Requests.Account.login)
	public Response login(@PathParam(MWGApplicationConstants.Requests.Params.Path.chainID) String chainId,
			@HeaderParam(MWGApplicationConstants.Headers.Params.auth) String sessionToken,
			@HeaderParam(MWGApplicationConstants.Headers.Params.accept) String accept,
			@HeaderParam(MWGApplicationConstants.Headers.Params.contentType) String contentType, String jsonBody) {

		JSONObject jsonData = new JSONObject(jsonBody);

		int appVer = (jsonData.has(appVerKey)) ? Integer.parseInt(jsonData.getString(appVerKey).split("\\.")[0]) : 0;

		// Reject all versions that are <= 2.x.x. Session cop fix.
		if (appVer < 3) {
			return this.createErrorResponse(new Exception("501, Please update the app to the latest version."));
		} else {
			return this.submitLogin(chainId, sessionToken, accept, contentType, jsonBody);
		}
	}

	private Response submitLogin(String chainId, String sessionToken, String accept, String contentType,
			String jsonBody) {
		try {
			return this.sendRequest(chainId, sessionToken, accept, contentType, jsonBody);
		} catch (Exception e) {
			return this.createErrorResponse(e);
		}
	}

	private Response sendRequest(String chainId, String sessionToken, String accept, String contentType,
			String jsonBody) throws Exception {

		JSONObject originalRequestBody = null;
		String email = null;

		try {
			try {
				if (chainId.isEmpty() || sessionToken.isEmpty() || accept.isEmpty() || contentType.isEmpty()) {
					throw new Exception("Insufficient data");
				}

				originalRequestBody = new JSONObject(jsonBody);
			} catch (Exception e) {
				throw new Exception("400, Data was provided in invalid format");
			}

			if (!originalRequestBody.has(emailKey) || !originalRequestBody.has(pwKey)) {
				throw new Exception("400, Email or password not provided");
			}

			final JSONObject requestBody = new JSONObject();

			email = originalRequestBody.getString(emailKey);
			final String password = originalRequestBody.getString(pwKey);

			requestBody.put(emailKey, email);
			requestBody.put(pwKey, password);

			this.requestToken = sessionToken;
			this.requestHeader = new MWGHeader(MWGApplicationConstants.Headers.json,
					MWGApplicationConstants.Headers.Account.login, sessionToken);
			this.requestParams = new HashMap<>();

			this.requestParams.put(MWGApplicationConstants.Requests.Params.Path.chainID, chainId);

			long startTime = System.currentTimeMillis();

			String responseJson = this.mwgRequest(BaseService.ReqType.PUT, requestBody.toString(),
					"com.wakefern.account.authentication.AuthenticateUser");

			long endTime = System.currentTimeMillis();
			long actualTime = endTime - startTime;

			if (actualTime > apiWarnTime) {
				logger.warn("com.wakefern.authentication.AuthenticateUser::sendRequest() - The API call took "
						+ actualTime + " ms to process the request, the warn time is " + apiWarnTime + " ms.");
			}

			return this.createValidResponse(responseJson);
		} catch (Exception e) {
			LogUtil.addErrorMaps(e, MwgErrorType.AUTHENTICATION_AUTHENTICATE_USER);

			String errorData = LogUtil.getRequestData("exceptionLocation", LogUtil.getRelevantStackTrace(e),
					"userEmail", email, "sessionToken", sessionToken, "accept", accept, "contentType", contentType);

			logger.error(errorData + " - " + LogUtil.getExceptionMessage(e));

			throw e;
		}
	}
}
