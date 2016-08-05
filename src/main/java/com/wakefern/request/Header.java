package com.wakefern.request;
import org.json.JSONException;

import com.ibm.json.java.JSONObject;
import com.wakefern.authentication.Credentials;

/**
 * Created by brandyn.brosemer on 8/3/16.
 */
public class Header {
	JSONObject jsonObject = null;

	public Header(String emailAddress, String password){
		try {
			buildHeader( emailAddress, password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void buildHeader(String emailAddress, String password) throws JSONException{

		Credentials credentials = new Credentials (emailAddress, password);
	
		JSONObject jObj = new JSONObject();
		jObj.put("Email:", credentials.getEmailAddress());
		jObj.put("Password", credentials.getPassword());
		jObj.put("StoreGroupId", credentials.getStoreGroupId());

		this.jsonObject =  jObj;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}
}
