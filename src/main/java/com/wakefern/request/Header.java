package com.wakefern.request;
import com.wakefern.Constants;
import org.json.JSONException;

import com.ibm.json.java.JSONObject;
import com.wakefern.authentication.Credentials;

import java.io.IOException;

/**
 * Created by brandyn.brosemer on 8/3/16.
 */
public class Header {
	JSONObject jsonObject = null;

	public String getInfo() throws Exception{
		JSONObject headerJSON = new JSONObject();

		headerJSON.put(Constants.authHeaderType, Constants.headerJson);
		headerJSON.put(Constants.authHeaderAccept, Constants.headerJson);
		headerJSON.put(Constants.authHeaderToken, Constants.authHeaderToken);

		return headerJSON.toString();
	}

}
