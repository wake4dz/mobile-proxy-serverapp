package com.wakefern.request;

import org.json.JSONException;

import com.ibm.json.java.JSONObject;
import com.wakefern.authentication.Credentials;

/**
 * Created by zacpuste on 8/5/16.
 */
public class Body {
	JSONObject jsonObject = null;

	public Body(String emailAddress, String password){
		try {
			buildBody( emailAddress, password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public void buildBody(String emailAddress, String password) throws JSONException{

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
